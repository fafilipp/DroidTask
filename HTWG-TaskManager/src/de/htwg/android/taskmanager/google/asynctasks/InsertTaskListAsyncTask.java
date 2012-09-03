package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class InsertTaskListAsyncTask extends AsyncTask<String, Void, TaskList> {

	private Activity activity;
	private Account account;

	public InsertTaskListAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected TaskList doInBackground(String... title) {
		TaskList taskList = null;
		if (title.length == 1 && title[0] != null) {
			try {
				GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "creating tasklist " + title[0]);
				TaskList temp = new TaskList();
				temp.setTitle(title[0]);
				taskList = service.tasklists().insert(temp).execute();
				Log.d(LOG_TAG, "creating tasklist succeeded (id = " + taskList.getId() + ")");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while creating/inserting the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return taskList;
	}

}
