package hello.android;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;

public class Dictate {

	public static void speech(Activity activity) {
		Intent intent = new Intent();
		intent.setAction(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		activity.startActivityForResult(intent, 888);
	}
}
