package com.texdevs.agendafinanciera

import android.app.Application
import android.content.Context

class CurseFirebaseApp :Application() {

    companion object {
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}