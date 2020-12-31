package com.manzanzani.calculadoram.repository.classes

import android.content.Context

data class BasicDataToExist(
        val operators: List<String>,
        val operatorsFunctionsWithTwoNumbers: List<(Float, Float) -> Float>,
        val operatorsFunctionsWithOneNumber: List<(Float) -> Float>,
        val maxScreen: Int,
        val context: Context
        )
