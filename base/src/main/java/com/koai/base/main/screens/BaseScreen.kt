package com.koai.base.main.screens

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.koai.base.main.BaseActivity
import com.koai.base.main.action.navigator.BaseNavigator
import com.koai.base.main.action.router.BaseRouter

/**
 * Base ui screen
 */
abstract class BaseScreen<T: ViewBinding, Router: BaseRouter, F: BaseNavigator>(private val layoutId: Int = 0) : Fragment() {
    lateinit var binding: T
    lateinit var activity: BaseActivity<*,*,*>
    protected var navigator: F? = null
    protected var router: Router? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, layoutId, container, false)
        activity = requireActivity() as BaseActivity<*,*,*>
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = getModelNavigator()
        try {
            router = navigator?.router as Router?
        }catch (e: Exception){
            Log.e("Cast router error: ", e.message.toString())
        }
        initView(savedInstanceState, binding)
    }

    abstract fun initView(savedInstanceState: Bundle?, binding: T)
    abstract fun getModelNavigator(): F?
}