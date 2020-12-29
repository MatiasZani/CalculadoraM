package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist

class Calculator(private val b: BasicDataToExist){

    var operation = ArrayList<String>().apply { add(b.context.getString(R.string.empty)) }

    val calculate = { parenthesisNotEnded: Int ->
        repeat(parenthesisNotEnded){ operation.add(b.context.getString(R.string.parenthesis_end)) }
        removeNotNecesaryParenthesis()
        while (operation.size != 1){
            removeTimesAndDiv(operation, listOf(b.operators[2], b.operators[3], b.operators[4]))
            removeTimesAndDiv(operation, listOf(b.operators[0], b.operators[1]))
        }
        operation[0].toFloat()
    }

    private val operateScientific = { operationList: ArrayList<String>, firstIndex: Int ->
        with(b){
            operatorsFunctions[operators.indexOf(operationList[firstIndex + 1])](operationList[firstIndex].toFloat(), operationList[firstIndex + 2].toFloat()).toString()
        }
    }

    private val removeNotNecesaryParenthesis = {

        var parenthesis = 0

        val checkP = { value: String, index: Int -> value == b.context.getString(R.string.parenthesis_start) && operation[index + 2] == b.context.getString(R.string.parenthesis_end) }

        for ((j, i) in operation.withIndex()) if (checkP(i, j)) parenthesis++

        while (parenthesis != 0){
            var pIndex = 0
            parenthesis--
            for ((j, i) in operation.withIndex()) if (checkP(i, j)){
                pIndex = j
                break
            }

            val value = operation[pIndex + 1]

            repeat(3){ operation.removeAt(pIndex) }

            operation.add(pIndex, value)
        }
    }

    private fun removeTimesAndDiv(operationList: ArrayList<String>, operatorsList: List<String>){
        with(b){

            operationList.removeAll { it == context.getString(R.string.empty) }

            var notSolvedOperations = 0

            for (i in operationList) if (i in operatorsList) notSolvedOperations++

            while (notSolvedOperations != 0){
                var operationIndex = 0

                for ((j, i) in operationList.withIndex())
                    if (i in operatorsList)
                        if (operationList[j - 1] != context.getString(R.string.parenthesis_end) && operationList[j + 1] != context.getString(R.string.parenthesis_start)){
                            operationIndex = j - 1
                            break
                        }

                notSolvedOperations--

                val result = operateScientific(operationList, operationIndex)

                var pCounter = 0

                if (operationIndex - 1 >= 0 && operationIndex + 3 <= operationList.size)
                    if (operationList[operationIndex - 1] == context.getString(R.string.parenthesis_start) && operationList[operationIndex + 3] == context.getString(R.string.parenthesis_end)){
                        operationList.removeAt(operationIndex + 3)
                        operationList.removeAt(operationIndex - 1)
                        pCounter += 1
                    }

                repeat(3){ operationList.removeAt(operationIndex - pCounter) }

                operationList.add(operationIndex - pCounter, result)
            }
        }
    }

}