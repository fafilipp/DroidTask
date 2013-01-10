package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_EDIT;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.ACTIVITY_KEY_TASK_TITLE;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

public class NewAndEditTaskActivityTest extends ActivityInstrumentationTestCase2<NewAndEditTaskActivity> {

	public NewAndEditTaskActivityTest() {
		super(NewAndEditTaskActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Intent intent = new Intent();
		intent.putExtra(ACTIVITY_KEY_EDIT, false);
		intent.putExtra(ACTIVITY_KEY_TASK_TITLE, "TestTask");
		setActivityIntent(intent);
	}

	public void testNewAndEditTaskActivityForNew() {
		assertNotNull(getActivity());
	}

//	public void testNewAndEditTaskActivityForEdit() {
//		Intent intent = new Intent();
//		intent.putExtra(ACTIVITY_KEY_EDIT, true);
//		intent.putExtra(ACTIVITY_KEY_TASK_ID, "");
//		setActivityIntent(intent);
//		assertNotNull(getActivity());
//	}

}