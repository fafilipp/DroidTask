package de.htwg.android.taskmanager.activity;

import android.test.ActivityInstrumentationTestCase2;

public class NewAndEditTaskActivityTest extends
ActivityInstrumentationTestCase2<NewAndEditTaskActivity> {
	
	public NewAndEditTaskActivityTest() {
		super(NewAndEditTaskActivity.class);
	}

	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", getActivity());
	}
	
}
