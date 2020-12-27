package com.manzanzani.calculadoram.repository.utils

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.repository.classes.BasicDataToExist
import com.manzanzani.calculadoram.repository.classes.ParenthesisIndex
import javax.security.auth.login.LoginException

class Calculator(private val basicData: BasicDataToExist){

    var operation = ArrayList<String>().apply { add(basicData.context.getString(R.string.empty)) }

    val calculate = { getResult(operation).toFloat() }

    private val findEndOfParenthesis = { operationList: ArrayList<String>, startIndex: Int ->
        with(basicData.context){

            var index = 0

            var startP = 0
            var endP = 0

            for (i in startIndex until operationList.size)
                when(operationList[i]){
                    getString(R.string.parenthesis_start) -> startP++

                    getString(R.string.parenthesis_end) ->{
                        endP++
                        if (startP == endP){
                            index = i
                            break
                        }
                    }

                }

            index
        }
    }

    private val calculateParenthesis = { operationList: ArrayList<String>, startIndex: Int ->
        with(basicData){
            val finalIndex = findEndOfParenthesis(operationList, startIndex)

            var counter = 0

            if (finalIndex < operationList.size)
                if (operationList[finalIndex + 1] !in operators)
                    operationList.add(finalIndex + 1 , operators[2])

            if (startIndex > 0)
                if (operationList[startIndex - 1] !in operators){
                    operationList.add(startIndex , operators[2])
                    counter++
                }

            operationList.add(startIndex + counter,
                    getResult(
                            ArrayList<String>().apply {
                                for (i in 2..finalIndex + counter) add(operationList[i])
                                remove(context.getString(R.string.parenthesis_start))
                                remove(context.getString(R.string.parenthesis_end))
                            }
                    )
            )

            repeat(finalIndex + counter - 1){ operationList.removeAt(3) }
        }
    }

    private fun getResult(operationList: ArrayList<String>): String {
        with(basicData){
            val operate = {
                operationList[0] = operatorsFunctions[operators.indexOf(operationList[1])](operationList[0].toFloat(), operationList[2].toFloat()).toString()
                repeat(2){ operationList.removeAt(1) }
            }

            Log.i("OPERATIONLIST", operationList.toString())

            while (operationList.size != 1){
                if (operationList[0] == context.getString(R.string.parenthesis_start)) calculateParenthesis(operationList, 0)
                else{
                    when(operationList[1]){

                        in operators -> operate()

                        context.getString(R.string.parenthesis_start) -> {
                            calculateParenthesis(operationList, 1)
                            operate()
                        }

                    }
                }
            }

            return operationList[0]
        }
    }
}