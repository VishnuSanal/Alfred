package com.vishnu.alfred

import android.content.Context
import java.io.File

class Utils {
    companion object {
        var cacheDirectory: File? = null
        fun getCacheDir(context: Context): File {
            if (cacheDirectory == null) cacheDirectory = context.cacheDir
            return cacheDirectory!!
        }
    }
}