package com.koai.base.main.extension

import android.annotation.SuppressLint
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.koai.base.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ClickableViewExtensions {
    private var mLastClickTime = 0L
    private var mediaPlayer: MediaPlayer? = null
    fun initSoundEffect() {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource("https://koai-dev.github.io/assets/raw/music/click_sound.mp3")
        mediaPlayer?.prepareAsync()
    }

    fun releaseSoundEffect() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    @SuppressLint("ClickableViewAccessibility")
    fun View.setClickableWithScale(
        enableSoundEffect: Boolean = true,
        delayTimeDoubleClick: Int = 200,
        onClick: () -> Unit
    ) {
        setOnClickListener {
            if (SystemClock.elapsedRealtime() - mLastClickTime < delayTimeDoubleClick) {
                return@setOnClickListener
            }
            mLastClickTime = SystemClock.elapsedRealtime()
            onClick.invoke()
            if (enableSoundEffect) {
                mediaPlayer?.start()
            }
        }
        setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        view.scaleX = 1.1f
                        view.scaleY = 1.1f
                        delay(100)
                        view.scaleX = 1f
                        view.scaleY = 1f
                    }
                }
            }
            false
        }
    }

    fun ImageView.loadImage(
        source: Any,
        onSuccess: (() -> Unit)? = null,
        onFail: (() -> Unit)? = null,
    ) {
        try {
            this.load(source) {
                listener(
                    onStart = { request ->
                        crossfade(true)
                        decoderFactory()
                        placeholder(
                            CircularProgressDrawable(context).apply {
                                strokeWidth = 5f
                                centerRadius = 30f
                                start()
                            },
                        )
                        transformations(CircleCropTransformation())
                    },
                    onSuccess = { request, result ->
                        this@loadImage.visible()
                        if (onSuccess != null) {
                            onSuccess()
                        }
                    },
                    onError = { request, result ->
                        this@loadImage.gone()
                        if (onFail != null) {
                            onFail()
                        }
                    },
                )
            }
        } catch (e: Exception) {
            this@loadImage.gone()
            if (onFail != null) {
                onFail()
            }
        }
    }
}
