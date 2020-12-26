package com.manzanzani.calculadoram.ui.activities.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manzanzani.calculadoram.repository.classes.ParenthesisIndex
import com.manzanzani.calculadoram.repository.utils.Calculator
import com.manzanzani.calculadoram.repository.utils.Screen
import com.manzanzani.calculadoram.ui.others.toStringE


class MainViewModel: ViewModel() {

    private val MAX_OPERATION_SIZE = 25

    val calculator = Calculator()

    val screen = Screen(calculator, MAX_OPERATION_SIZE)



}