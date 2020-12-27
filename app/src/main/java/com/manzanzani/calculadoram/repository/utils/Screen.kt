package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist
import com.manzanzani.calculadoram.ui.others.lastValue
import com.manzanzani.calculadoram.ui.others.toStringE

class Screen(private val c: Calculator, private val basicData: BasicDataToExist) {

    private val _screen = MutableLiveData<String>()
    val screen: LiveData<String> get() = _screen

    private val _story = MutableLiveData<String>()
    val story: LiveData<String> get() = _story

    private val _result = MutableLiveData<Float>()
    val result: LiveData<Float> get() = _result

    val addAny = { value: Int, type: Int ->
        with(basicData){
            if (c.operation.toStringE(context).toCharArray().size < maxScreen) listOf(addNumber, addOperator, addEspecial)[type](value)
            else Toast.makeText(context, context.getString(R.string.max_lenght) + maxScreen.toString() + context.getString(R.string.digits), Toast.LENGTH_SHORT).show()
        }
    }

    val restart = { btnRestart: View ->
        with(basicData){
            if (c.operation.isEmpty()) btnRestart.visibility = View.GONE
            else{
                btnRestart.visibility = View.VISIBLE
                btnRestart.setOnClickListener {
                    c.operation = arrayListOf(context.getString(R.string.empty))
                    _screen.value = context.getString(R.string.empty)
                    _story.value = context.getString(R.string.empty)
                }
            }
        }
    }

    private val show = { flag: Boolean ->
        with(c){
            val value = operation.toStringE(basicData.context)
            if (flag) _screen.value = value
            else _story.value = value
        }
    }

    private val addCharacter = { value: String, flag: Boolean ->
        with(c){
            if (!flag) operation[operation.lastIndex] = operation.lastValue + value
            else{
                operation.add(value)
                operation.add(basicData.context.getString(R.string.empty))
            }
            show(false)
        }
    }

    private val addNumber = { value: Int -> addCharacter(value.toString(), false) }

    private val addOperator = { value: Int ->
        with(basicData){
            if (c.operation.lastValue !in context.resources.getStringArray(R.array.operatos) && c.operation.lastValue.isNotEmpty())
                if (value in operators.indices) addCharacter(operators[value], true)
        }
    }

    private val addEspecial = { value: Int ->
        with(c){

            when(value){

                0 -> addCharacter(basicData.context.getString(R.string.parenthesis_start), true)

                1 -> if (operation.lastValue.isNotEmpty()) addCharacter(basicData.context.getString(R.string.parenthesis_end), true)

                2 -> addCharacter(basicData.context.getString(R.string.dot), false)

                3 -> {

                    val removeLastCharacter = {
                        operation[operation.lastIndex] = operation.lastValue.substring(0, operation.lastValue.length - 1)
                    }

                    val removeLastString = { operation.removeAt(operation.lastIndex) }

                    if (operation.isNotEmpty()){
                        if (operation.lastValue.isEmpty() && operation.size != 1){
                            removeLastString()
                            if (operation.lastValue.length > 1) removeLastCharacter()
                            else removeLastString()
                        }
                        else if (operation.lastValue.isNotEmpty()) removeLastCharacter()
                    }
                    else operation = arrayListOf(basicData.context.getString(R.string.empty))
                }

                else ->{

                    _result.value = c.calculate()
                    show(true)
                }
            }
            show(false)
        }
    }

}