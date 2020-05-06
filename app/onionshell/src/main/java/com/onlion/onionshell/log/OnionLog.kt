package com.onlion.onionshell.log

import android.app.Application
import android.util.Log
import com.tencent.mars.xlog.Xlog
import org.jetbrains.annotations.NonNls
import java.io.PrintWriter
import java.io.StringWriter


typealias Supplier<T> = () -> T

fun Application.initLogger(
    isDebug: Boolean,
    initParam: Any = BaseEncryptedInitParam(this, null, null),
    iLog: ILog? = DefaultEncryptedLogImpl()
) {

    iLog?.initLog(isDebug, initParam)
    OnionLog.logImpl = iLog

}

fun releaseLog() {
    com.tencent.mars.xlog.Log.appenderClose()
}

object OnionLog {
    var logImpl: ILog? = null

    fun setTag(tag: String) {
        logImpl?.explicitTag?.set(tag)
    }

    /** Log a verbose message with optional format args.  */
    fun v(isEncrypted: Boolean, message: String?, vararg args: Any?) {
        logImpl?.v(isEncrypted, message, args)
    }

    /** Log a verbose exception and a message with optional format args.  */
    fun v(
        isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.v(isEncrypted, t, message, args)
    }

    /** Log a verbose exception.  */
    fun v(isEncrypted: Boolean, t: Throwable?) {
        logImpl?.v(isEncrypted, t)
    }

    /** Log a debug message with optional format args.  */
    fun d(isEncrypted: Boolean, message: String?, vararg args: Any?) {
        logImpl?.d(isEncrypted, message, args)
    }

    /** Log a debug exception and a message with optional format args.  */
    fun d(
        isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.d(isEncrypted, t, message, args)
    }

    /** Log a debug exception.  */
    fun d(isEncrypted: Boolean, t: Throwable?) {
        logImpl?.d(isEncrypted, t)
    }

    /** Log an info message with optional format args.  */
    fun i(isEncrypted: Boolean, message: String?, vararg args: Any?) {
        logImpl?.i(isEncrypted, message, args)
    }

    /** Log an info exception and a message with optional format args.  */
    fun i(
        isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.i(isEncrypted, t, message, args)
    }

    /** Log an info exception.  */
    fun i(isEncrypted: Boolean, t: Throwable?) {
        logImpl?.i(isEncrypted, t)
    }

    /** Log a warning message with optional format args.  */
    fun w(isEncrypted: Boolean, message: String?, vararg args: Any?) {
        logImpl?.w(isEncrypted, message, args)
    }

    /** Log a warning exception and a message with optional format args.  */
    inline fun w(
        isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.w(isEncrypted, t, message, args)
    }

    /** Log a warning exception.  */
    fun w(isEncrypted: Boolean, t: Throwable?) {
        logImpl?.w(isEncrypted, t)
    }

    /** Log an error message with optional format args.  */
    fun e(isEncrypted: Boolean, message: String?, vararg args: Any?) {
        logImpl?.e(isEncrypted, message, args)
    }

    /** Log an error exception and a message with optional format args.  */
    fun e(
        isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.e(isEncrypted, t, message, args)
    }

    /** Log an error exception.  */
    fun e(isEncrypted: Boolean, t: Throwable?) {
        logImpl?.e(isEncrypted, t)
    }

    /** Log an assert message with optional format args.  */
    fun wtf(isEncrypted: Boolean, message: String?, vararg args: Any?) {
        logImpl?.wtf(isEncrypted, message, args)
    }

    /** Log an assert exception and a message with optional format args.  */
    fun wtf(
        isEncrypted: Boolean,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.wtf(isEncrypted, t, message, args)
    }

    /** Log an assert exception.  */
    fun wtf(isEncrypted: Boolean, t: Throwable?) {
        logImpl?.wtf(isEncrypted, t)
    }

    /** Log at `priority` a message with optional format args.  */
    fun log(isEncrypted: Boolean, priority: Int, message: String?, vararg args: Any?) {
        logImpl?.log(isEncrypted, priority, message, args)
    }

    /** Log at `priority` an exception and a message with optional format args.  */
    fun log(
        isEncrypted: Boolean,
        priority: Int,
        t: Throwable?,
        message: String?,
        vararg args: Any?
    ) {
        logImpl?.log(isEncrypted, priority, t, message, args)
    }


    /** Log at `priority` an exception.  */
    fun log(isEncrypted: Boolean, priority: Int, t: Throwable?) {
        logImpl?.log(isEncrypted, priority, t)
    }

}


fun setLogTag(tag: String) = OnionLog.setTag(tag)

/** Log a verbose message with optional format args.  */
fun logv(@NonNls message: String?, vararg args: Any?) = OnionLog.v(true, message, *args)

