package com.shubham.lib_speechrecognizer.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.shubham.lib_speechrecognizer.speech.communicator.ISpeechToTextConvertListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

class SpeechRecognizerManager private constructor(private val speechRecognizer: SpeechRecognizer) : RecognitionListener {

    private var callbackProvider: ISpeechToTextConvertListener? = null

    companion object {
        fun create(context: Context): SpeechRecognizerManager {
            val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

            return SpeechRecognizerManager(speechRecognizer)
        }
    }

    fun setListener(listener: ISpeechToTextConvertListener) {
        callbackProvider = listener
    }



    fun startRecognizingSpeech() {

            val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(
                    RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            }
        speechIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now");
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        speechRecognizer.setRecognitionListener(this)

        Timber.tag("SpeechRecognizer").e("the speech has been started")

        speechRecognizer.startListening(speechIntent)

    }

    fun stopRecognizer() {
            speechRecognizer.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onReadyForSpeech")
    }

    override fun onBeginningOfSpeech() {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onBeginningOfSpeech")
    }

    override fun onRmsChanged(rmsdB: Float) {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onRmsChanged")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onBufferReceived")
    }

    override fun onEndOfSpeech() {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onEndOfSpeech")
    }

    override fun onError(error: Int) {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onError: $error")
    }

    override fun onResults(results: Bundle?) {
        val spokenText = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.getOrNull(0)
        Timber.tag("SpeechRecognizer").e("the results are $spokenText")
        if (!spokenText.isNullOrEmpty()) {
            callbackProvider?.speechToTextConverted(spokenText)
        }
        stopRecognizer()
    }

    override fun onPartialResults(partialResults: Bundle?) {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onPartialResults")
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Timber.tag("SpeechRecognizer").i("SpeechRecognizer onEvent: $eventType")
    }
}
