package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.app.Activity;
import android.util.Log;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.sync.GoogleSyncException;
import de.htwg.android.taskmanager.google.sync.IGoogleTaskManager;

public class AsyncTasksManager implements IGoogleTaskManager {

	private Activity activity;
	private Account account;

	public AsyncTasksManager(Activity activity, Account account) throws GoogleSyncException {
		this.activity = activity;
		this.account = account;
	}

	public void deleteTask(String taskListId, String taskId) throws GoogleSyncException {
		DeleteTaskAsyncTask asyncTask = new DeleteTaskAsyncTask(activity, account);
		asyncTask.deleteTask(taskListId, taskId);
	}

	public void deleteTaskList(String taskListId) throws GoogleSyncException {
		DeleteTaskListAsyncTask asyncTask = new DeleteTaskListAsyncTask(activity, account);
		asyncTask.deleteTaskList(taskListId);
	}

	public List<TaskList> getTaskLists() throws GoogleSyncException {
		GetTaskListAsyncTask asyncTask = new GetTaskListAsyncTask(activity, account);
		try {
			return asyncTask.getTaskLists();
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "InterruptedException caught on getTaskLists() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "ExecutionException caught on getTaskLists() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (TimeoutException e) {
			Log.e(LOG_TAG, "TimeoutException caught on getTaskLists() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		}
	}

	public List<Task> getTasks(String taskListId) throws GoogleSyncException {
		GetTaskAsyncTask asyncTask = new GetTaskAsyncTask(activity, account);
		try {
			return asyncTask.getTasks(taskListId);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "InterruptedException caught on getTasks() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "ExecutionException caught on getTasks() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (TimeoutException e) {
			Log.e(LOG_TAG, "TimeoutException caught on getTasks() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		}
	}

	public Task insertTask(String taskListId, Task task) throws GoogleSyncException {
		InsertTaskAsyncTask asyncTask = new InsertTaskAsyncTask(activity, account);
		try {
			return asyncTask.insertTask(taskListId, task);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "InterruptedException caught on insertTask() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "ExecutionException caught on insertTask() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (TimeoutException e) {
			Log.e(LOG_TAG, "TimeoutException caught on insertTask() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		}
	}

	public TaskList insertTaskList(String title) throws GoogleSyncException {
		InsertTaskListAsyncTask asyncTask = new InsertTaskListAsyncTask(activity, account);
		try {
			return asyncTask.insertTaskList(title);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "InterruptedException caught on insertTaskList() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "ExecutionException caught on insertTaskList() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (TimeoutException e) {
			Log.e(LOG_TAG, "TimeoutException caught on insertTaskList() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		}
	}

	public Task updateTask(String taskListId, String taskId, Task task) throws GoogleSyncException {
		UpdateTaskAsyncTask asyncTask = new UpdateTaskAsyncTask(activity, account);
		try {
			return asyncTask.updateTask(taskListId, taskId, task);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "InterruptedException caught on updateTask() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "ExecutionException caught on updateTask() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (TimeoutException e) {
			Log.e(LOG_TAG, "TimeoutException caught on updateTask() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		}
	}

	public TaskList updateTaskList(String taskListId, TaskList taskList) throws GoogleSyncException {
		UpdateTaskListAsyncTask asyncTask = new UpdateTaskListAsyncTask(activity, account);
		try {
			return asyncTask.updateTaskList(taskListId, taskList);
		} catch (InterruptedException e) {
			Log.e(LOG_TAG, "InterruptedException caught on updateTaskList() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (ExecutionException e) {
			Log.e(LOG_TAG, "ExecutionException caught on updateTaskList() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		} catch (TimeoutException e) {
			Log.e(LOG_TAG, "TimeoutException caught on updateTaskList() -> Throwing GoogleSyncException.");
			throw new GoogleSyncException(e);
		}
	}

}
