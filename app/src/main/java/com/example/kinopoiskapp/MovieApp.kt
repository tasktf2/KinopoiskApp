package com.example.kinopoiskapp

import android.app.Application
import android.content.Context
import com.example.kinopoiskapp.di.GlobalDI

class MovieApp : Application() {

    val globalDI: GlobalDI by lazy { GlobalDI }

    override fun onCreate() {
        super.onCreate()

        appContext = this
    }

    override fun onTerminate() {
        super.onTerminate()
        appContext = null
    }

    companion object {
        var appContext: Context? = null
    }
}