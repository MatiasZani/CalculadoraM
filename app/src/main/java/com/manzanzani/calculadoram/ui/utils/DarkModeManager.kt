package com.manzanzani.calculadoram.ui.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.conectors.PreferencesDBConnector

object DarkModeManager {

    val setStyle = { value: Boolean ->
        AppCompatDelegate.setDefaultNightMode(if (value) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }

    val setStyleAndSave = { value: Boolean, context: Context ->
        setStyle(value)
        PreferencesDBConnector.uploadPref(context.getString(R.string.dark_mode), value.toString(), context)
    }

    val getStyle = { context: Context ->
        if (PreferencesDBConnector.retrievePref(context, context.getString(R.string.dark_mode)) == true.toString())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

}