package de.htwg.android.taskmanager.google.task.api;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.List;
import java.util.Observable;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.activity.MainActivity;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;
import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

public class GoogleSyncManager extends AsyncTask<Void, Void, Void> {

	public class MyObservable extends Observable {
		public void setChanged() {
			super.setChanged();
		}
	}

	private MyObservable observable;

	private MainActivity activity;
	private Account account;
	private ProgressDialog progressDialog;

	public GoogleSyncManager(MainActivity activity, Account account) {
		this.activity = activity;
		this.account = account;
		this.observable = new MyObservable();
		this.observable.addObserver(activity);
	}

	@Override
	protected Void doInBackground(Void... params) {
		try {
			DatabaseHandler dbHandler = new DatabaseHandler(activity);
			GoogleTaskApiManager apiManager = new GoogleTaskApiManager(activity, account);
			Log.i(LOG_TAG, "Starting Sync with Google Tasks (syncing local database)");
			startSyncTaskLists(dbHandler, apiManager);
			startSyncTasks(dbHandler, apiManager);
			Log.i(LOG_TAG, "Sync with Google Tasks ended.");
		} catch (GoogleSyncException googleSyncException) {
			// TODO: handle exception
		}
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		observable.setChanged();
		observable.notifyObservers();
		observable.deleteObserver(activity);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(activity, "Syncing", "Please wait");
	}

	private Task searchRemoteTaskForLocalTask(List<Task> remoteTasks, LocalTask localTask) {
		for (Task remoteTask : remoteTasks) {
			if (remoteTask.getId().equals(localTask.getGoogleId())) {
				return remoteTask;
			}
		}
		return null;
	}

	private TaskList searchRemoteTaskListForLocalTaskList(List<TaskList> remoteTaskLists, LocalTaskList localTaskList) {
		for (TaskList remoteTaskList : remoteTaskLists) {
			if (remoteTaskList.getId().equals(localTaskList.getGoogleId())) {
				return remoteTaskList;
			}
		}
		return null;
	}

