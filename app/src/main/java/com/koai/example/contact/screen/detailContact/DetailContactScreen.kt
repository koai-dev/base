package com.koai.example.contact.screen.detailContact

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.extension.ClickableViewExtensions.setClickableWithScale
import com.koai.base.main.screens.BaseScreen
import com.koai.example.R
import com.koai.example.contact.ContactNavigator
import com.koai.example.databinding.ScreenDetailContactBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailContactScreen : BaseScreen<ScreenDetailContactBinding, DetailContactRouter, ContactNavigator>(
    R.layout.screen_detail_contact) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenDetailContactBinding) {
        binding.button2.setOnClickListener {
            router?.goToListContact()
        }

        binding.img.setClickableWithScale {

        }
    }

    override val navigator: ContactNavigator by viewModel()

}