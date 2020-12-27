package com.manzanzani.calculadoram.repository.classes

import android.content.Context

data class BasicDataToExist(
        val operators: List<String>,
        val operatorsFunctions: List<(Float, Float) -> Float>,
        val maxScreen: Int,
        val context: Context
        )