	private void startSyncTaskLists(DatabaseHandler dbHandler, GoogleTaskApiManager apiManager) throws GoogleSyncException {
		List<TaskList> remoteTaskLists = apiManager.getTaskLists();
		List<LocalTaskList> deletedLocalTaskLists = dbHandler.getDeletedTaskLists();
		for (TaskList remoteTaskList : remoteTaskLists) {
			Log.d(LOG_TAG, "Remote tasklist (" + remoteTaskList.getTitle() + "/" + remoteTaskList.getId() + ") found");
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(remoteTaskList.getId());
			if (localTaskList == null) {
				Log.d(LOG_TAG, "Local tasklist for " + remoteTaskList.getId() + " not found in database. Creating it in database ...");
				// Create new local tasklist, because not in db available
				LocalTaskList newLocalTasklist = new LocalTaskList();
				newLocalTasklist.setGoogleId(remoteTaskList.getId());
				newLocalTasklist.setLastModification(transformDateTime(remoteTaskList.getUpdated()));
				newLocalTasklist.setTitle(remoteTaskList.getTitle());
				// Saving the tasklist to the database
				dbHandler.addTaskList(newLocalTasklist);
				Log.d(LOG_TAG, "... local tasklist for " + remoteTaskList.getId() + " created with id " + newLocalTasklist.getInternalId());
			} else if (!deletedLocalTaskLists.contains(localTaskList)) {
				Log.d(LOG_TAG, "Local tasklist for " + remoteTaskList.getId() + " found in database (id=" + localTaskList.getInternalId()
						+ ")");
				if (localTaskList.getLastModification() > transformDateTime(remoteTaskList.getUpdated())) {
					Log.d(LOG_TAG, "... the local tasklist is newer. Syncing it to Google Tasks.");
					// updating remote tasklist, due local timestamp is bigger
					remoteTaskList.setTitle(localTaskList.getTitle());
					// sending update command to tasks api
					remoteTaskList = apiManager.updateTaskList(localTaskList.getGoogleId(), remoteTaskList);
				} else {
					Log.d(LOG_TAG, "... the local tasklist is older. Syncing the Google tasklist to the database.");
					// updating local tasklist, due remote timestamp is bigger
					localTaskList.setTitle(remoteTaskList.getTitle());
					localTaskList.setLastModification(transformDateTime(remoteTaskList.getUpdated()));
					// sending update statement to database
					dbHandler.updateTaskList(localTaskList);
				}
			}
		}
		// create new remote tasklist, if no google id provided in database
		List<LocalTaskList> newLocalTaskLists = dbHandler.getTaskListsWithoutGoogleId();
		Log.d(LOG_TAG, "Found " + newLocalTaskLists.size() + " tasklists to sync to the Google server.");
		for (LocalTaskList newLocalTaskList : newLocalTaskLists) {
			Log.d(LOG_TAG, "Creating new remote tasklist (" + newLocalTaskList.getTitle() + "/" + newLocalTaskList.getInternalId() + ")");
			TaskList remoteTaskList = apiManager.insertTaskList(newLocalTaskList.getTitle());
			newLocalTaskList.setGoogleId(remoteTaskList.getId());
			dbHandler.updateTaskList(newLocalTaskList);
			remoteTaskLists.add(remoteTaskList);
			Log.d(LOG_TAG, "... new remote tasklist created with id " + remoteTaskList.getId());
		}

		// checking remotly deleted tasklists (existing in database, but not
		// remotely)
		List<LocalTaskList> localTaskLists = dbHandler.getTaskLists();
		for (LocalTaskList localTaskList : localTaskLists) {
			if (searchRemoteTaskListForLocalTaskList(remoteTaskLists, localTaskList) == null) {
				Log.d(LOG_TAG, "No remote tasklist found for local tasklist " + localTaskList.getInternalId() + " ... deleting it locally.");
				dbHandler.deleteTaskList(localTaskList.getInternalId());
			}
		}

		// TODO: remote delete case is too risky, so no remote delete done at
		// this point.
	}

