package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_ID;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class TaskActivityTest extends ActivityInstrumentationTestCase2<TaskActivity> {

	public TaskActivityTest() {
		super(TaskActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testTaskActivityOpen() {
		Intent intent = new Intent();
		intent.putExtra(ACTIVITY_KEY_TASK_ID, "");
		setActivityIntent(intent);
	}

}