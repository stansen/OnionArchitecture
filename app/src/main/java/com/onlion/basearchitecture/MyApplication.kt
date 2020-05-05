package com.onlion.basearchitecture

import android.app.Application
import com.onlion.onionshell.log.BaseEncryptedInitParam
import com.onlion.onionshell.log.initLogger
import timber.log.Timber

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initLogger(BuildConfig.DEBUG)
        Timber.plant(Timber.DebugTree())
    }

}