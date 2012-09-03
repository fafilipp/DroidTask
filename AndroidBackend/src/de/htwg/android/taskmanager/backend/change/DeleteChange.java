package de.htwg.android.taskmanager.backend.change;

import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EType;

public class DeleteChange implements Change {

	private EType type;
	private LocalTaskList localTaskList;
	private LocalTask localTask;
	
	public DeleteChange(LocalTask localTask) {
		this.type = EType.TASK;
		this.localTask = localTask;
	}

	public DeleteChange(LocalTaskList localTaskList) {
		this.type = EType.TASKLIST;
		this.localTaskList = localTaskList;
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

}
