package com.robotemi.go.feature.delivery.di

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.annotation.StringRes
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getContext(): Context {
        return context
    }

    fun getString(@StringRes stringResId: Int): String {
        return context.getString(stringResId)
    }

    fun getMusic(): Uri? {
        val musicDir = File(Environment.getExternalStorageDirectory().path + "/TemiGo/data")
        val musicFiles = musicDir.listFiles()
        if (!musicFiles.isNullOrEmpty()) {
            return musicFiles[0].toUri()
        }
        return null
    }
}
