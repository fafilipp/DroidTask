package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class UpdateTaskAsyncTask extends AsyncTask<Task, Void, Task> {

	private Activity activity;
	private Account account;
	private String taskListId;
	private String taskId;

	public UpdateTaskAsyncTask(Activity activity, Account account, String taskListId, String taskId) {
		this.activity = activity;
		this.account = account;
		this.taskListId = taskListId;
		this.taskId = taskId;
	}

	@Override
	protected Task doInBackground(Task... task) {
		Task returnTask = null;
		if (task.length == 1 && task[0] != null) {
			try {
				GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "updating task " + taskId + " in task list " + taskListId);
				returnTask = service.tasks().update(taskListId, taskId, task[0]).execute();
				Log.d(LOG_TAG, "updating task succeeded (id = " + returnTask.getId() + ")");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while updating the task = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return returnTask;
	}

}