/** Log a verbose exception and a message with optional format args.  */
fun logv(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.v(true, t, message, *args)

/** Log a verbose exception.  */
fun logv(t: Throwable?) = OnionLog.v(true, t)

/** Log a debug message with optional format args.  */
fun logd(@NonNls message: String?, vararg args: Any?) = OnionLog.d(true, message, *args)

/** Log a debug exception and a message with optional format args.  */
fun logd(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.d(true, t, message, *args)

/** Log a debug exception.  */
fun logd(t: Throwable?) = OnionLog.d(true, t)

/** Log an info message with optional format args.  */
fun logi(@NonNls message: String?, vararg args: Any?) = OnionLog.i(true, message, *args)

/** Log an info exception and a message with optional format args.  */
fun logi(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.i(true, t, message, *args)

/** Log an info exception.  */
fun logi(t: Throwable?) = OnionLog.i(true, t)

/** Log a warning message with optional format args.  */
fun logw(@NonNls message: String?, vararg args: Any?) = OnionLog.w(true, message, *args)

/** Log a warning exception and a message with optional format args.  */
fun logw(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.w(true, t, message, *args)

/** Log a warning exception.  */
fun logw(t: Throwable?) = OnionLog.w(true, t)

/** Log an error message with optional format args.  */
fun loge(@NonNls message: String?, vararg args: Any?) = OnionLog.e(true, message, *args)

/** Log an error exception and a message with optional format args.  */
fun loge(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.e(true, t, message, *args)

/** Log an error exception.  */
fun loge(t: Throwable?) = OnionLog.e(true, t)

/** Log an assert message with optional format args.  */
fun logwtf(@NonNls message: String?, vararg args: Any?) = OnionLog.wtf(true, message, *args)

/** Log an assert exception and a message with optional format args.  */
fun logwtf(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.wtf(true, t, message, *args)

/** Log an assert exception.  */
fun logwtf(t: Throwable?) = OnionLog.wtf(true, t)


//not encrypt
/** Log a verbose message with optional format args.  */
fun logvN(@NonNls message: String?, vararg args: Any?) = OnionLog.v(false, message, *args)

/** Log a verbose exception and a message with optional format args.  */
fun logvN(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.v(false, t, message, *args)

/** Log a verbose exception.  */
fun logvN(t: Throwable?) = OnionLog.v(false, t)

/** Log a debug message with optional format args.  */
fun logdN(@NonNls message: String?, vararg args: Any?) = OnionLog.d(false, message, *args)

/** Log a debug exception and a message with optional format args.  */
fun logdN(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.d(false, t, message, *args)

/** Log a debug exception.  */
fun logdN(t: Throwable?) = OnionLog.d(false, t)

/** Log an info message with optional format args.  */
fun logiN(@NonNls message: String?, vararg args: Any?) = OnionLog.i(false, message, *args)

/** Log an info exception and a message with optional format args.  */
fun logiN(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.i(false, t, message, *args)

/** Log an info exception.  */
fun logiN(t: Throwable?) = OnionLog.i(false, t)

/** Log a warning message with optional format args.  */
fun logwN(@NonNls message: String?, vararg args: Any?) = OnionLog.w(false, message, *args)

/** Log a warning exception and a message with optional format args.  */
fun logwN(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.w(false, t, message, *args)

/** Log a warning exception.  */
fun logwN(t: Throwable?) = OnionLog.w(false, t)

/** Log an error message with optional format args.  */
fun logeN(@NonNls message: String?, vararg args: Any?) = OnionLog.e(false, message, *args)

/** Log an error exception and a message with optional format args.  */
fun logeN(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.e(false, t, message, *args)

/** Log an error exception.  */
fun logeN(t: Throwable?) = OnionLog.e(false, t)

/** Log an assert message with optional format args.  */
fun logwtfN(@NonNls message: String?, vararg args: Any?) = OnionLog.wtf(false, message, *args)

/** Log an assert exception and a message with optional format args.  */
fun logwtfN(t: Throwable?, @NonNls message: String?, vararg args: Any?) =
    OnionLog.wtf(false, t, message, *args)


/** Log an assert exception.  */
fun logwtfN(t: Throwable?) = OnionLog.wtf(false, t)


/** Log at `priority` a message with optional format args.  */
fun log(
    isEncrypted: Boolean,
    priority: Int, @NonNls message: String?,
    vararg args: Any?
) {
    OnionLog.log(isEncrypted, priority, message, *args)
}

/** Log at `priority` an exception and a message with optional format args.  */
fun log(
    isEncrypted: Boolean,
    priority: Int,
    t: Throwable?, @NonNls message: String?,
    vararg args: Any?
) {
    OnionLog.log(isEncrypted, priority, t, message, *args)
}

/** Log at `priority` an exception.  */
fun log(isEncrypted: Boolean, priority: Int, t: Throwable?) {
    OnionLog.log(isEncrypted, priority, t)
}