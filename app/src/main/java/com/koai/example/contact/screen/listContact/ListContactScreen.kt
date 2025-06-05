package com.koai.example.contact.screen.listContact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.koai.base.core.action.event.PermissionResultEvent
import com.koai.base.di.navigatorViewModel
import com.koai.base.core.ui.screens.BaseScreen
import com.koai.example.R
import com.koai.example.contact.ContactNavigator
import com.koai.example.databinding.ScreenListContactBinding

class ListContactScreen : BaseScreen<ScreenListContactBinding, ListContactRouter, ContactNavigator>(
    R.layout.screen_list_contact
) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenListContactBinding) {
        binding.button2.setOnClickListener {
            navigator.sendEvent(PermissionResultEvent(0, arrayOf(""), intArrayOf(), 0))
            router?.gotoDetailScreen()
        }
    }

    override val navigator: ContactNavigator by navigatorViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("ListContactScreen", "onViewCreated")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ListContactScreen", "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    override fun onResume() {
        super.onResume()
        Log.d("ListContactScreen", "onResume")
    }
    override fun onPause() {
        super.onPause()
        Log.d("ListContactScreen", "onPause")
    }
    override fun onStop() {
        super.onStop()
        Log.d("ListContactScreen", "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.d("ListContactScreen", "onStart")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ListContactScreen", "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ListContactScreen", "onDestroy")
    }

    override suspend fun observer() {
        viewModel.uiState.collect {
            Log.d("ListContactScreen", "uiState: $it")
        }
    }
}