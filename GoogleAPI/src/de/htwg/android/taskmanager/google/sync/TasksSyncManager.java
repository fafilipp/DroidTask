package de.htwg.android.taskmanager.google.sync;

import java.io.IOException;
import java.util.List;

import com.google.api.services.tasks.model.TaskList;

public class TasksSyncManager {

	private TasksSyncHelper syncHelper;

	public TasksSyncManager() {
		syncHelper = new TasksSyncHelper();
	}

	/**
	 * Searchs the same remote tasklist for the given localTaskList. The search
	 * is performed by comparision of the ids.
	 * 
	 * @param remote
	 *            the list of remote Tasklists
	 * @param localTaskList
	 *            the local Tasklist
	 * @return the remote tasklist if it is given, null otherwise
	 */
	private TaskList searchRemoteTaskList(List<TaskList> remote, de.htwg.android.taskmanager.backend.entity.TaskList localTaskList) {
		TaskList remoteTaskList = null;
		for (TaskList tmpRemoteTaskList : remote) {
			if (tmpRemoteTaskList.getId().equals(localTaskList.getId())) {
				remoteTaskList = tmpRemoteTaskList;
				break;
			}
		}
		return remoteTaskList;
	}

	/**
	 * 
	 * 
	 * 
	 * new remote tasklist created - local tasklist doesn't exist
	 * 
	 * updated local tasklist - local.lastSyncTime < local.lastModification
	 * 
	 * updated remote tasklist - local.lastSyncTime < remote.lastUpdate
	 * 
	 * updated both tasklists --> (Hier was machen???) - local.lastSyncTime <
	 * local.lastModification and local.lastSyncTime < remote.lastUpdate -
	 * aktuellerer lastModificationTimeStamp
	 * @throws IOException 
	 */
	void sync(List<de.htwg.android.taskmanager.backend.entity.TaskList> local, List<TaskList> remote) throws IOException {
		for (de.htwg.android.taskmanager.backend.entity.TaskList localTaskList : local) {
			TaskList remoteTaskList = searchRemoteTaskList(remote, localTaskList);
			if(remoteTaskList == null) {
				remoteTaskList = syncHelper.taskListTransformation(localTaskList);
				// last sync online is null -> task list will be inserted remotly
				if(localTaskList.getLastSynchOnline() == null) {
					syncHelper.insertNewTaskList(remoteTaskList);
				} else {
					// remote task list doesn't exist --> remote is deleted or local is new!
					if(localTaskList.getUpdate().after(localTaskList.getLastSynchOnline())) {
						// local is new --> lastModification > lastSyncDatum
						syncHelper.insertNewTaskList(remoteTaskList);
					} else {
						// remote is deleted --> lastModification < lastSyncDatum
						localTaskList.setDeleted(true);
					}
				}
			} else if(localTaskList.isDeleted()) {
				// local task list has been deleted --> local.isDeleted() is true

			}
			
			
			
			// local tasklist is been deleted?

			// remote tasklist is been deleted?

			// new local tasklist created
			// new remote tasklist created

			// updated local tasklist
			// updated remote tasklist
			// updated both tasklists

			if (remoteTaskList == null) {
				// insert the new task list to the remote server
				// TODO: insert or delete?
			} else if (localTaskList.isDeleted()) {
				// delete the remote task list.
			} else {
				// check modification and sync date and update accordingly the
				// right way
				// TODO: what should happen if both are been updated?
			}
		}
	}

}
