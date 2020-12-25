package com.manzanzani.calculadoram.repository.abstracs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner

abstract class BaseActivity<T, V>(

    viewModelProvider: (ViewModelStoreOwner) -> V,
    bindingFragment: (Context) -> T,
    private val bindingFragmentRoot: (T) -> View,
    private val configView: (T, V) -> Unit

) : AppCompatActivity() {

    private val binding = bindingFragment(this)
    private val viewModel = viewModelProvider(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(bindingFragmentRoot(binding))
    }

    override fun onResume() {
        super.onResume()
        configView(binding, viewModel)
    }

}