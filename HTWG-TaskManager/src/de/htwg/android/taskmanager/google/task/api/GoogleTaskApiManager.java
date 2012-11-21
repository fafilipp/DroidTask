package de.htwg.android.taskmanager.google.task.api;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.app.Activity;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;
import de.htwg.android.taskmanager.google.task.api.util.GoogleTaskApiUtil;

/**
 * This GoogleTask Api implementation of the ITaskApiManager.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class GoogleTaskApiManager implements ITaskApiManager {

	/**
	 * The activity of where the calls will be performed.
	 */
	private Activity activity;

	/**
	 * The google account necessary for the authentification.
	 */
	private Account account;

	/**
	 * Creates a Google Task Api manager
	 * 
	 * @param activity
	 *            the activity where the calls will be performed
	 * @param account
	 *            the google account necessary for the authentification.
	 */
	public GoogleTaskApiManager(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	/**
	 * Deletes a remote task with given ids.
	 * 
	 * @param taskListId
	 *            the task list remote id.
	 * @param taskId
	 *            the task remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
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

	/**
	 * Deletes a remote task list with a given id.
	 * 
	 * @param taskListId
	 *            the task list remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
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

	/**
	 * Gets all remote task lists.
	 * 
	 * @return the remote task lists (in the remote representation).
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public List<TaskList> getTaskLists() throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "getting all task lists");
			List<TaskList> list = service.tasklists().list().execute().getItems();
			list = list == null ? new ArrayList<TaskList>() : list;
			Log.d(LOG_TAG, "getting all task lists succeeded (size = " + list.size() + ")");
			return list;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while getting all task lists = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	/**
	 * Gets all remote tasks for a given task list id.
	 * 
	 * @param taskListId
	 *            the task list id.
	 * @return a list of tasks (in the remote representation).
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
	public List<Task> getTasks(String taskListId) throws GoogleSyncException {
		try {
			GoogleTaskApiUtil asyncUtil = new GoogleTaskApiUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "getting all tasks for tasklist " + taskListId);
			List<Task> taskList = service.tasks().list(taskListId).execute().getItems();
			taskList = taskList == null ? new ArrayList<Task>() : taskList;
			Log.d(LOG_TAG, "getting all task lists succeeded (size = " + taskList.size() + ")");
			return taskList;
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while getting all tasks = (" + e.getClass().getName() + ")" + e.getMessage());
			throw new GoogleSyncException(e);
		}
	}

	/**
	 * Inserts a remote task in a task list.
	 * 
	 * @param taskListId
	 *            the remote task list id
	 * @param task
	 *            the task to be add in the remote representation.
	 * @return the Task after insertion with remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
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

	/**
	 * Inserts a remote task list with a given title.
	 * 
	 * @param title
	 *            the title for this task list.
	 * @return the TaskList insertion with remote id.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
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

	/**
	 * Updates a remote task.
	 * 
	 * @param taskListId
	 *            the task list id where the task is into.
	 * @param taskId
	 *            the task id of the task which should be updated.
	 * @param task
	 *            the updated task object
	 * @return the updated Task object.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
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

	/**
	 * Updates a remote task list.
	 * 
	 * @param taskListId
	 *            the task list id which should be updated.
	 * @param taskList
	 *            the updated task list object
	 * @return the updated TaskList object.
	 * @throws GoogleSyncException
	 *             will be thrown if something goes wrong while executing.
	 */
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
