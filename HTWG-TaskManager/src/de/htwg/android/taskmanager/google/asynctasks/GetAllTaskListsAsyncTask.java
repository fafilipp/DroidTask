package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.List;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class GetAllTaskListsAsyncTask extends AsyncTask<Void, Void, List<TaskList>> {

	private Activity activity;
	private Account account;

	public GetAllTaskListsAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected List<TaskList> doInBackground(Void... nothing) {
		GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
		List<TaskList> list = null;
		try {
			Tasks service = asyncUtil.getTasksService(activity, account);
			list = service.tasklists().list().execute().getItems();
			Log.d(LOG_TAG, "size of result list = " + list.size());
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while getting the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
		}
		return list;
	}

	@Override
	protected void onPostExecute(List<TaskList> result) {
		if (result == null) {
			Log.i(LOG_TAG, "result is null");
		} else {
			List<TaskList> list = result;
			for (TaskList tl : list) {
				Log.i(LOG_TAG, tl.getTitle());
			}
		}
	}
}
