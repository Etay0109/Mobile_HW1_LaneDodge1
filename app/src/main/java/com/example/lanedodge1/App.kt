package com.example.lanedodge1

import android.app.Application
import com.example.lanedodge1.utilities.SharedPreferencesManager
import com.example.lanedodge1.utilities.SignalManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        SignalManager.Companion.init(this)
    }
}