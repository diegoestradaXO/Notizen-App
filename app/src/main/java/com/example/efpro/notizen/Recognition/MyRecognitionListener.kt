package com.example.efpro.notizen.Recognition

import android.app.Application
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.EditText
import androidx.constraintlayout.widget.R.attr.content
import com.example.efpro.notizen.R
import kotlinx.android.synthetic.main.fragment_add_note.*

class MyRecognitionListener: RecognitionListener{
    override fun onReadyForSpeech(params: Bundle){}
    val content :EditText? = null


    /**
     * The user has started to speak.
     */
    override fun onBeginningOfSpeech(){}

    /**
     * The sound level in the audio stream has changed. There is no guarantee that this method will
     * be called.
     *
     * @param rmsdB the new RMS dB value
     */
    override fun onRmsChanged(rmsdB: Float){}

    /**
     * More sound has been received. The purpose of this function is to allow giving feedback to the
     * user regarding the captured audio. There is no guarantee that this method will be called.
     *
     * @param buffer a buffer containing a sequence of big-endian 16-bit integers representing a
     * single channel audio stream. The sample rate is implementation dependent.
     */
    override fun onBufferReceived(buffer: ByteArray){}

    /**
     * Called after the user stops speaking.
     */
    override fun onEndOfSpeech(){}

    /**
     * A network or recognition error occurred.
     *
     * @param error code is defined in [SpeechRecognizer]
     */
    override fun onError(error: Int){}

    /**
     * Called when recognition results are ready.
     *
     * @param results the recognition results. To retrieve the results in `ArrayList<String>` format use [Bundle.getStringArrayList] with
     * [SpeechRecognizer.RESULTS_RECOGNITION] as a parameter. A float array of
     * confidence values might also be given in [SpeechRecognizer.CONFIDENCE_SCORES].
     */
    override fun onResults(results: Bundle){
        val matches = results
            .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if(matches != null)
            content?.setText(matches[0])
    }

    /**
     * Called when partial recognition results are available. The callback might be called at any
     * time between [.onBeginningOfSpeech] and [.onResults] when partial
     * results are ready. This method may be called zero, one or multiple times for each call to
     * [SpeechRecognizer.startListening], depending on the speech recognition
     * service implementation.  To request partial results, use
     * [RecognizerIntent.EXTRA_PARTIAL_RESULTS]
     *
     * @param partialResults the returned results. To retrieve the results in
     * ArrayList&lt;String&gt; format use [Bundle.getStringArrayList] with
     * [SpeechRecognizer.RESULTS_RECOGNITION] as a parameter
     */
    override fun onPartialResults(partialResults: Bundle){}

    /**
     * Reserved for adding future events.
     *
     * @param eventType the type of the occurred event
     * @param params a Bundle containing the passed parameters
     */
    override fun onEvent(eventType: Int, params: Bundle){}
}