package com.manzanzani.calculadoram.ui.others

import android.content.Context
import android.widget.Toast
import com.manzanzani.calculadoram.R

val ArrayList<String>.lastValue: String
    get() = this[this.size - 1]

val ArrayList<String>.lastIndex: Int
    get() = this.size - 1

val ArrayList<String>.toStringE: (Context) -> String
    get() = { this.joinToString().clear(it) }

val String.clear: (Context) -> String
    get() = {
        this.replace(it.getString(R.string.coma), it.getString(R.string.empty)).replace(it.getString(R.string.espace), it.getString(R.string.empty))
    }