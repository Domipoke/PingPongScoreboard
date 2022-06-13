package it.domipoke.pingpongscoreboard;

import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import java.util.Locale;

class VoiceRecognition  implements Runnable {
   SpeechRecognizer speechRecognizer;
   Context ctx;
   public VoiceRecognition(Context contex) {
      ctx=contex;
   }
   @Override
   public void run() {
      sRecognizer();
   }

   private void sRecognizer() {
      speechRecognizer = SpeechRecognizer.createSpeechRecognizer(ctx);
      final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

   }

}
