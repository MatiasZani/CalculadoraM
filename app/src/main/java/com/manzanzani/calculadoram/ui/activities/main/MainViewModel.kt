package com.manzanzani.calculadoram.ui.activities.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manzanzani.calculadoram.repository.classes.ParenthesisIndex

class MainViewModel: ViewModel() {

    private val _screen = MutableLiveData<String>()
    val screen: LiveData<String> get() = _screen

    private val _story = MutableLiveData<String>()
    val story: LiveData<String> get() = _story

    private val _result = MutableLiveData<Float>()
    val result: LiveData<Float> get() = _result

    private val operation = ArrayList<String>().apply { add("") }

    private val parenthesisIndex = ArrayList<ParenthesisIndex>()
    private var parenthesisFlags = ArrayList<Boolean>()

    val show = { flag: Boolean ->

        val value = operation.joinToString().replace(",", "")

        if (flag) _screen.value = value
        else _story.value = value
    }

    val calculate = {

    }

    val addNumber = { value: Int ->
        operation[operation.size - 1] = operation[operation.size - 1] + value.toString()
        show(false)
    }

    val addOperator = { value: Int ->
        if (value != 4){
            operation.add(
                when(value){

                    0 -> "+"
                    1 -> "-"
                    2 -> "*"
                    else -> "/"

                }
            )
            operation.add("")
            show(false)
        }
        else{
            calculate()
            show(true)
        }
    }

    val addEspecial = { value: Int ->
        when(value){

            0 ->
                if (checkParenthesis()){
                    parenthesisFlags.add(true)
                    parenthesisIndex.add(ParenthesisIndex(operation.size - 1, null))
                    operation.add("(")
                    operation.add(")")
                }

            1 -> if (!checkParenthesis()){
                parenthesisFlags.remove(true)
                parenthesisIndex[parenthesisIndex.size - 1].end = operation.size - 1
            }

            else -> {

            }

        }
        show(true)
    }

    val checkParenthesis = { parenthesisFlags.isEmpty() }

}