package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.MAX_WAIT_TIME;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.MAX_WAIT_TIME_UNIT;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class InsertTaskAsyncTask extends AsyncTask<Task, Void, Task> {

	private Activity activity;
	private Account account;
	private String taskListId;

	public InsertTaskAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	public InsertTaskAsyncTask(Activity activity, Account account, String taskListId) {
		this.activity = activity;
		this.account = account;
		this.taskListId = taskListId;
	}

	public String getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(String taskListId) {
		this.taskListId = taskListId;
	}

	@Override
	protected Task doInBackground(Task... task) {
		Task returnTask = null;
		if (task.length == 1 && task[0] != null) {
			try {
				GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "creating task in task list " + taskListId);
				returnTask = service.tasks().insert(taskListId, task[0]).execute();
				Log.d(LOG_TAG, "creating task succeeded (id = " + returnTask.getId() + ")");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while creating/inserting the task = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return returnTask;
	}

	public Task insertTask(String taskListId, Task task) throws InterruptedException, ExecutionException, TimeoutException {
		this.taskListId = taskListId;
		AsyncTask<Task, Void, Task> asyncTaskReturn = execute(task);
		return asyncTaskReturn.get(MAX_WAIT_TIME, MAX_WAIT_TIME_UNIT);
	}
}
