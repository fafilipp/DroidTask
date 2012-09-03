package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class DeleteTaskAsyncTask extends AsyncTask<String, Void, Void> {

	private Activity activity;
	private Account account;

	public DeleteTaskAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected Void doInBackground(String... ids) {
		if (ids.length == 2 && ids[0] != null && ids[1] != null) {
			GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
			try {
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "deleting task " + ids[1] + " from tasklist " + ids[0]);
				service.tasks().delete(ids[0], ids[1]).execute();
				Log.d(LOG_TAG, "deleting task succeeded");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while deleting the task = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return null;
	}

}
