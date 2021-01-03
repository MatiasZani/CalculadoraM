package com.manzanzani.calculadoram.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.manzanzani.calculadoram.R

/*

    VIEW

 */

val View.alertDialog: () -> AlertDialog
    get() = {
        AlertDialog.Builder(this.context).setView(this).show()
    }

/*

    ARRAY LIST

 */
val ArrayList<String>.firstValue: String
    get() = this[0]

val ArrayList<String>.lastValue: String
    get() = this[this.size - 1]

val ArrayList<String>.lastIndex: Int
    get() = this.size - 1

val ArrayList<String>.toStringE: (Context) -> String
    get() = { this.joinToString().clear(it) }

/*

    STRING

 */

val String.clear: (Context) -> String
    get() = {
        this.replace(it.getString(R.string.coma), it.getString(R.string.empty)).replace(it.getString(R.string.espace), it.getString(R.string.empty))
    }

/*

    CONTEXT

 */

val Context.getPref: (String) -> Unit
    get() = {
        PreferenceManager.getDefaultSharedPreferences(this).getString(it, getString(R.string.empty))!!
    }

val Context.setPref: (String, String) -> Unit
    get() = { ref: String, value: String ->
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(ref, value).apply()
    }

val Context.toast: (String) -> Unit
    get() = {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

