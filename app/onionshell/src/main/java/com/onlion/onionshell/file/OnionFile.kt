package com.onlion.onionshell.file

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File


object OnionFile {

    private const val TAG = "OnionFile"

    /**
     * get cache file root path
     */
    fun getDiskCacheRootDir(context:Context): String? {
        val diskRootFile = if (existsSdcard()) {
            context.externalCacheDir
        } else {
            context.cacheDir
        }
        val cachePath= if (diskRootFile != null) {
            diskRootFile.path
        } else {
            throw IllegalArgumentException("disk is invalid")
        }
        return cachePath
    }


    fun getDiskCacheDir(context: Context,dirName: String?): String? {
        val dir = String.format("%s/%s/", getDiskCacheRootDir(context), dirName)
        val file = File(dir)
        if (!file.exists()) {
            val isSuccess = file.mkdirs()
            if (isSuccess) {
                Log.d(TAG, "dir mkdirs success")
            }
        }
        return file.path
    }
    /**
     * is sdcard available
     */
    private fun existsSdcard(): Boolean {
        return Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()
    }
}