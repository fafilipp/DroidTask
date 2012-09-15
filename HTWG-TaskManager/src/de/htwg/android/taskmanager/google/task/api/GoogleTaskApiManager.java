package de.htwg.android.taskmanager.google.task.api;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.List;

import android.accounts.Account;
import android.app.Activity;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;
import de.htwg.android.taskmanager.google.task.api.util.GoogleTaskApiUtil;

public class GoogleTaskApiManager implements IGoogleTaskApiManager {

	private Activity activity;
	private Account account;

	public GoogleTaskApiManager(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	public void deleteTask(String taskListId, String taskId) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "deleting task " + taskId + " from tasklist " + taskListId);
			service.tasks().delete(taskListId, taskId).execute();
			Log.d(LOG_TAG, "deleting task succeeded");
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while deleting the task = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	public void deleteTaskList(String taskListId) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "deleting tasklist " + taskListId);
			service.tasklists().delete(taskListId).execute();
			Log.d(LOG_TAG, "deleting tasklist succeeded");
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while deleting the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	public List<TaskList> getTaskLists() throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "getting all task lists");
			List<TaskList> list = service.tasklists().list().execute().getItems();
			Log.d(LOG_TAG, "getting all task lists succeeded (size = " + list.size() + ")");
			return list;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while getting all task lists = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	public List<Task> getTasks(String taskListId) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "getting all tasks for tasklist " + taskListId);
			List<Task> taskList = service.tasks().list(taskListId).execute().getItems();
			Log.d(LOG_TAG, "getting all task lists succeeded (size = " + taskList.size() + ")");
			return taskList;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while getting all tasks = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	public Task insertTask(String taskListId, Task task) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "creating task in task list " + taskListId);
			Task returnTask = service.tasks().insert(taskListId, task).execute();
			Log.d(LOG_TAG, "creating task succeeded (id = " + returnTask.getId() + ")");
			return returnTask;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while creating/inserting the task = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	public TaskList insertTaskList(String title) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "creating tasklist " + title);
			TaskList temp = new TaskList();
			temp.setTitle(title);
			TaskList taskList = service.tasklists().insert(temp).execute();
			Log.d(LOG_TAG, "creating tasklist succeeded (id = " + taskList.getId() + ")");
			return taskList;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while creating/inserting the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	public Task updateTask(String taskListId, String taskId, Task task) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "updating task " + taskId + " in task list " + taskListId);
			Task returnTask = service.tasks().update(taskListId, taskId, task).execute();
			Log.d(LOG_TAG, "updating task succeeded (id = " + returnTask.getId() + ")");
			return returnTask;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while updating the task = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}

	}

	public TaskList updateTaskList(String taskListId, TaskList taskList) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "updating tasklist " + taskList.getId());
			TaskList returnTaskList = service.tasklists().update(taskListId, taskList).execute();
			Log.d(LOG_TAG, "updating tasklist succeeded (id = " + returnTaskList.getId() + ")");
			return returnTaskList;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while updating the tasklist = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

}
