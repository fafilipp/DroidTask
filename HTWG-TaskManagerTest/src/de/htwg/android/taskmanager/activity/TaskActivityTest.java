package de.htwg.android.taskmanager.activity;

import android.test.ActivityInstrumentationTestCase2;

public class TaskActivityTest extends
		ActivityInstrumentationTestCase2<TaskActivity> {
	public TaskActivityTest() {
		super(TaskActivity.class);
	}

	public void testActivityTestCaseSetUpProperly() {
		assertNotNull("activity should be launched successfully", getActivity());
	}

}
