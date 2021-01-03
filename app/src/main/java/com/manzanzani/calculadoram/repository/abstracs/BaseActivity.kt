package com.manzanzani.calculadoram.repository.abstracs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner

abstract class BaseActivity<T, V>(
    private val viewModelProvider: (ViewModelStoreOwner) -> V,
    private val bindingFragment: (Context) -> T,
    private val bindingFragmentRoot: (T) -> View,
    private val configView: (T, V, LifecycleOwner, Activity) -> Unit

) : AppCompatActivity() {

    private var binding: T? = null
    private var viewModel: V? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFragment(this)
        viewModel = viewModelProvider(this)
        configView(binding!!, viewModel!!, this, this)
        setContentView(bindingFragmentRoot(binding!!))
    }

}