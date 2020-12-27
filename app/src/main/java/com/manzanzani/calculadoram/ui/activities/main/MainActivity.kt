package com.manzanzani.calculadoram.ui.activities.main

import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.manzanzani.calculadoram.databinding.ActivityMainBinding
import com.manzanzani.calculadoram.repository.abstracs.BaseActivity
import com.manzanzani.calculadoram.repository.classes.ButtonAndCardView
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(

        { ViewModelProvider(it).get(MainViewModel::class.java) }, //Get ViewModel
        { ActivityMainBinding.inflate(LayoutInflater.from(it)) }, //Inflate a View
        { it.root }, //Get View of Binding
        { b: ActivityMainBinding, v: MainViewModel, lfo: LifecycleOwner -> // Config View

            with(b) {

                v.init(b.root.context)

                v.viewModelScope.launch {

                    for ((lambdaIndex, groupdOfView) in listOf(
                        ButtonAndCardView( // Numbers
                            listOf(
                                buttonZero,
                                buttonOne,
                                buttonTwo,
                                buttonTrhee,
                                buttonFour,
                                buttonFive,
                                buttonSix,
                                buttonSeven,
                                buttonEight,
                                buttonNine
                            ),
                            listOf(
                                cardViewZero,
                                cardViewOne,
                                cardViewTwo,
                                cardViewThree,
                                cardViewFour,
                                cardViewFive,
                                cardViewSix,
                                cardViewSeven,
                                cardViewEight,
                                cardViewNine
                            )
                        ),
                        ButtonAndCardView( // Operators
                            listOf(
                                buttonPlus,
                                buttonMinus,
                                buttonTimes,
                                buttonDiv,
                                buttonPercent
                            ),
                            listOf(
                                cardViewPlus,
                                cardViewMinus,
                                cardViewTimes,
                                cardViewDiv,
                                cardViewPercent


                            )
                        ),
                        ButtonAndCardView( // Especial
                            listOf(
                                buttonParenthesisStart,
                                buttonParenthesisEnd,
                                buttonDot,
                                buttonFuntion,
                                    cardViewEquals
                            ),
                            listOf(
                                cardViewParenthesisStart,
                                cardViewParenthesisEnd,
                                cardViewDot,
                                cardViewFuntion,
                                buttonEquals
                            )
                        )

                    ).withIndex()){
                        for (group in listOf(groupdOfView.buttons, groupdOfView.cardView))
                            for ((viewIndex, view) in group.withIndex())
                                view.setOnClickListener {
                                    v.screen.addAny(viewIndex, lambdaIndex)
                                }
                    }
                }

                v.screen.screen.observe(lfo){
                    txtScreen.text = it
                    v.screen.restart(b.buttonRestart)
                }

                v.screen.story.observe(lfo){
                    txtStory.text = it
                    v.screen.restart(b.buttonRestart)
                }

                v.screen.result.observe(lfo){

                }
            }


        }

)