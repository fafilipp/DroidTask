package de.htwg.android.taskmanager.google.asynctasks;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.app.Activity;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

public class AsyncTasksManager {

	private Activity activity;
	private Account account;

	public AsyncTasksManager(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	public void deleteTask(String taskListId, String taskId) {
		DeleteTaskAsyncTask asyncTask = new DeleteTaskAsyncTask(activity, account);
		asyncTask.deleteTask(taskListId, taskId);
	}

	public void deleteTaskList(String taskListId) {
		DeleteTaskListAsyncTask asyncTask = new DeleteTaskListAsyncTask(activity, account);
		asyncTask.deleteTaskList(taskListId);
	}

	public List<Task> getTasks(String taskListId) throws InterruptedException, ExecutionException, TimeoutException {
		GetTaskAsyncTask asyncTask = new GetTaskAsyncTask(activity, account);
		return asyncTask.getTasks(taskListId);
	}

	public List<TaskList> getTaskLists() throws InterruptedException, ExecutionException, TimeoutException {
		GetTaskListAsyncTask asyncTask = new GetTaskListAsyncTask(activity, account);
		return asyncTask.getTaskLists();
	}

	public Task insertTask(String taskListId, Task task) throws InterruptedException, ExecutionException, TimeoutException {
		InsertTaskAsyncTask asyncTask = new InsertTaskAsyncTask(activity, account);
		return asyncTask.insertTask(taskListId, task);
	}

	public TaskList insertTaskList(String title) throws InterruptedException, ExecutionException, TimeoutException {
		InsertTaskListAsyncTask asyncTask = new InsertTaskListAsyncTask(activity, account);
		return asyncTask.insertTaskList(title);
	}

	public Task updateTask(String taskListId, String taskId, Task task) throws InterruptedException, ExecutionException, TimeoutException {
		UpdateTaskAsyncTask asyncTask = new UpdateTaskAsyncTask(activity, account);
		return asyncTask.updateTask(taskListId, taskId, task);
	}

	public TaskList updateTaskList(String taskListId, TaskList taskList) throws InterruptedException, ExecutionException, TimeoutException {
		UpdateTaskListAsyncTask asyncTask = new UpdateTaskListAsyncTask(activity, account);
		return asyncTask.updateTaskList(taskListId, taskList);
	}

}
