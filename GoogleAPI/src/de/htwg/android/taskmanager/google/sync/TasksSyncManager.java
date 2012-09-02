package de.htwg.android.taskmanager.google.sync;

import java.util.List;

import com.google.api.services.tasks.model.TaskList;

public class TasksSyncManager {

	private TasksSyncHelper syncHelper;

	public TasksSyncManager() {
		syncHelper = new TasksSyncHelper();
	}

	/**
	 * local tasklist is been deleted? - local.getDeleted is true
	 * 
	 * remote tasklist is been deleted? - remote tasklist doesn't exist -
	 * lastModification < lastSyncDatum
	 * 
	 * new local tasklist created - remote tasklist doesn't exist -
	 * lastModification > lastSyncDatum
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
	 */
	void sync(List<entity.TaskList> local, List<TaskList> remote) {
		for (entity.TaskList localTaskList : local) {
			TaskList remoteTaskList = searchRemoteTaskList(remote, localTaskList);
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
			} else if (localTaskList.getDeleted()) {
				// delete the remote task list.
			} else {
				// check modification and sync date and update accordingly the
				// right way
				// TODO: what should happen if both are been updated?
			}
		}
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
	private TaskList searchRemoteTaskList(List<TaskList> remote, entity.TaskList localTaskList) {
		TaskList remoteTaskList = null;
		for (TaskList tmpRemoteTaskList : remote) {
			if (tmpRemoteTaskList.getId().equals(localTaskList.getID())) {
				remoteTaskList = tmpRemoteTaskList;
				break;
			}
		}
		return remoteTaskList;
	}

}
