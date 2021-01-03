package com.manzanzani.calculadoram.repository.conectors

import android.content.Context
import androidx.preference.PreferenceManager
import com.manzanzani.calculadoram.R

object PreferencesDBConnector {

    val retrievePref= { ref: Context, value: String ->
        PreferenceManager.getDefaultSharedPreferences(ref).getString(value, ref.getString(R.string.empty))!!
    }

    val uploadPref = { ref: String, value: String, context: Context ->
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(ref, value).apply()
    }

}