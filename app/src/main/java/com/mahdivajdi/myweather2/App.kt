package com.mahdivajdi.myweather2

import android.app.Application
import com.mahdivajdi.myweather2.data.local.AppDatabase

class App : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}