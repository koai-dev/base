package com.koai.example.contact.screen.detailContact

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.koai.base.core.ui.extension.ClickableViewExtensions.setClickableWithScale
import com.koai.base.core.ui.screens.BaseScreen
import com.koai.base.di.navigatorViewModel
import com.koai.example.R
import com.koai.example.contact.ContactNavigator
import com.koai.example.databinding.ScreenDetailContactBinding

class DetailContactScreen :
    BaseScreen<ScreenDetailContactBinding, DetailContactRouter, ContactNavigator>(
        R.layout.screen_detail_contact
    ) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenDetailContactBinding) {

        binding.button2.setOnClickListener {
            router?.goToListContact()
        }

        binding.img.setClickableWithScale() {
        }
    }

    override val navigator: ContactNavigator by navigatorViewModel()

    override fun onPermissionResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        Toast.makeText(requireContext(), "onPermissionResult", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("DetailContactScreen", "onViewCreated")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("DetailContactScreen", "onDestroy")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("DetailContactScreen", "onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("DetailContactScreen", "onDestroyView")
    }

    override fun onStart() {
        super.onStart()
        Log.d("DetailContactScreen", "onStart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("DetailContactScreen", "onStop")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DetailContactScreen", "onCreate")
    }

    override fun onResume() {
        super.onResume()
        Log.d("DetailContactScreen", "onResume")

    }
}