package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist

class Calculator(private val b: BasicDataToExist){

    var operation = ArrayList<String>().apply { add(b.context.getString(R.string.empty)) }

    val calculate = { getResult(operation).toFloat() }

    private val findEndOfParenthesis = { operationList: ArrayList<String>, startIndex: Int ->
        with(b.context){
            var index = 0

            var startP = 0
            var endP = 0

            for (i in startIndex..operationList.lastIndex){
                when(operationList[i]){
                    getString(R.string.parenthesis_start) -> startP++

                    getString(R.string.parenthesis_end) ->{
                        endP++
                        if (startP == endP){
                            index = i
                            break
                        }
                    }

                    else ->
                        if (i == operationList.lastIndex){
                            repeat(startP - endP){ operationList.add(getString(R.string.parenthesis_end)) }
                            index = operationList.lastIndex
                            break
                        }
                }
            }

            index
        }
    }

    private val calculateParenthesis = { operationList: ArrayList<String>, startIndex: Int ->
        with(b){
            val finalIndex = findEndOfParenthesis(operationList, startIndex)

            val notPosibleCharacters = ArrayList<String>().apply {
                addAll(operators)
                add(context.getString(R.string.parenthesis_start))
            }

            var counter = 0

            if (finalIndex < operationList.lastIndex)
                if (operationList[finalIndex + 1] !in notPosibleCharacters && finalIndex + 1 != operationList.size)
                    operationList.add(finalIndex + 1 , operators[2])

            if (startIndex > 0)
                if (operationList[startIndex - 1] !in notPosibleCharacters){
                    operationList.add(startIndex , operators[2])
                    counter++
                }

            operationList.add(startIndex,
                    getResult(
                            ArrayList<String>().apply {
                                for (i in startIndex..finalIndex + counter) add(operationList[i])
                                Log.i("OUT_P_1", this.toString())
                                removeAt(0)
                                removeAt(lastIndex)
                                Log.i("OUT_P", this.toString())
                            }
                    )
            )

            Log.i("OPERATION_NOT_MOFIDIF", operationList.toString())
            repeat(finalIndex + 1 - startIndex){ operationList.removeAt(startIndex + counter + 1) }
            Log.i("OPERATION_MOFIDIF", operationList.toString())
        }
    }

    private fun getResult(operationList: ArrayList<String>): String {
        with(b){


            operationList.removeAll { it == (context.getString(R.string.empty)) }

            val operate = {
                operationList[0] = operatorsFunctions[operators.indexOf(operationList[1])](operationList[0].toFloat(), operationList[2].toFloat()).toString()
                repeat(2){ operationList.removeAt(1) }
                Log.i("OPERATE", operationList.toString())
            }

            while (operationList.size != 1){
                Log.i("WHILE_LOOP", operationList.toString())
                if (operationList[0] == context.getString(R.string.parenthesis_start)) calculateParenthesis(operationList, 0)
                else{
                    when(operationList[1]){

                        in operators ->{
                            if (operationList.size >= 2){
                                if (operationList[2] == b.context.getString(R.string.parenthesis_start)){
                                    calculateParenthesis(operationList, 2)
                                    operate()
                                }
                                else operate()
                            }
                            else operate()
                        }

                        context.getString(R.string.parenthesis_start) -> {
                            calculateParenthesis(operationList, 1)
                            operate()
                        }

                    }
                }
            }

            Log.i("GET_RESULT_RESULT", operationList[0])
            return operationList[0]
        }
    }
}