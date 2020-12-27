package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist
import com.manzanzani.calculadoram.ui.others.lastIndex
import com.manzanzani.calculadoram.ui.others.lastValue
import com.manzanzani.calculadoram.ui.others.toStringE

class Screen(private val c: Calculator, private val b: BasicDataToExist) {

    private val _screen = MutableLiveData<String>()
    val screen: LiveData<String> get() = _screen

    private val _story = MutableLiveData<String>()
    val story: LiveData<String> get() = _story

    private val _result = MutableLiveData<Float>()
    val result: LiveData<Float> get() = _result

    private var parenthesisCounter = 0

    val addAny = { value: Int, type: Int ->
        with(b){
            if (c.operation.toStringE(context).toCharArray().size < maxScreen) listOf(addNumber, addOperator, addEspecial)[type](value)
            else Toast.makeText(context, context.getString(R.string.max_lenght) + maxScreen.toString() + context.getString(R.string.digits), Toast.LENGTH_SHORT).show()
            Log.i("OP_EDITED", c.operation.toString())
        }
    }

    val restart = { btnRestart: View ->
        with(b){

            val restart = {
                btnRestart.visibility = View.VISIBLE
                btnRestart.setOnClickListener {
                    c.operation = arrayListOf(context.getString(R.string.empty))
                    _screen.value = context.getString(R.string.empty)
                    _story.value = context.getString(R.string.empty)
                }
            }

            if (screen.value.toString().isEmpty()){
                if (c.operation.size == 0 && _story.value.toString().isNotEmpty()) c.operation.add(context.getString(R.string.empty))
                if (c.operation[0].toCharArray().isNotEmpty()) restart()
                else btnRestart.visibility = View.GONE
            }
            else restart()
        }
    }

    val calculate = {
        _result.value = c.calculate()
        show(true)
    }

    val removeCharacter = {
        with(c){

            val checkParenthesisIndexs = { string: String ->
                if (string == b.context.getString(R.string.parenthesis_start)) parenthesisCounter--
                if (string == b.context.getString(R.string.parenthesis_end)) parenthesisCounter++
            }

            val removeLastCharacter = {
                checkParenthesisIndexs(operation.lastValue)
                operation[operation.lastIndex] = operation.lastValue.substring(0, operation.lastValue.length - 1)
            }

            val removeLastString = {
                checkParenthesisIndexs(operation.lastValue)
                operation.removeAt(operation.lastIndex)
            }

            if (operation.isNotEmpty()){
                if (operation.lastValue.isEmpty() && operation.size != 1){
                    removeLastString()
                    if (operation.lastValue.length > 1) removeLastCharacter()
                    else removeLastString()
                }
                else if (operation.lastValue.isNotEmpty()) removeLastCharacter()
                else Unit
            }
            else operation = arrayListOf(b.context.getString(R.string.empty))
            show(false)
        }
    }

    private val show = { flag: Boolean ->
        with(c){
            val value = operation.toStringE(b.context)
            if (flag) _screen.value = value
            else _story.value = value
        }
    }

    private val addCharacter = { value: String, flag: Boolean ->
        with(c){
            if (!flag) operation[operation.lastIndex] = operation.lastValue + value
            else{
                operation.add(value)
                operation.add(b.context.getString(R.string.empty))
            }
            show(false)
        }
    }

    private val addNumber = { value: Int -> addCharacter(value.toString(), false) }

    private val addOperator = { value: Int ->
        with(b){
            if (c.operation.lastValue !in context.resources.getStringArray(R.array.operatos) &&
                    when {
                        c.operation.lastIndex > 2 -> {
                            if (c.operation[c.operation.lastIndex - 1] == context.getString(R.string.parenthesis_end)){
                                c.operation.remove(context.getString(R.string.empty))
                                true
                            } else c.operation.lastValue.isNotEmpty()  &&
                            c.operation[c.operation.lastIndex] !in context.resources.getStringArray(R.array.operatos)
                        }

                        else -> c.operation.lastValue.isNotEmpty()
                    })

                if (value in operators.indices) addCharacter(operators[value], true)
        }
    }

    private val addEspecial = { value: Int ->
        with(c){
            when(value){

                0 -> {
                    if (operation.lastValue == b.context.getString(R.string.empty)) operation.removeLast()
                    addCharacter(b.context.getString(R.string.parenthesis_start), true)
                    parenthesisCounter++
                }

                1 ->{
                    Log.i("P", parenthesisCounter.toString())
                    if (parenthesisCounter >= 1){
                        val add = {
                            addCharacter(b.context.getString(R.string.parenthesis_end), true)
                            parenthesisCounter--
                        }
                        if(operation.lastIndex > 0){
                            if (operation.lastValue.isNotEmpty()) add()
                            else if (operation[operation.lastIndex - 1] == b.context.getString(R.string.parenthesis_end)){
                                parenthesisCounter--
                                operation[operation.lastIndex] = b.context.getString(R.string.parenthesis_end)
                                operation.add(b.context.getString(R.string.empty))
                            }
                        }
                    }
                }

                else -> addCharacter(b.context.getString(R.string.dot), false)

            }
            show(false)
        }
    }

}