package com.mahdivajdi.modernweather

import android.app.Application
import com.mahdivajdi.modernweather.data.local.AppDatabase

class App : Application() {

    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
}