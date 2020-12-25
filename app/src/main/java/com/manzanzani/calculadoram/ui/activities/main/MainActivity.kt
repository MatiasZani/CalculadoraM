package com.manzanzani.calculadoram.ui.activities.main

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.manzanzani.calculadoram.databinding.ActivityMainBinding
import com.manzanzani.calculadoram.repository.abstracs.BaseActivity

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(

        { ViewModelProvider(it).get(MainViewModel::class.java) },
        { ActivityMainBinding.inflate(LayoutInflater.from(it)) },
        { it.root },
        { b: ActivityMainBinding, v: MainViewModel ->



        }

)