package com.koai.example.contact.screen.listContact

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.screens.BaseScreen
import com.koai.example.R
import com.koai.example.contact.ContactNavigator
import com.koai.example.databinding.ScreenListContactBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListContactScreen : BaseScreen<ScreenListContactBinding, ListContactRouter, ContactNavigator>(
    R.layout.screen_list_contact) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenListContactBinding) {
        binding.button2.setOnClickListener {
            router?.gotoDetailScreen()
        }
    }

    override val navigator: ContactNavigator by viewModel()

}