package com.koai.example.app.login

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.koai.base.main.screens.BaseScreen
import com.koai.base.utils.LoadImageUtil.loadImage
import com.koai.base.utils.ClickableViewExtensions.setClickableWithScale
import com.koai.example.R
import com.koai.example.app.MainNavigator
import com.koai.example.databinding.ScreenLoginBinding
class LoginScreen :
    BaseScreen<ScreenLoginBinding, HomeRouter, MainNavigator>(R.layout.screen_login) {
    override fun initView(savedInstanceState: Bundle?, binding: ScreenLoginBinding) {
        setupViews()
        actionViews()
    }

    private fun actionViews() {
        binding.loginImageView.setClickableWithScale {
            binding.loginImageView.loadImage(R.drawable.logo_splash)
        }
        binding.buttonNo.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_homeScreen)
        }
    }

    private fun setupViews() {
        val spannableStringBuilder = SpannableStringBuilder(binding.textView.text.toString())
        val termsClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(requireContext(), "Terms", Toast.LENGTH_SHORT).show()
            }
            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableStringBuilder.setSpan(
            termsClickableSpan,
            binding.textView.text.toString().indexOf("Terms"),
            binding.textView.text.toString().indexOf("Terms") + "Terms".length,
            0
        )
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.black_tran)),
            binding.textView.text.toString().indexOf("Terms"),
            binding.textView.text.toString().indexOf("Terms") + "Terms".length,
            0
        )

        val privacyClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(requireContext(), "Privacy POLICY", Toast.LENGTH_SHORT).show()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        spannableStringBuilder.setSpan(
            privacyClickableSpan,
            binding.textView.text.toString().indexOf("Privacy POLICY"),
            binding.textView.text.toString().length,
            0
        )
        spannableStringBuilder.setSpan(
            ForegroundColorSpan(resources.getColor(R.color.black_tran)),
            binding.textView.text.toString().indexOf("Privacy POLICY"),
            binding.textView.text.toString().length,
            0
        )
        binding.textView.text = spannableStringBuilder
        binding.textView.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun getModelNavigator() = ViewModelProvider(activity)[MainNavigator::class.java]

}