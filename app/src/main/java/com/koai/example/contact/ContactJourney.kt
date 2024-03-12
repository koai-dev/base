package com.koai.example.contact

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.screens.BaseJourney
import com.koai.example.R
import com.koai.example.databinding.JourneyContactBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContactJourney : BaseJourney<JourneyContactBinding, ContactRouter, ContactNavigator>(R.layout.journey_contact) {
    override fun initView(savedInstanceState: Bundle?, binding: JourneyContactBinding) {

    }

    override val navigator: ContactNavigator by viewModel()
}