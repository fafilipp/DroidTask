package de.htwg.android.taskmanager.google.sync;

import java.util.List;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

public interface IGoogleTaskManager {

	public void deleteTask(String taskListId, String taskId) throws GoogleSyncException;

	public void deleteTaskList(String taskListId) throws GoogleSyncException;

	public List<TaskList> getTaskLists() throws GoogleSyncException;

	public List<Task> getTasks(String taskListId) throws GoogleSyncException;

	public Task insertTask(String taskListId, Task task) throws GoogleSyncException;

	public TaskList insertTaskList(String title) throws GoogleSyncException;

	public Task updateTask(String taskListId, String taskId, Task task) throws GoogleSyncException;

	public TaskList updateTaskList(String taskListId, TaskList taskList) throws GoogleSyncException;
}
