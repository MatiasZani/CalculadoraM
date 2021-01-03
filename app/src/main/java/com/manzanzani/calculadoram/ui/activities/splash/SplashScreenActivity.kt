package com.manzanzani.calculadoram.ui.activities.splash

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.manzanzani.calculadoram.R
import com.manzanzani.calculadoram.databinding.ActivityMainBinding
import com.manzanzani.calculadoram.databinding.ActivitySplashScreenBinding
import com.manzanzani.calculadoram.repository.abstracs.BaseActivity
import com.manzanzani.calculadoram.ui.activities.main.MainActivity
import com.manzanzani.calculadoram.ui.activities.main.MainViewModel
import com.manzanzani.calculadoram.ui.utils.DarkModeManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : BaseActivity<ActivitySplashScreenBinding, SplashScreenViewModel>(

    { ViewModelProvider(it).get(SplashScreenViewModel::class.java) }, //Get ViewModel
    { ActivitySplashScreenBinding.inflate(LayoutInflater.from(it)) }, //Inflate a View
    { it.root }, //Get View of Binding
    { b: ActivitySplashScreenBinding, _: SplashScreenViewModel, _: LifecycleOwner, a: Activity -> // Config View
        with(b){
            ico.animation = AnimationUtils.loadAnimation(root.context, R.anim.bottom)
            ico.postOnAnimation {
                DarkModeManager.getStyle(root.context)
                GlobalScope.launch {
                    delay(1700)
                    root.context.startActivity(Intent(root.context, MainActivity::class.java))
                    a.finish()
                }
            }
        }
    })