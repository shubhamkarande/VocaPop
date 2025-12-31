package com.vocapop.android.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class TtsManager(context: Context) {
    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                tts?.language = Locale.FRENCH // Default or dynamic
            }
        }
    }

    fun speak(text: String, languageCode: String? = null) {
        if (!isInitialized) return
        
        if (languageCode != null) {
            val locale = Locale(languageCode)
            tts?.language = locale
        }
        
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
    }
}
