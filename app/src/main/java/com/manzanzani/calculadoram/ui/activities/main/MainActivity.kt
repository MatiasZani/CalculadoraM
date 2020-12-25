package com.manzanzani.calculadoram.ui.activities.main

import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import com.manzanzani.calculadoram.databinding.ActivityMainBinding
import com.manzanzani.calculadoram.repository.abstracs.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(

        { ViewModelProvider(it).get(MainViewModel::class.java) }, //Get ViewModel
        { ActivityMainBinding.inflate(LayoutInflater.from(it)) }, //Inflate a View
        { it.root }, //Get View of Binding
        { b: ActivityMainBinding, v: MainViewModel -> // Config View

            with(b){
                
            }

        }

)