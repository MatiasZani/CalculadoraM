package com.manzanzani.calculadoram.repository.utils

import android.util.Log
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist

class Calculator(private val b: BasicDataToExist) {

    val calculate = { operation: ArrayList<String>, parenthesisNotEnded: Int ->
        with(b){
            operation.removeAll { it == context.getString(R.string.empty) }
            Log.i("REMOVE_EMPTY", operation.toString())
            repeat(parenthesisNotEnded){ operation.add(b.context.getString(R.string.parenthesis_end)) }

            Log.i("COMPLETE_PARENTHESIS", operation.toString())

            completeAllOperators(operation)

            Log.i("COMPLETE_OPERATORS", operation.toString())

            removeNotNecesaryParenthesis(operation)

            Log.i("REMOVE_PARENTHESIS", operation.toString())

            while (operation.size != 1){
                Log.i("OPERATION", operation.toString())
                oneValueOperate(operation, listOf(b.operators[6], b.operators[7]))
                operate(operation, listOf(b.operators[1], b.operators[2], b.operators[3], b.operators[4], b.operators[5]))
                operate(operation, listOf(b.operators[0], b.operators[8]))
            }
            operation[0].toFloat()
        }
    }

    private val operateScientific = { operationList: ArrayList<String>, firstIndex: Int ->
        with(b){
            operatorsFunctionsWithTwoNumbers[if (operationList[firstIndex + 1] == operators[8]) 6 else operators.indexOf(operationList[firstIndex + 1])](operationList[firstIndex].toFloat(), operationList[firstIndex + 2].toFloat()).toString()
        }
    }

    private val completeAllOperators = { operation: ArrayList<String> ->
        with(b){
            var parenthesis = 0

            val checkP = { value: String, index: Int ->
                if (index > 0 && index < operation.lastIndex)
                    value == context.resources.getStringArray(R.array.parenthesis)[0] &&
                    operation[index - 1] !in operators &&
                    operation[index - 1] != context.resources.getStringArray(R.array.parenthesis)[0] ||
                    value == context.resources.getStringArray(R.array.parenthesis)[1] &&
                    operation[index + 1] !in operators &&
                    operation[index + 1] != context.resources.getStringArray(R.array.parenthesis)[1] &&
                    (if (index + 1 <= operation.lastIndex) operation[index + 1] != context.getString(R.string.empty) else true) ||
                    value == operators[6] &&
                    operation[index - 1] !in operators ||
                    value == operators[7] &&
                    operation[index - 1] !in operators
                else false
            }

            for ((j, i) in operation.withIndex()) if (checkP(i, j)) parenthesis++

            while (parenthesis != 0){
                for ((j, i) in operation.withIndex()) if (checkP(i, j)){
                    if (i == context.getString(R.string.parenthesis_start)) operation.add(j, operators[1])
                    else if (i == context.getString(R.string.parenthesis_end))operation.add(j + 1, operators[1])
                    else if (i ==  operators[6] || i == operators[7]) operation.add(j, operators[1])

                    break
                }
                parenthesis--
            }
        }
    }

    private val removeNotNecesaryParenthesis = { operation: ArrayList<String> ->

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

    private fun oneValueOperate(operationList: ArrayList<String>, operatorsList: List<String>){
        with(b){

            var notSolvedOperations = 0

            val checkP = { i: String, j: Int ->i in operatorsList && if (j + 1 in 0..operationList.lastIndex) operationList[j + 1] !in context.resources.getStringArray(R.array.parenthesis) else false }
            
            for ((j, i) in operationList.withIndex())
                if (checkP(i, j))
                    notSolvedOperations++

            while (notSolvedOperations != 0){

                var operationIndex = 0

                notSolvedOperations--

                for ((j, i) in operationList.withIndex())
                    if (checkP(i, j))
                        operationIndex = j

                val result = operatorsFunctionsWithOneNumber[operatorsList.indexOf(operationList[operationIndex])](operationList[operationIndex + 1].toFloat())

                repeat(2){ operationList.removeAt(operationIndex) }

                operationList.add(operationIndex, result.toString())
            }
        }
    }

    private fun operate(operationList: ArrayList<String>, operatorsList: List<String>){
        with(b){

            var notSolvedOperations = 0

            for ((j, i) in operationList.withIndex())
                if (i in operatorsList && operationList[j - 1] != context.getString(R.string.parenthesis_end) && operationList[j + 1] != context.getString(R.string.parenthesis_start))
                    notSolvedOperations++

            while (notSolvedOperations != 0){
                var operationIndex = 0

                for ((j, i) in operationList.withIndex())
                    if (i in operatorsList && operationList[j - 1] != context.getString(R.string.parenthesis_end) && operationList[j + 1] != context.getString(R.string.parenthesis_start)) {
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
                Log.i("OPERATION_1", operationList.toString())
            }
        }
    }

}