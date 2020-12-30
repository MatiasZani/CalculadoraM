package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist

class Calculator(private val b: BasicDataToExist){

    var operation = ArrayList<String>().apply { add(b.context.getString(R.string.empty)) }

    val calculate = { parenthesisNotEnded: Int ->
        operation.remove(b.context.getString(R.string.empty))
        Log.i("REMOVE_EMPTY", operation.toString())
        repeat(parenthesisNotEnded){ operation.add(b.context.getString(R.string.parenthesis_end)) }
        Log.i("COMPLETE_PARENTHESIS", operation.toString())
        completeAllOperators()
        Log.i("COMPLETE_OPERATORS", operation.toString())
        removeNotNecesaryParenthesis()
        Log.i("REMOVE_PARENTHESIS", operation.toString())
        while (operation.size != 1){
            operate(operation, listOf(b.operators[1], b.operators[2], b.operators[3]))
            operate(operation, listOf(b.operators[0], b.operators[4]))
        }
        operation[0].toFloat()
    }

    private val operateScientific = { operationList: ArrayList<String>, firstIndex: Int ->
        with(b){
            operatorsFunctions[operators.indexOf(operationList[firstIndex + 1])](operationList[firstIndex].toFloat(), operationList[firstIndex + 2].toFloat()).toString()
        }
    }

    private val completeAllOperators = {
        with(b){
            var parenthesis = 0

            val checkP = { value: String, index: Int ->
                if (index > 0 && index < operation.lastIndex)
                    value == context.getString(R.string.parenthesis_start) &&
                    operation[index - 1] !in operators &&
                    operation[index - 1] != context.resources.getStringArray(R.array.parenthesis)[0] ||
                    value == context.getString(R.string.parenthesis_end) &&
                    operation[index + 1] !in operators &&
                    operation[index + 1] != context.resources.getStringArray(R.array.parenthesis)[1] &&
                    if (index + 1 <= operation.lastIndex) operation[index + 1] != context.getString(R.string.empty) else true
                else false
            }

            for ((j, i) in operation.withIndex()) if (checkP(i, j)) parenthesis++

            while (parenthesis != 0){
                for ((j, i) in operation.withIndex()) if (checkP(i, j)){
                    if (i == context.getString(R.string.parenthesis_start)) operation.add(j, operators[1])
                    else operation.add(j + 1, operators[1])

                    break
                }
                parenthesis--
            }
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

    private fun operate(operationList: ArrayList<String>, operatorsList: List<String>){
        with(b){

            operationList.removeAll { it == context.getString(R.string.empty) }

            var notSolvedOperations = 0

            for ((j, i) in operationList.withIndex())
                if (i in operatorsList && operation[j + 1] != context.resources.getStringArray(R.array.parenthesis)[0] && operation[j + 1] != context.resources.getStringArray(R.array.parenthesis)[1])
                    notSolvedOperations++

            Log.i("OPERATORS_LIST", operatorsList.toString())
            Log.i("NOTSOLVED", notSolvedOperations.toString())

            while (notSolvedOperations != 0){
                Log.i("OPERATION", operation.toString())
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
                Log.i("OPERATION_1", operation.toString())
            }
        }
    }

}