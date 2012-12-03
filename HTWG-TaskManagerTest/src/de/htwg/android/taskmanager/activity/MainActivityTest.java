package de.htwg.android.taskmanager.activity;

import android.test.ActivityInstrumentationTestCase2;

public class MainActivityTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public MainActivityTest() {
		super(MainActivity.class);
	}

	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", getActivity());
	}
}