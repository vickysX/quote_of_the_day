package com.example.quoteoftheday.modules

import android.content.Context
import androidx.startup.Initializer
import androidx.work.WorkManager
import dagger.Module
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
object WorkManagerInitializer : Initializer<WorkManager> {

    override fun create(@ApplicationContext context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    override fun dependencies(): MutableList<Class<out Initializer<*>>> {
        return emptyList<Class<out Initializer<*>>>().toMutableList()
    }

}