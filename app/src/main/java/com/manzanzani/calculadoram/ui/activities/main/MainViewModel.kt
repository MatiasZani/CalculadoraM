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


class MainViewModel: ViewModel() {

    private val MAX_OPERATION_SIZE = 25

    private lateinit var operators: List<String>

    private val operatorsFunctions=
            listOf(
                    { f1: Float, f2: Float -> f1 + f2}, // +
                    { f1: Float, f2: Float -> f1 - f2}, // -
                    { f1: Float, f2: Float -> f1 * f2}, // *
                    { f1: Float, f2: Float -> f1 / f2}, // /
                    { f1: Float, f2: Float -> f1 % f2} // %
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