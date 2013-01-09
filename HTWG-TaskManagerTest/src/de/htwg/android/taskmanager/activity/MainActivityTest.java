package de.htwg.android.taskmanager.activity;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", getActivity());
	}
	
	public void testActivityPause(){
		
		final View view = getActivity().findViewById(R.id.add);
		
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				view.requestFocus();
				view.callOnClick();
				
//				EditText title = (EditText) getActivity().findViewById(R.id.title);
				
			}
		});
		
//		getActivity().finish();
		
	}
}