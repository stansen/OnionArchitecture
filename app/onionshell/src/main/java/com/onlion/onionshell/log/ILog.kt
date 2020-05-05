package com.onlion.onionshell.log

import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter

abstract class ILog {
    open var interceptEvent: ((
        priority: Int, tag: String?, message: String,
        t: Throwable?, isEncrypted: Boolean
    ) -> Boolean)? = null
    val explicitTag = ThreadLocal<String>()

    open fun getTag(): String? {
        val tag = explicitTag.get()
        if (tag != null) {
            explicitTag.remove()
        }
        return tag
    }

    abstract fun initLog(isDebug: Boolean, initParam: Any?)

    /**
     * Write a log message to its destination. Called for all level-specific methods by default.
     *
     * @param priority Log level. See [Log] for constants.
     * @param tag Explicit or inferred tag. May be `null`.
     * @param message Formatted log message. May be `null`, but then `t` will not be.
     * @param t Accompanying exceptions. May be `null`, but then `message` will not be.
     */
    abstract fun log(
        priority: Int, tag: String?, message: String,
        t: Throwable?, isEncrypted: Boolean = true
    )

    /** Return whether a message at `priority` or `tag` should be logged.  */
    open fun isLoggable(tag: String?, priority: Int): Boolean {
        return true
    }


    /** Log a verbose message with optional format args.  */
    fun v(isEncrypted: Boolean,message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,Log.VERBOSE, null, message, args)
    }

    /** Log a verbose exception and a message with optional format args.  */
    fun v(isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,Log.VERBOSE, t, message, args)
    }

    /** Log a verbose exception.  */
    fun v(isEncrypted: Boolean,t: Throwable?) {
         prepareLog(isEncrypted,Log.VERBOSE, t, null)
    }

    /** Log a debug message with optional format args.  */
    fun d(isEncrypted: Boolean,message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,Log.DEBUG, null, message, args)
    }

    /** Log a debug exception and a message with optional format args.  */
    fun d(isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,Log.DEBUG, t, message, args)
    }

    /** Log a debug exception.  */
    fun d(isEncrypted: Boolean,t: Throwable?) {
         prepareLog(isEncrypted,Log.DEBUG, t, null)
    }

    /** Log an info message with optional format args.  */
    fun i(isEncrypted: Boolean,message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,Log.INFO, null, message, args)
    }

    /** Log an info exception and a message with optional format args.  */
    fun i(isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,Log.INFO, t, message, args)
    }

    /** Log an info exception.  */
    fun i(isEncrypted: Boolean,t: Throwable?) {
         prepareLog(isEncrypted,Log.INFO, t, null)
    }

    /** Log a warning message with optional format args.  */
    fun w(isEncrypted: Boolean,message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,Log.WARN, null, message, args)
    }

    /** Log a warning exception and a message with optional format args.  */
    fun w(isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,Log.WARN, t, message, args)
    }

    /** Log a warning exception.  */
    fun w(isEncrypted: Boolean,t: Throwable?) {
         prepareLog(isEncrypted,Log.WARN, t, null)
    }

    /** Log an error message with optional format args.  */
    fun e(isEncrypted: Boolean,message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,Log.ERROR, null, message, args)
    }

    /** Log an error exception and a message with optional format args.  */
    fun e(isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,Log.ERROR, t, message, args)
    }

    /** Log an error exception.  */
    fun e(isEncrypted: Boolean,t: Throwable?) {
         prepareLog(isEncrypted,Log.ERROR, t, null)
    }

    /** Log an assert message with optional format args.  */
    fun wtf(isEncrypted: Boolean,message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,Log.ASSERT, null, message, args)
    }

    /** Log an assert exception and a message with optional format args.  */
    fun wtf(isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,Log.ASSERT, t, message, args)
    }

    /** Log an assert exception.  */
    fun wtf(isEncrypted: Boolean,t: Throwable?) {
         prepareLog(isEncrypted,Log.ASSERT, t, null)
    }

    /** Log at `priority` a message with optional format args.  */
    fun log(isEncrypted: Boolean,priority: Int, message: String?, vararg args: Any?) {
         prepareLog(isEncrypted,priority, null, message, args)
    }

    /** Log at `priority` an exception and a message with optional format args.  */
    fun log(isEncrypted: Boolean,
        priority: Int,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
         prepareLog(isEncrypted,priority, t, message, args)
    }


    /** Log at `priority` an exception.  */
    fun log(isEncrypted: Boolean,priority: Int, t: Throwable?) {
         prepareLog(isEncrypted,priority, t, null)
    }

    private fun prepareLog(isEncrypted: Boolean,
        priority: Int,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) { // Consume tag even when message is not loggable so that next message is correctly tagged.
        var newMessage: String? = message
        val tag: String? = getTag()
        if (OnionLog.logImpl?.isLoggable(tag, priority) == false) {
            return
        }
        if (newMessage != null && newMessage.isEmpty()) {
            newMessage = null
        }
        if (newMessage == null) {
            if (t == null) {
                return  // Swallow message if it's null and there's no throwable.
            }
            newMessage = getStackTraceString(t)
        } else {
            if (args.isNotEmpty()) {
                newMessage = formatMessage(newMessage, args)
            }
            if (t != null) {
                newMessage += "\n" + getStackTraceString(t)
            }
        }
        if (interceptEvent != null) {
            if (interceptEvent?.invoke(priority, tag, newMessage!!, t, isEncrypted) == false)
                log(priority, tag, newMessage!!, t,isEncrypted)
        } else {
            log(priority, tag, newMessage!!, t,isEncrypted)
        }
    }

    private fun getStackTraceString(t: Throwable): String? { // Don't replace this with Log.getStackTraceString() - it hides
// UnknownHostException, which is not what we want.
        val sw = StringWriter(256)
        val pw = PrintWriter(sw, false)
        t.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }

    /**
     * Formats a log message with optional arguments.
     */
    private fun formatMessage(
        message: String,
        vararg args: Any
    ): String? {
        return String.format(message, args)
    }
}
