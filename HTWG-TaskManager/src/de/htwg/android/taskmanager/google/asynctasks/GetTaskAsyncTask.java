package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.MAX_WAIT_TIME;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.MAX_WAIT_TIME_UNIT;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class GetTaskAsyncTask extends AsyncTask<String, Void, List<Task>> {

	private Activity activity;
	private Account account;

	public GetTaskAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected List<Task> doInBackground(String... taskListId) {
		List<Task> taskList = null;
		if (taskListId.length == 1 && taskListId[0] != null) {
			try {
				GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "getting all tasks for tasklist " + taskListId[0]);
				taskList = service.tasks().list(taskListId[0]).execute().getItems();
				Log.d(LOG_TAG, "getting all task lists succeeded (size = " + taskList.size() + ")");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while getting all tasks = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return taskList;
	}

	public List<Task> getTasks(String taskListId) throws InterruptedException, ExecutionException, TimeoutException {
		AsyncTask<String, Void, List<Task>> asyncTaskReturn = execute(taskListId);
		return asyncTaskReturn.get(MAX_WAIT_TIME, MAX_WAIT_TIME_UNIT);
	}

}
