package de.htwg.android.taskmanager.google.sync;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

public class EntitiyTransformator {

	private DatabaseHandler dbHandler;

	public EntitiyTransformator(DatabaseHandler dbHandler) {
		this.dbHandler = dbHandler;
	}

	/**
	 * Transforms a Google represented task list into a local represented
	 * tasklist
	 * 
	 * @param taskList
	 *            the Google represented task list
	 * @return local represented task list
	 */
	public LocalTaskList taskListTransformation(TaskList taskList) {
		LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(taskList.getId());
		if (localTaskList == null) {
			localTaskList = new LocalTaskList();
		}
		localTaskList.setGoogleId(taskList.getId());
		localTaskList.setLastModification(transformDateTime(taskList.getUpdated()));
		localTaskList.setTitle(taskList.getTitle());
		return localTaskList;
	}

	/**
	 * Transforms a Google represented task into a local represented task
	 * 
	 * @param taskList
	 *            the Google represented task list
	 * @return local represented task list
	 */
	public LocalTask taskTransformation(Task task) {
		LocalTask localTask = dbHandler.getTaskByGoogleId(task.getId());
		if (localTask == null) {
			localTask = new LocalTask();
		}
		localTask.setGoogleId(task.getId());
		localTask.setLastModification(transformDateTime(task.getUpdated()));
		localTask.setTitle(task.getTitle());
		localTask.setStatus(EStatus.transformStatus(task.getStatus()));
		localTask.setDue(transformDateTime(task.getDue()));
		localTask.setCompleted(transformDateTime(task.getCompleted()));
		localTask.setNotes(task.getNotes());
		return localTask;
	}

	/**
	 * Transforms a Google represented task into a local represented task
	 * 
	 * @param taskList
	 *            the Google represented task list
	 * @return local represented task list
	 */
	public Task taskTransformation(LocalTask localTask) {
		Task task = new Task();
		task.setUpdated(transformDateTime(localTask.getLastModification()));
		task.setTitle(localTask.getTitle());
		task.setStatus(EStatus.transformStatus(localTask.getStatus()));
		task.setDue(transformDateTime(localTask.getDue()));
		task.setCompleted(transformDateTime(localTask.getCompleted()));
		task.setNotes(task.getNotes());
		return task;
	}

	/**
	 * Transforms a Google DateTime object into a long Unix-Timestamp
	 * 
	 * @param d
	 *            the DateTime attribute (Google)
	 * @return zero if datetime is null, otherwise the corresponding DateTime
	 * 
	 */
	private long transformDateTime(DateTime d) {
		return d != null ? d.getValue() : 0;
	}

	/**
	 * Transforms a long Unix-Timestamp into a DateTime object
	 * 
	 * @param d
	 *            the Unix-Timestamp long
	 * @return null if the long is zero, otherwise the corresponding DateTime
	 *         object
	 * 
	 */
	private DateTime transformDateTime(long d) {
		return d != 0 ? new DateTime(d) : null;
	}
}
