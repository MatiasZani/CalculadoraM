package com.manzanzani.calculadoram.ui.activities.main

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.manzanzani.calculadoram.databinding.ActivityMainBinding
import com.manzanzani.calculadoram.repository.abstracs.BaseActivity
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(

        { ViewModelProvider(it).get(MainViewModel::class.java) }, //Get ViewModel
        { ActivityMainBinding.inflate(LayoutInflater.from(it)) }, //Inflate a View
        { it.root }, //Get View of Binding
        { b: ActivityMainBinding, v: MainViewModel, lfo: LifecycleOwner -> // Config View

            with(b) {

                val btnsNumbers = listOf(
                        buttonZero, // 0
                        buttonOne, // 1
                        buttonTwo, // 2
                        buttonTrhee, // 3
                        buttonFour, // 4
                        buttonFive, // 5
                        buttonSix,  // 6
                        buttonSeven, // 7
                        buttonEight, // 8
                        buttonNine // 9
                )

                val btnOperators = listOf(
                        buttonPlus, // +
                        buttonMinus, // -
                        buttonTimes, // *
                        buttonDiv, // /
                        buttonEquals // =
                )

                val especial = listOf(
                        buttonParenthesisStart, // (
                        buttonParenthesisEnd, // )
                        buttonPercent // %
                )


                v.viewModelScope.launch {
                    for ((j, i) in btnsNumbers.withIndex()) i.setOnClickListener { v.addNumber(j) }
                    for ((j, i) in btnOperators.withIndex()) i.setOnClickListener { v.addOperator(j) }
                    for ((j, i) in especial.withIndex()) i.setOnClickListener { v.addEspecial(j) }
                }

                v.screen.observe(lfo){ screen.text = it }

                v.story.observe(lfo){ story.text = it }

                v.result.observe(lfo){

                }
            }


        }

)