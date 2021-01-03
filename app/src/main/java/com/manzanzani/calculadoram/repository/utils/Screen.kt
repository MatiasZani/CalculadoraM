package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist
import com.manzanzani.calculadoram.ui.utils.lastIndex
import com.manzanzani.calculadoram.ui.utils.lastValue
import com.manzanzani.calculadoram.ui.utils.toStringE
import com.manzanzani.calculadoram.ui.utils.toast

class Screen(private val c: Calculator, private val b: BasicDataToExist) {

    private val _screen = MutableLiveData<String>()
    val screen: LiveData<String> get() = _screen

    private val _story = MutableLiveData<String>()
    val story: LiveData<String> get() = _story

    private var parenthesisCounter = 0

    private var operation = ArrayList<String>().apply { add(b.context.getString(R.string.empty)) }

    val addAny = { value: Int, type: Int ->
        with(b){
            if (operation.toStringE(context).toCharArray().size < maxScreen) // OPERATION SIZE > MAX SCREEN NOT POSIBLE
                listOf(addNumber, addOperator, addEspecial)[type](value)
            else    // OPERATION SIZE > MAX SCREEN
                context.toast("${context.getString(R.string.max_lenght)} $maxScreen ${context.getString(R.string.digits)}")
            Log.i("OP_EDITED", operation.toString())
        }
    }

    val removeCharacter = {
        with(c){

            val checkParenthesisIndexs = { string: String ->
                if (string == b.context.getString(R.string.parenthesis_start)) parenthesisCounter--
                if (string == b.context.getString(R.string.parenthesis_end)) parenthesisCounter++
            }

            val removeLastCharacter = { // CASE [ (, 123 ] ... [ (, 12 ]
                checkParenthesisIndexs(operation.lastValue)
                operation[operation.lastIndex] = operation.lastValue.substring(0, operation.lastValue.length - 1)
            }

            val removeLastString = { // CASE [ (,  ] ... [ ( ]
                checkParenthesisIndexs(operation.lastValue)
                operation.removeAt(operation.lastIndex)
            }

            if (operation.isNotEmpty()){ // CASE [ 12 ] ... [ 1 ]
                if (operation.lastValue.isEmpty() && operation.size != 1){
                    removeLastString()
                    if (operation.lastValue.length > 1) removeLastCharacter() // CASE [ 123, +,  ]
                    else removeLastString() // CASE [ (, (,  ] ... [ (,  ]
                }
                else if (operation.lastValue.isNotEmpty()) removeLastCharacter() // CASE [ 12 ] ... [ 1 ]
            }
            else operation = arrayListOf(b.context.getString(R.string.empty)) // CASE [ 1 ] ... [  ]

            show(false) // SHOW RESULT
            Log.i("OPERATION_REMOVE", operation.toString())
        }
    }

    val calculate = {
        val calculate = {
            c.calculate(operation, parenthesisCounter)
            show(true)
        }

        when {
            operation.lastValue.isNotEmpty() -> calculate() // CASE [ ] ... [  ] NOT POSSIBLE
            operation[operation.lastIndex - 1] !in b.operators -> calculate() // CASE [ 123, + ] ... [  ] NOT POSSIBLE
            else -> b.context.toast(b.context.getString(R.string.format_no_vaild)) // CASE [ 8, + ] ... [ 8, + ]
        }
    }

    val configFunction = { btn: TextView ->
        with(b){
            if (operation[0].isNotEmpty()) btn.text = context.getString(R.string.ce)
            else btn.text = context.getString(R.string.c)
        }
    }

    val function = {
        with(b){
            if (operation[0].isNotEmpty()){
                operation = arrayListOf(context.getString(R.string.empty))
                show(false)
            }
            else {
                if (_screen.value == null) _screen.value = context.getString(R.string.empty)
                else if(screen.value!! != context.getString(R.string.empty)) _screen.value = context.getString(R.string.empty)
            }
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
            if (operation.lastValue !in context.resources.getStringArray(R.array.operatos) && // CASE [ 123, + ] ... [ 123, +, +] NOT POSSIBLE
                    when {
                        operation.lastIndex > 2 -> {
                            if (operation[operation.lastIndex - 1] == context.getString(R.string.parenthesis_end)){ // CASE [ 123, + ] ... [ 123, +, ) ] NOT POSIBLE
                                operation.remove(context.getString(R.string.empty))
                                true
                            }
                            else
                                operation.lastValue.isNotEmpty()  &&
                                operation[operation.lastIndex] !in context.resources.getStringArray(R.array.operatos)
                                // CASE [ (, 123, ), ] ... [ (, 123, ), +]
                        }

                        else -> operation.lastValue.isNotEmpty() // CASE [  ] ... [ + ] NOT POSSIBLE
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


                3 -> { // -
                    if (operation.lastValue.isEmpty()) // CASE [(, ] ... [(, -123]
                        operation[operation.lastIndex] = b.operators[8]
                    else  // CASE [(, 123, -, 123]
                        addOperator(8)
                }

                4 -> {
                    if (operation.lastValue.isEmpty()) { // CASE [(, ] ... [(, √123]
                        operation[operation.lastIndex] = b.operators[6]
                        operation.add(b.context.getString(R.string.empty))
                    }
                    else  // CASE [(, 123, √, 123]
                        addCharacter(b.operators[6], true)
                }

                5 -> {
                    if (operation.lastValue.isEmpty()) { // CASE [(, ] ... [(, L123]
                        operation[operation.lastIndex] = b.operators[7]
                        operation.add(b.context.getString(R.string.empty))
                    }
                    else  // CASE [(, 123, L, 123]
                        addCharacter(b.operators[7], true)
                }

            }
            show(false) // SHOW RESULT
        }
    }

}