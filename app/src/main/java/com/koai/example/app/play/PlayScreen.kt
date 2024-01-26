package com.koai.example.app.play

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import com.koai.base.main.screens.BaseScreen
import com.koai.example.R
import com.koai.example.app.MainNavigator
import com.koai.example.app.TextViewUtils
import com.koai.example.app.login.HomeRouter
import com.koai.example.databinding.ScreenPlayBinding
import java.util.Random


class PlayScreen : BaseScreen<ScreenPlayBinding, HomeRouter, MainNavigator>(R.layout.screen_play) {
    private var presCounter = 0
    private var maxPresCounter = 0
    private var keys: Array<String> = emptyArray()
    private lateinit var textAnswer: String
    override fun initView(savedInstanceState: Bundle?, binding: ScreenPlayBinding) {
        textAnswer = "HAPPY"
        maxPresCounter = textAnswer.length
        keys = textAnswer.toCharArray().map { it.toString() }.toTypedArray()
        keys = shuffleArray(keys)
        binding.textViewQuestion.text = keys.joinToString("/")
        for (key in keys) {
            addView(binding.layoutQuestion, key, binding.editText, binding.layoutAnswer)
        }
    }
    @SuppressLint("SetTextI18n")
    private fun addView(
        viewParent: GridLayout,
        text: String,
        editText: EditText,
        viewParent2: GridLayout
    ) {
        val typeface = ResourcesCompat.getFont(requireContext(), R.font.super_corn)
        val textViewQuestion = TextViewUtils.createTextView(
            requireContext(),
            text,
            32f,
            typeface!!,
            ResourcesCompat.getDrawable(resources, R.drawable.bg_text, null)!!,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )

        val textViewAnswer = TextViewUtils.createTextView(
            requireContext(),
            "",
            32f,
            typeface,
            ResourcesCompat.getDrawable(resources, R.drawable.bg_empty_text, null)!!,
            ResourcesCompat.getColor(resources, R.color.white, null)
        )

        textViewQuestion.setOnClickListener {
            if (presCounter < maxPresCounter) {
                if (presCounter == 0)
                    editText.setText("")
                editText.setText(editText.text.toString() + text)
                textViewAnswer.text = text
                textViewAnswer.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_text_answer, null)
                textViewAnswer.isClickable = true
                textViewAnswer.isFocusable = true
                textViewQuestion.text = null
                textViewQuestion.background =
                    ResourcesCompat.getDrawable(resources, R.drawable.bg_text_empty, null)
                textViewQuestion.isClickable = false
                textViewQuestion.isFocusable = false
                presCounter++
                val newPosition = (presCounter - 1) % viewParent2.columnCount
                viewParent2.removeView(textViewAnswer)
                viewParent2.addView(textViewAnswer, newPosition)
                if (presCounter == maxPresCounter)
                    checkAnswer()
                }
        }
        viewParent.addView(textViewQuestion)
        viewParent2.addView(textViewAnswer)
    }

    private fun checkAnswer() {
        presCounter = 0
        if (binding.editText.text.toString() == textAnswer) {
            Toast.makeText(requireContext(), "Correct", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "Wrong", Toast.LENGTH_SHORT).show()
            binding.editText.setText("")
            keys = shuffleArray(keys)
            binding.layoutQuestion.removeAllViews()
            binding.layoutAnswer.removeAllViews()
            for (key in keys) {
                addView(binding.layoutQuestion, key, binding.editText, binding.layoutAnswer)
            }
        }
    }

    private fun shuffleArray(ar: Array<String>): Array<String> {
        val rnd = Random()
        return ar.toList().shuffled(rnd).toTypedArray()
    }

    override fun getModelNavigator() = ViewModelProvider(activity)[MainNavigator::class.java]

}