package hello.android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HelloAndroidActivity extends Activity {

	EditText editText;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.myStartButton);
		final Activity activity = this;
		button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Dictate.speech(activity);
			}
		});
		
		editText = (EditText) findViewById(R.id.editText1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 888) {
			if(data!=null) {
				ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				if(!result.isEmpty()) {
					String res = result.get(0);
					editText.setText(res);
					System.out.println(res);
				}
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}