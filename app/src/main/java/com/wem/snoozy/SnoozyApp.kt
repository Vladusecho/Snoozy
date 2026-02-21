package com.wem.snoozy

import android.app.Application
import android.content.Context

class SnoozyApp : Application() {
    companion object {
        private lateinit var instance: SnoozyApp

        fun getContext(): Context = instance.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

}