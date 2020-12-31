package com.manzanzani.calculadoram.ui.activities.main

import android.view.LayoutInflater
import androidx.cardview.widget.CardView
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
                                buttonTimes,
                                buttonDiv,
                                buttonPercent,
                                buttonPow,
                                buttonFraction
                            ),
                            listOf(
                                cardViewPlus,
                                cardViewTimes,
                                cardViewDiv,
                                cardViewPercent,
                                cardViewPow,
                                cardViewFraction


                            )
                        ),
                        ButtonAndCardView( // Especial
                            listOf(
                                buttonParenthesisStart,
                                buttonParenthesisEnd,
                                buttonDot,
                                buttonMinus,
                                buttonRoot,
                                buttonLog
                            ),
                            listOf(
                                cardViewParenthesisStart,
                                cardViewParenthesisEnd,
                                cardViewDot,
                                cardViewMinus,
                                cardViewRoot,
                                cardViewLog
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

                val constantButtons = ButtonAndCardView(listOf(
                                buttonEquals,
                                buttonFunction
                        ), listOf(
                                cardViewEquals,
                                cardViewFunction
                        ))

                for (i in 0..1) for (x in listOf(constantButtons.buttons, constantButtons.cardView))
                    x[i].setOnClickListener {
                        listOf(v.screen.calculate, v.screen.function, v.screen.removeCharacter)[i]()
                    }

                for ((j, i) in listOf(buttonRemove).withIndex())
                    i.setOnClickListener {
                        listOf(v.screen.removeCharacter)[j]()
                    }

                for((j, i) in listOf(v.screen.screen, v.screen.story).withIndex())
                    i.observe(lfo){
                        listOf(txtScreen, txtStory)[j].text = it
                        v.screen.configFunction(buttonFunction)
                    }
            }
        }
)