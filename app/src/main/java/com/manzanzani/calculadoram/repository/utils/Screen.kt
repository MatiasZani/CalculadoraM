package com.manzanzani.calculadoram.repository.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manzanzani.calculadoram.repository.classes.ParenthesisIndex
import com.manzanzani.calculadoram.ui.others.toStringE

class Screen(calculator: Calculator, maxScreen: Int) {

    private val _screen = MutableLiveData<String>()
    val screen: LiveData<String> get() = _screen

    private val _story = MutableLiveData<String>()
    val story: LiveData<String> get() = _story

    private val _result = MutableLiveData<Float>()
    val result: LiveData<Float> get() = _result

    private var parenthesisFlags = ArrayList<Boolean>()

    val addAny = { value: Int, type: Int, context: Context ->
        if (calculator.operation.toStringE.toCharArray().size < maxScreen) listOf(addNumber, addOperator, addEspecial)[type](value)
        else Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
    }

    private val show = { flag: Boolean ->
        with(calculator){
            Log.i("operation", calculator.operation.toString())
            val value = operation.toStringE

            if (flag) _screen.value = value
            else _story.value = value
        }
    }

    private val addCharacter = { value: String ->
        with(calculator){
            val parenthesisValue = if (checkParenthesis()) operation.size - 1 else operation.size - 2

            val index = if (parenthesisValue < 0) 0 else parenthesisValue

            operation[index] = operation[index] + value
            show(false)
        }
    }

    private val addNumber = { value: Int -> addCharacter(value.toString()) }

    private val addOperator = { value: Int ->
        with(calculator){
            if (operation[operation.size - 1] !in listOf("+", "-", "*", "/") && operation[operation.size - 1] != ""){
                if (value != 4){

                    val parenthesisValue = if (checkParenthesis()) 0 else 1

                    operation.add(operation.size - parenthesisValue,
                            when(value){
                                0 -> "+"
                                1 -> "-"
                                2 -> "*"
                                else -> "/"
                            }
                    )

                    operation.add(operation.size - parenthesisValue,"")
                    show(false)
                }
                else{
                    parenthesisFlags.clear()
                    _result.value = calculate()
                    show(true)
                }
            }
        }
    }

    private val addEspecial = { value: Int ->
        with(calculator){
            val finishParenthesis = { if (operation[operation.size - 1] !in listOf("+", "-", "*", "/") && operation[operation.size - 1] != "") operation.add("*") }

            when(value){

                0 ->
                    if (checkParenthesis()){
                        parenthesisFlags.add(true)
                        finishParenthesis()
                        if (operation[operation.size - 1] == "") operation[operation.size - 1] = "("
                        else operation.add("(")

                        operation.add(")")
                    }

                1 -> if (!checkParenthesis()){
                    parenthesisFlags.remove(true)
                    finishParenthesis()
                }

                2 -> {

                }

                3 -> addCharacter(".")

                else -> {

                    val removeCharacter = {
                        val newValue  = operation[operation.size - 1].toCharArray().toMutableList().apply {
                            if (operation[operation.size - 1][0] == ')' && operation[operation.size - 2][0] == '(' && operation[operation.size - 2].toCharArray().size == 1){
                                operation[operation.size - 2] = operation[operation.size - 2].toCharArray().toMutableList().apply {
                                    removeAt(0)
                                }.joinToString().replace(",", "").replace(" ", "")
                            }
                            else if (operation[operation.size - 1][0] == '(') parenthesisFlags.remove(true)

                            removeAt(operation[operation.size - 1].toCharArray().size - 1)
                        }.joinToString().replace(",", "").replace(" ", "")

                        if (newValue.isEmpty()) operation.removeAt(operation.size - 1)
                        else operation[operation.size - 1] = newValue
                    }

                    if (operation.isNotEmpty()){
                        if (operation[operation.size - 1].isEmpty() && operation.size != 1){
                            operation.removeAt(operation.size - 1)
                            removeCharacter()
                        }
                        else if (operation[operation.size - 1].toCharArray().isNotEmpty()) removeCharacter()
                    }
                    else operation = arrayListOf("")

                }

            }
            show(false)
        }
    }

    val checkParenthesis = { parenthesisFlags.isEmpty() }

}