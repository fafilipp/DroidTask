package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class DeleteTaskListAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private Account account;

	public DeleteTaskListAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected Void doInBackground(String... id) {
		if (id.length == 1 && id[0] != null) {
			try {
				GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "deleting tasklist " + id[0]);
				service.tasklists().delete(id[0]).execute();
				Log.d(LOG_TAG, "deleting tasklist succeeded");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while deleting the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return null;
	}

	public void deleteTaskList(String taskListId) {
		 execute(taskListId);
	}
}
