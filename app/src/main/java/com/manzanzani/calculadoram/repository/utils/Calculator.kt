package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manzanzani.calculadoram.repository.classes.ParenthesisIndex

class Calculator {


    var operation = ArrayList<String>().apply { add("") }

    val getResult = { operationList: ArrayList<String> ->
        operation.remove(")")

        while (operationList.size != 1){
            when(operationList[1]){
                "+" -> operationList[0] = (operationList[0].toFloat() + operationList[2].toFloat()).toString()
                "-" ->  operationList[0] = (operationList[0].toFloat() - operationList[2].toFloat()).toString()
                "*" -> operationList[0] = (operationList[0].toFloat() * operationList[2].toFloat()).toString()
                else -> operationList[0] = (operationList[0].toFloat() / operationList[2].toInt()).toString()
            }
            operationList.removeAt(1)
            operationList.removeAt(1)
        }

        operationList[0]
    }

    val calculate = {

        getResult(operation).toFloat()
    }

}