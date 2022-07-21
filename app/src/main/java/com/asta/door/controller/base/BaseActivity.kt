package com.asta.door.controller.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding


abstract class BaseActivity<VB : ViewBinding>(
    private val bindingInflater: (inflater: LayoutInflater) -> VB
) : AppCompatActivity(), View.OnClickListener {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding as VB

    protected open fun initializeToolBar() {}

    protected abstract fun initializeControls()

    protected open fun attachListeners() {}

    protected open fun observeViewModel() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = bindingInflater.invoke(layoutInflater)
        if (_binding == null)
            throw IllegalArgumentException("Binding cannot be null")
        setContentView(_binding!!.root)

        initializeToolBar()
        initializeControls()
        attachListeners()
        observeViewModel()
    }

    override fun onClick(view: View) {

    }

    fun startSpecificActivity(otherActivityClass: Class<*>?) {
        val intent = Intent(this@BaseActivity, otherActivityClass)
        startActivity(intent)
    }
}