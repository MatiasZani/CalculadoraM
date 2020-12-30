package com.manzanzani.calculadoram.ui.activities.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist
import com.manzanzani.calculadoram.repository.classes.ParenthesisIndex
import com.manzanzani.calculadoram.repository.utils.Calculator
import com.manzanzani.calculadoram.repository.utils.Screen
import com.manzanzani.calculadoram.ui.others.toStringE

infix fun Float.plus(otherNum: Float): Float = this.plus(otherNum)
infix fun Float.minus(otherNum: Float): Float = this.minus(otherNum)
infix fun Float.divided(otherNum: Float): Float = this.div(otherNum)
infix fun Float.multiplied(otherNum: Float): Float = this.times(otherNum)
infix fun Float.percentage(otherNum: Float): Float = this % otherNum

class MainViewModel: ViewModel() {

    private val MAX_OPERATION_SIZE = 50

    private lateinit var operators: List<String>

    private val operatorsFunctions =
            listOf(
                    { num1: Float, num2: Float -> num1 plus num2}, // +
                    { num1: Float, num2: Float -> num1 multiplied  num2}, // *
                    { num1: Float, num2: Float -> num1 divided  num2}, // /
                    { num1: Float, num2: Float -> num1 percentage num2}, // %
                    { num1: Float, num2: Float -> num1 minus num2} // -
            )

    private lateinit var calculator: Calculator
    lateinit var screen: Screen

    val init = { context: Context ->
        with(context){
            operators = resources.getStringArray(R.array.operatos).toList()

            val basicData =
                    BasicDataToExist(
                            operators,
                            operatorsFunctions,
                            MAX_OPERATION_SIZE,
                            context
                    )

            calculator = Calculator(basicData)
            screen = Screen(calculator, basicData)
        }
    }



}