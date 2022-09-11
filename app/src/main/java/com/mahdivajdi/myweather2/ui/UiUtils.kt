package com.mahdivajdi.myweather2.ui

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.preference.PreferenceManager
import com.mahdivajdi.myweather2.R
import java.time.*
import java.time.format.TextStyle
import java.util.*


fun ImageView.setIcon(icon: String) {
    setImageResource(when (icon) {
        "01d" -> R.drawable.a01d
        "01n" -> R.drawable.a01n
        "02d" -> R.drawable.a02d
        "02n" -> R.drawable.a02n
        "03d", "03n" -> R.drawable.a03d
        "04d", "04n" -> R.drawable.a04d
        "09d", "09n" -> R.drawable.a09d
        "10d" -> R.drawable.a10d
        "10n" -> R.drawable.a10n
        "11d", "11n" -> R.drawable.a11d
        "13d", "13n" -> R.drawable.a13d
        "50d", "50n" -> R.drawable.a50d
        else -> R.drawable.a01d
    })
}

fun TextView.setTime(epochTime: Long) {
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZoneId.systemDefault())
    text = "${date.hour}:00"
}

fun TextView.setDate(epochTime: Long) {
    val date = LocalDateTime.ofInstant(Instant.ofEpochSecond(epochTime), ZoneId.systemDefault())
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())

    text = "$dayOfWeek, $month ${date.dayOfMonth}"
}

fun getTemp(temp: Int, tempUnit: String): Int {
    return when (tempUnit) {
        "metric" -> (temp - 273.15).toInt()
        "imperial" -> (1.8 * (temp - 273.15) + 32).toInt()
        else -> temp
    }
}
