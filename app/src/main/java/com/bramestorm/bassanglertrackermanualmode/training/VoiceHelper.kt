// 📂 utils/VoiceHelper.kt
package com.bramestorm.bassanglertrackermanualmode.training

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.widget.TextView
import com.bramestorm.bassanglertrackermanualmode.R

object VoiceHelper {

    fun startListening(context: Context, speechRecognizer: SpeechRecognizer, speechIntent: android.content.Intent, txtResult: TextView) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                txtResult.text = context.getString(R.string.error_recognizing_speech)
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val spokenText = matches?.firstOrNull()?.trim() ?: context.getString(R.string.nothing_heard)
                txtResult.text = context.getString(R.string.heard, spokenText)

                // Add more logic here if needed
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        speechRecognizer.startListening(speechIntent)
    }
}

