package com.onlion.onionshell.log

import android.content.Context
import android.os.Build
import android.util.Log
import com.onlion.onionshell.file.OnionFile
import com.tencent.mars.xlog.Xlog
import java.util.regex.Pattern

const val ONION_LOG = "onionlog"
const val ONION_CACHE_LOG = "onionlogcache"
const val DEFAULT_KEY = "onion_log"

class DefaultEncryptedLogImpl() : BaseEncryptedLogImpl() {
    private val MAX_LOG_LENGTH = 4000


    override fun normalLog(priority: Int, tag: String?, message: String, t: Throwable?) {
        if (message.length < MAX_LOG_LENGTH) {
            if (priority == Log.ASSERT) {
                Log.wtf(tag, message)
            } else {
                Log.println(priority, tag, message)
            }
            return
        }
        // Split by line, then ensure each line can fit into Log's maximum length.
        var i = 0
        val length = message.length
        while (i < length) {
            var newline = message.indexOf('\n', i)
            newline = if (newline != -1) newline else length
            do {
                val end = Math.min(newline, i + MAX_LOG_LENGTH)
                val part = message.substring(i, end)
                if (priority == Log.ASSERT) {
                    Log.wtf(tag, part)
                } else {
                    Log.println(priority, tag, part)
                }
                i = end
            } while (i < newline)
            i++
        }
    }

}

abstract class BaseEncryptedLogImpl : ILog() {

    private val MAX_TAG_LENGTH = 23
    private val CALL_STACK_INDEX = 5
    private val ANONYMOUS_CLASS =
        Pattern.compile("(\\$\\d+)+$")

    /**
     * Extract the tag which should be used for the message from the `element`. By default
     * this will use the class name without any anonymous class suffixes (e.g., `Foo$1`
     * becomes `Foo`).
     *
     *
     * Note: This will not be called if a [manual tag][.tag] was specified.
     */
    protected open fun createStackElementTag(element: StackTraceElement): String? {
        var tag = element.className
        val m = ANONYMOUS_CLASS.matcher(tag)
        if (m.find()) {
            tag = m.replaceAll("")
        }
        tag = tag.substring(tag.lastIndexOf('.') + 1)
        // Tag length limit was removed in API 24.
        return if (tag.length <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tag
        } else tag.substring(0, MAX_TAG_LENGTH)
    }

    override fun getTag(): String? {
        val tag = super.getTag()
        if (tag != null) {
            return tag
        }
        // DO NOT switch this to Thread.getCurrentThread().getStackTrace(). The test will pass
// because Robolectric runs them on the JVM but on Android the elements are different.
        val stackTrace =
            Throwable().stackTrace
        check(stackTrace.size > CALL_STACK_INDEX) { "Synthetic stacktrace didn't have enough elements: are you using proguard?" }
        return createStackElementTag(stackTrace[CALL_STACK_INDEX])
    }
    /**
     * Break up `message` into maximum-length chunks (if needed) and send to either
     * [Log.println()][Log.println] or
     * [Log.wtf()][Log.wtf] for logging.
     *
     * {@inheritDoc}
     */
    protected open fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {

    }

    override fun initLog(isDebug: Boolean,initParam: Any?) {
        (initParam as BaseEncryptedInitParam).apply {
            initXlog(context,isDebug,logFilePath,encryptKey)
        }
    }

    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?,
        isEncrypted: Boolean
    ) {

        if (isEncrypted) {
            when (priority) {
                Log.DEBUG -> com.tencent.mars.xlog.Log.d(tag, message)
                Log.ERROR -> com.tencent.mars.xlog.Log.e(tag, message)
                Log.INFO -> com.tencent.mars.xlog.Log.i(tag, message)
                Log.VERBOSE -> com.tencent.mars.xlog.Log.v(tag, message)
                Log.WARN -> com.tencent.mars.xlog.Log.w(tag, message)
            }
        } else {

            normalLog(priority, tag, message, t)
        }
    }

    abstract fun normalLog(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    )


}

class BaseEncryptedInitParam(
    val context: Context ,
    val logFilePath: String?,
    val encryptKey: String? = null
)


private fun initXlog(
    context: Context,
    isDebug: Boolean,
    logFilePath: String?,
    encryptKey: String? = null
) {
    System.loadLibrary("c++_shared")
    System.loadLibrary("marsxlog")
    //init xlog

    val logPath = logFilePath ?: OnionFile.getDiskCacheDir(context, ONION_LOG)
    val cachePath = context.filesDir.absolutePath + "/xlog"
    val encryptKeyString = encryptKey ?: ""
    if (isDebug) {
        Xlog.appenderOpen(
            Xlog.LEVEL_DEBUG,
            Xlog.AppednerModeAsync,
            cachePath,
            logPath,
            "XLog",
            0,
            encryptKeyString
        )
        Xlog.setConsoleLogOpen(true)
    } else {
        Xlog.appenderOpen(
            Xlog.LEVEL_INFO,
            Xlog.AppednerModeAsync,
            cachePath,
            logPath,
            "XLog",
            0,
            encryptKeyString
        )
        Xlog.setConsoleLogOpen(false)
    }

    com.tencent.mars.xlog.Log.setLogImp(Xlog())
}