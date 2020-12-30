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
import com.manzanzani.calculadoram.ui.others.toast

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
            else context.toast("${context.getString(R.string.max_lenght)} $maxScreen ${context.getString(R.string.digits)}")
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
        val calculate = {
            _result.value = c.calculate(parenthesisCounter)
            show(true)
        }

        when {
            c.operation.lastValue.isNotEmpty() -> calculate()
            c.operation[c.operation.lastIndex - 1] !in b.operators -> calculate()
            else -> b.context.toast(b.context.getString(R.string.format_no_vaild))
        }
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

            if (operation.isNotEmpty()){ // CASE [ 12 ] ... [ 1 ]
                if (operation.lastValue.isEmpty() && operation.size != 1){
                    removeLastString()
                    if (operation.lastValue.length > 1) removeLastCharacter() // CASE [ 123, +,  ]
                    else removeLastString() // CASE [ (, 1, +, 1, ),  ] ... [ (, 1, +, 1]
                }
                else if (operation.lastValue.isNotEmpty()) removeLastCharacter() // CASE [ 12 ] ... [ 1 ]
            }
            else operation = arrayListOf(b.context.getString(R.string.empty)) // CASE [ 1 ] ... [  ]

            show(false) // SHOW RESULT
            Log.i("OPERATION_REMOVE", operation.toString())
        }
    }

    private val show = { flag: Boolean ->
        with(c){
            val value = operation.toStringE(b.context) // GET VALUE
            if (flag) _screen.value = value // Show in SCREEN
            else _story.value = value // Show in STORY
        }
    }

    private val addCharacter = { value: String, flag: Boolean ->
        with(c){
            if (flag) { // CASE [ (, 123 ] ... [ (, 123, ),  ]
                operation.add(value)
                operation.add(b.context.getString(R.string.empty))
            }
            else  // CASE [ 12 ] ... [ 123 ]
                operation[operation.lastIndex] = operation.lastValue + value
            show(false) // SHOW RESULT
        }
    }

    private val addNumber = { value: Int -> addCharacter(value.toString(), false) }

    private val addOperator = { value: Int ->
        with(b){
            if (c.operation.lastValue !in context.resources.getStringArray(R.array.operatos) && // CASE [ 123, + ] ... [ 123, +, +] NOT POSSIBLE
                    when {
                        c.operation.lastIndex > 2 -> {
                            if (c.operation[c.operation.lastIndex - 1] == context.getString(R.string.parenthesis_end)){ // CASE [ 123, + ] ... [ 123, +, ) ] NOT POSIBLE
                                c.operation.remove(context.getString(R.string.empty))
                                true
                            }
                            else
                                c.operation.lastValue.isNotEmpty()  &&
                                c.operation[c.operation.lastIndex] !in context.resources.getStringArray(R.array.operatos)
                                // CASE [ (, 123, ), ] ... [ (, 123, ), +]
                        }

                        else -> c.operation.lastValue.isNotEmpty() // CASE [  ] ... [ + ] NOT POSSIBLE
                    })

                if (value in operators.indices) addCharacter(operators[value], true)
        }
    }

    private val addEspecial = { value: Int ->
        with(c){
            when(value){

                0 -> { // (
                    if (operation.lastValue == b.context.getString(R.string.empty)) operation.removeLast() // CASE [ 123 ] ... [ 123( ] NOT POSSIBLE
                    addCharacter(b.context.getString(R.string.parenthesis_start), true)
                    parenthesisCounter++
                }

                1 ->{ // )
                    Log.i("P", parenthesisCounter.toString())
                    if (parenthesisCounter >= 1){ // CASE [ ] ... [ ( ] NOT POSSIBLE
                        val add = {
                            addCharacter(b.context.getString(R.string.parenthesis_end), true)
                            parenthesisCounter--
                        }

                        if(operation.lastIndex > 0){ // CASE [ (, ] ... [ (, ) ] NOT POSSIBLE
                            if (operation.lastValue.isNotEmpty()) add()
                            else if (operation[operation.lastIndex - 1] == b.context.getString(R.string.parenthesis_end)){
                                parenthesisCounter--
                                add()
                            }
                        }
                    }
                }

                2 -> { // .
                    if (operation.lastValue.isNotEmpty() && // CASE [(, ] ... [(, .] NOT POSSIBLE
                        !operation.lastValue.contains(b.context.getString(R.string.dot).toCharArray()[0])) //CASE [(, 1.3] ... [(, 1.3.] NOT POSSIBLE
                        addCharacter(b.context.getString(R.string.dot), false)
                }


                else -> { // -
                    if (operation.lastValue.isEmpty()) // CASE [(, ] ... [(, -123]
                        operation[operation.lastIndex] = b.operators[1]
                    else  // CASE [(, 123, -, 123]
                        addOperator(4)
                }

            }
            show(false) // SHOW RESULT
        }
    }

}