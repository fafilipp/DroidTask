package de.htwg.android.taskmanager.backend.change;

import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.ETaskAttributes;
import de.htwg.android.taskmanager.backend.util.ETaskListAttributes;
import de.htwg.android.taskmanager.backend.util.EType;

public class UpdateChange implements Change {

	private EType type;
	private ETaskListAttributes taskListAttribute;
	private LocalTaskList localTaskList;
	private ETaskAttributes taskAttribute;
	private LocalTask localTask;

	public UpdateChange(LocalTask localTask, ETaskAttributes taskAttribute) {
		this.type = EType.TASK;
		this.localTask = localTask;
		this.taskAttribute = taskAttribute;
	}

	public UpdateChange(LocalTaskList localTaskList, ETaskListAttributes taskListAttrbute) {
		this.type = EType.TASKLIST;
		this.localTaskList = localTaskList;
		this.taskListAttribute = taskListAttrbute;
	}

	public EType getType() {
		return type;
	}

	public LocalTaskList getLocalTaskList() {
		return localTaskList;
	}

	public LocalTask getLocalTask() {
		return localTask;
	}

	public ETaskAttributes getTaskAttribute() {
		return taskAttribute;
	}

	public ETaskListAttributes getTaskListAttribute() {
		return taskListAttribute;
	}
}