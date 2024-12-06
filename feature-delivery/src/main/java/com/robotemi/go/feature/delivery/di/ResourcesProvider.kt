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
}
