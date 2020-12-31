package com.manzanzani.calculadoram.ui.activities.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist
import com.manzanzani.calculadoram.repository.utils.Calculator
import com.manzanzani.calculadoram.repository.utils.Screen
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

infix fun Float.plus(otherNum: Float): Float = this.plus(otherNum)
infix fun Float.minus(otherNum: Float): Float = this.minus(otherNum)
infix fun Float.divided(otherNum: Float): Float = this.div(otherNum)
infix fun Float.multiplied(otherNum: Float): Float = this.times(otherNum)
infix fun Float.percentage(otherNum: Float): Float = this % otherNum
infix fun Float.pow(otherNum: Float): Float = this.toDouble().pow(otherNum.toDouble()).toFloat()

class MainViewModel: ViewModel() {

    lateinit var screen: Screen

    val init = { context: Context ->
        with(context){

            val maxOperationSize = 50

            val operatorsFunctionsWithTwoNumbers =
                    listOf(
                            { num1: Float, num2: Float -> num1 plus num2}, // +
                            { num1: Float, num2: Float -> num1 multiplied  num2}, // *
                            { num1: Float, num2: Float -> num1 divided  num2}, // ÷
                            { num1: Float, num2: Float -> num1 percentage num2}, // %
                            { num1: Float, num2: Float -> num1 pow num2}, // ^
                            { num1: Float, num2: Float -> num1 divided  num2}, // /
                            { num1: Float, num2: Float -> num1 minus num2}, // -
                    )

            val operatorsFunctionsWithOneNumber =
                    listOf(
                            { num1: Float -> sqrt(num1.toDouble()).toFloat() }, // √
                            { num1: Float -> ln(num1.toDouble()).toFloat() } // L
                    )


            val operators = resources.getStringArray(R.array.operatos).toList() // arrys

            val basicData =
                    BasicDataToExist(
                            operators,
                            operatorsFunctionsWithTwoNumbers,
                            operatorsFunctionsWithOneNumber,
                            maxOperationSize,
                            context
                    )

            screen = Screen(Calculator(basicData), basicData)
        }
    }

}