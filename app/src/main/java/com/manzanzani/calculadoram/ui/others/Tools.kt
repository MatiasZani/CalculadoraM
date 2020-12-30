package com.manzanzani.calculadoram.ui.others

import android.content.Context
import android.widget.Toast
import com.manzanzani.calculadoram.R

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

val Context.toast: (String) -> Unit
    get() = {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
    }

