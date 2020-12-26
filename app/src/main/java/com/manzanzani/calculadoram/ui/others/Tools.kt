package com.manzanzani.calculadoram.ui.others

import android.content.Context
import android.widget.Toast

val ArrayList<String>.toStringE: String
    get() = this.joinToString().replace(",", "").replace(" ", "")
