package com.manzanzani.calculadoram.repository.abstracs

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelStoreOwner

abstract class BaseActivity<T, V>(
    private val viewModelProvider: (ViewModelStoreOwner) -> V,
    private val bindingFragment: (Context) -> T,
    private val bindingFragmentRoot: (T) -> View,
    private val configView: (T, V) -> Unit

) : AppCompatActivity() {

    private var binding: T? = null
    private var viewModel: V? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFragment(this)
        viewModel = viewModelProvider(this)
        setContentView(bindingFragmentRoot(binding!!))
    }

    override fun onResume() {
        super.onResume()
        configView(binding!!, viewModel!!)
    }

}