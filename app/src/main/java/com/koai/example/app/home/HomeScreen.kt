package com.koai.example.app.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.koai.base.main.screens.BaseScreen
import com.koai.example.R
import com.koai.example.app.MainNavigator
import com.koai.example.app.login.HomeRouter
import com.koai.example.databinding.ScreenHomeBinding

class HomeScreen : BaseScreen<ScreenHomeBinding, HomeRouter, MainNavigator>(R.layout.screen_home) {
    private var clickCount = 0
    override fun initView(savedInstanceState: Bundle?, binding: ScreenHomeBinding) {
        binding.motionLayout.transitionToState(R.id.end)
        binding.buttonPlayNow.setOnClickListener {
            findNavController().navigate(R.id.action_homeScreen_to_playScreen)
        }
        binding.motionLayout2.setOnClickListener {
            onConstraintLayoutClick()
        }
    }

    private fun onConstraintLayoutClick() {
        when (clickCount) {
            0 -> {
                updateUI(
                    "Your score",
                    R.drawable.ic_arrow_2,
                    com.intuit.sdp.R.dimen._141sdp,
                    com.intuit.sdp.R.dimen._1sdp,
                    com.intuit.sdp.R.dimen._130sdp,
                    com.intuit.sdp.R.dimen._30sdp
                )
                binding.imageUserTutorial.alpha = 0f
                binding.textViewDiamondTutorial.alpha = 1f
                binding.icDiamondTutorial.alpha = 1f
            }

            1 -> {updateUI(
                "Your level",
                R.drawable.ic_arrow_3,
                com.intuit.sdp.R.dimen._150sdp,
                com.intuit.sdp.R.dimen._1sdp,
                com.intuit.sdp.R.dimen._215sdp,
                com.intuit.sdp.R.dimen._30sdp
            )
                binding.imageUserTutorial.alpha = 0f
                binding.textViewDiamondTutorial.alpha = 0f
                binding.icDiamondTutorial.alpha = 0f
                binding.textViewCrownTutorial.alpha = 1f
                binding.icCrownTutorial.alpha = 1f
            }
            else -> resetUI()
        }
        clickCount++

    }

    private fun updateUI(
        text: String,
        imageResId: Int,
        translationXArrow: Int,
        translationYArrow: Int,
        translationXRounded: Int,
        translationYRounded: Int
    ) {
        binding.textView4.text = text
        binding.imageArrow.setImageResource(imageResId)

        binding.imageArrow.translationX =
            resources.getDimensionPixelSize(translationXArrow).toFloat()
        binding.imageArrow.translationY =
            resources.getDimensionPixelSize(translationYArrow).toFloat()
        binding.imageRounded.translationX =
            resources.getDimensionPixelSize(translationXRounded).toFloat()
        binding.textView4.translationX = resources.getDimensionPixelSize(translationXArrow).toFloat()
        binding.logoToturial.translationX = binding.textView4.translationX
    }

    private fun resetUI() {
        binding.motionLayout.removeView(binding.motionLayout2)
    }

    override fun getModelNavigator() = ViewModelProvider(activity)[MainNavigator::class.java]

}