	private void startSyncTasks(DatabaseHandler dbHandler, GoogleTaskApiManager apiManager) throws GoogleSyncException {
		List<LocalTaskList> localTaskLists = dbHandler.getTaskLists();
		List<LocalTask> deletedLocalTasks = dbHandler.getDeletedTask();
		for (LocalTaskList localTaskList : localTaskLists) {
			Log.d(LOG_TAG, "Syncing tasks for tasklist (" + localTaskList.getTitle() + "/" + localTaskList.getInternalId() + ").");
			List<Task> remoteTasks = apiManager.getTasks(localTaskList.getGoogleId());
			for (Task remoteTask : remoteTasks) {
				Log.d(LOG_TAG, "Remote task (" + remoteTask.getTitle() + "/" + remoteTask.getId() + ") found");
				LocalTask localTask = dbHandler.getTaskByGoogleId(remoteTask.getId());
				if (localTask == null) {
					Log.d(LOG_TAG, "Local task for " + remoteTask.getId() + " not found in database. Creating it in database ...");
					// Create new local tasklist, because not in db available
					LocalTask newLocalTask = new LocalTask();
					newLocalTask.setGoogleId(remoteTask.getId());
					newLocalTask.setLastModification(transformDateTime(remoteTask.getUpdated()));
					newLocalTask.setTitle(remoteTask.getTitle());
					newLocalTask.setNotes(remoteTask.getNotes());
					newLocalTask.setStatus(EStatus.transformStatus(remoteTask.getStatus()));
					newLocalTask.setDue(transformDateTime(remoteTask.getDue()));
					newLocalTask.setCompleted(transformDateTime(remoteTask.getCompleted()));
					// Saving the task to the database
					dbHandler.addTask(localTaskList, newLocalTask);
					Log.d(LOG_TAG, "... local task for " + remoteTask.getId() + " created with id " + newLocalTask.getInternalId());
				} else if (!deletedLocalTasks.contains(localTask)) {
					Log.d(LOG_TAG, "Local task for " + remoteTask.getId() + " found in database (id=" + localTask.getInternalId() + ")");
					if (localTask.getLastModification() > transformDateTime(remoteTask.getUpdated())) {
						Log.d(LOG_TAG, "... the local task is newer. Syncing it to Google Tasks.");
						// updating remote tasklist, due local timestamp is
						// bigger
						remoteTask.setTitle(localTask.getTitle());
						remoteTask.setNotes(localTask.getNotes());
						remoteTask.setStatus(EStatus.transformStatus(localTask.getStatus()));
						// TODO: Due and Completed not working for tasks.
						// remoteTask.setDue(transformDateTime(localTask.getDue()));
						// remoteTask.setCompleted(transformDateTime(localTask.getCompleted()));
						// sending update command to tasks api
						apiManager.updateTask(localTaskList.getGoogleId(), localTask.getGoogleId(), remoteTask);
					} else {
						Log.d(LOG_TAG, "... the local task is older. Syncing the Google task to the database.");
						// updating local tasklist, due remote timestamp is
						// bigger
						localTask.setGoogleId(remoteTask.getId());
						localTask.setLastModification(transformDateTime(remoteTask.getUpdated()));
						localTask.setTitle(remoteTask.getTitle());
						localTask.setNotes(remoteTask.getNotes());
						localTask.setStatus(EStatus.transformStatus(remoteTask.getStatus()));
						localTask.setDue(transformDateTime(remoteTask.getDue()));
						localTask.setCompleted(transformDateTime(remoteTask.getCompleted()));
						// sending update statement to database
						dbHandler.updateTask(localTask);
					}
				}
			}
			// create new remote tasklist, if no google id provided in database
			List<LocalTask> newLocalTasks = dbHandler.getTasksWithoutGoogleId(localTaskList);
			Log.d(LOG_TAG, "Found " + newLocalTasks.size() + " tasks to sync to the Google server.");
			for (LocalTask newLocalTask : newLocalTasks) {
				Log.d(LOG_TAG, "Creating new remote task (" + newLocalTask.getTitle() + "/" + newLocalTask.getInternalId() + ")");
				Task remoteTask = new Task();
				remoteTask.setTitle(newLocalTask.getTitle());
				remoteTask.setNotes(newLocalTask.getNotes());
				remoteTask.setStatus(EStatus.transformStatus(newLocalTask.getStatus()));
				// TODO: Due and Completed not working for tasks.
				// remoteTask.setDue(transformDateTime(newLocalTask.getDue()));
				// remoteTask.setCompleted(transformDateTime(newLocalTask.getCompleted()));
				Task newRemoteTask = apiManager.insertTask(localTaskList.getGoogleId(), remoteTask);
				newLocalTask.setGoogleId(newRemoteTask.getId());
				dbHandler.updateTask(newLocalTask);
				remoteTasks.add(newRemoteTask);
				Log.d(LOG_TAG, "... new remote task created with id " + newRemoteTask.getId());
			}

			// checking remotly deleted tasklists (existing in database, but not
			// remotely)
			List<LocalTask> localTasks = dbHandler.getTasks(localTaskList);
			for (LocalTask localTask : localTasks) {
				if (searchRemoteTaskForLocalTask(remoteTasks, localTask) == null) {
					Log.d(LOG_TAG, "No remote task found for local task " + localTask.getInternalId() + " ... deleting it locally.");
					dbHandler.deleteTask(localTask.getInternalId());
				}
			}

			// TODO: remote delete case is too risky, so no remote delete done
			// at this point.
		}
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
	@SuppressWarnings("unused")
	private DateTime transformDateTime(long d) {
		return d != 0 ? new DateTime(d) : null;
	}

}
