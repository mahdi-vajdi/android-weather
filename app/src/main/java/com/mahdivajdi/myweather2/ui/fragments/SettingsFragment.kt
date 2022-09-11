package com.mahdivajdi.myweather2.ui.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.mahdivajdi.myweather2.R


class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}