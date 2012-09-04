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
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class UpdateTaskListAsyncTask extends AsyncTask<TaskList, Void, TaskList> {

	private Activity activity;
	private Account account;
	private String taskListId;

	public UpdateTaskListAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	public UpdateTaskListAsyncTask(Activity activity, Account account, String taskListId) {
		this.activity = activity;
		this.account = account;
		this.taskListId = taskListId;
	}

	@Override
	protected TaskList doInBackground(TaskList... taskList) {
		TaskList returnTaskList = null;
		if (taskList.length == 1 && taskList[0] != null) {
			try {
				GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
				Tasks service = asyncUtil.getTasksService(activity, account);
				Log.d(LOG_TAG, "updating tasklist " + taskList[0].getId());
				returnTaskList = service.tasklists().update(taskListId, taskList[0]).execute();
				Log.d(LOG_TAG, "updating tasklist succeeded (id = " + returnTaskList.getId() + ")");
			} catch (Exception e) {
				Log.e(LOG_TAG, "exception thrown while updating the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
			}
		}
		return returnTaskList;
	}

	public String getTaskListId() {
		return taskListId;
	}

	public void setTaskListId(String taskListId) {
		this.taskListId = taskListId;
	}

	public TaskList updateTaskList(String taskListId, TaskList taskList) throws InterruptedException, ExecutionException, TimeoutException {
		this.taskListId = taskListId;
		AsyncTask<TaskList, Void, TaskList> asyncTaskReturn = execute(taskList);
		return asyncTaskReturn.get(MAX_WAIT_TIME, MAX_WAIT_TIME_UNIT);
	}

}
