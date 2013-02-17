package de.htwg.android.taskmanager.google.task.api;

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

/**
 * This class is a AsyncTask and performes the Google Sync on a different
 * Thread.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class GoogleSyncManager extends AsyncTask<Void, Void, Void> {

	/**
	 * The Observable inner class implementation, necessary to notify the
	 * Activity as soon as the sync is been finished.
	 * 
	 * @author Filippelli, Gerhart, Gillet
	 * 
	 */
	public class MyObservable extends Observable {
		public void setChanged() {
			super.setChanged();
		}
	}

	/**
	 * The MyObservable instance object, to registry the Activity.
	 */
	private MyObservable observable;

	/**
	 * This flag will be changed to false, if something goes wrong while
	 * syncing.
	 */
	private Boolean success = true;

	/**
	 * The MainActivity instance to registry the observer and call the Google
	 * Tasks Api.
	 */
	private MainActivity activity;

	/**
	 * The Google Account instance to authentificate to the Google server.
	 */
	private Account account;

	/**
	 * This ProgressDialog will be showed up as long as the sync will be
	 * performed.
	 */
	private ProgressDialog progressDialog;

	/**
	 * Creates a new GoogleSyncManager for a given Activity and a given Account.
	 * It creates the observable object and adds the activity observer to it.
	 * 
	 * @param activity
	 *            the activity instance.
	 * @param account
	 *            the account instance.
	 */
	public GoogleSyncManager(MainActivity activity, Account account) {
		this.activity = activity;
		this.account = account;
		this.observable = new MyObservable();
		this.observable.addObserver(activity);
	}

	/**
	 * Performs the Sync in a new Thread. Syncs first the task lists and then
	 * the tasks.
	 */
	@Override
	protected Void doInBackground(Void... params) {
		try {
			DatabaseHandler dbHandler = new DatabaseHandler(activity);
			GoogleTaskApiManager apiManager = new GoogleTaskApiManager(activity, account);
			Log.i("Google Sync", "Starting Sync with Google Tasks (syncing local database)");
			startSyncTaskLists(dbHandler, apiManager);
			startSyncTasks(dbHandler, apiManager);
			Log.i("Google Sync", "Sync with Google Tasks ended.");
		} catch (GoogleSyncException googleSyncException) {
			Log.e("GoogleSyncException", googleSyncException.getMessage(), googleSyncException.getInnerException());
			success = false;
		}
		return null;
	}

	/**
	 * Closes the ProgressDialog after execution.
	 */
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		progressDialog.dismiss();
		observable.setChanged();
		observable.notifyObservers(success);
		observable.deleteObserver(activity);
	}

	/**
	 * Opens a ProgressDialog before execution.
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		progressDialog = ProgressDialog.show(activity, "Syncing", "Please wait");
	}

	/**
	 * Search the remote object for a local task object.
	 * 
	 * @param remoteTasks
	 *            the remote list of tasks.
	 * @param localTask
	 *            the local task object.
	 * @return the remote task object if found, null otherwise.
	 */
	private Task searchRemoteTaskForLocalTask(List<Task> remoteTasks, LocalTask localTask) {
		for (Task remoteTask : remoteTasks) {
			if (remoteTask.getId().equals(localTask.getGoogleId())) {
				return remoteTask;
			}
		}
		return null;
	}

	/**
	 * Search the remote object for a local task list object.
	 * 
	 * @param remoteTaskLists
	 *            the remote list of task lists.
	 * @param localTaskList
	 *            the local task list object.
	 * @return the remote task object if found, null otherwise.
	 */
	private TaskList searchRemoteTaskListForLocalTaskList(List<TaskList> remoteTaskLists, LocalTaskList localTaskList) {
		for (TaskList remoteTaskList : remoteTaskLists) {
			if (remoteTaskList.getId().equals(localTaskList.getGoogleId())) {
				return remoteTaskList;
			}
		}
		return null;
	}

	/**
	 * Performs a sync from the databse to the Google Tasks Api for task lists.
	 * 
	 * @param dbHandler
	 *            the database handler to access the local tasks and task lists.
	 * @param apiManager
	 *            the api manager to access the remote tasks and task lists.
	 * @throws GoogleSyncException
	 *             if something goes wrong while syncing.
	 */
	private void startSyncTaskLists(DatabaseHandler dbHandler, GoogleTaskApiManager apiManager) throws GoogleSyncException {
		List<TaskList> remoteTaskLists = apiManager.getTaskLists();
		List<LocalTaskList> deletedLocalTaskLists = dbHandler.getDeletedTaskLists();
		for (TaskList remoteTaskList : remoteTaskLists) {
			Log.d("Google Sync", "Remote tasklist (" + remoteTaskList.getTitle() + "/" + remoteTaskList.getId() + ") found");
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(remoteTaskList.getId());
			if (localTaskList == null) {
				Log.d("Google Sync", "Local tasklist for " + remoteTaskList.getId() + " not found in database. Creating it in database ...");
				// Create new local tasklist, because not in db available
				LocalTaskList newLocalTasklist = new LocalTaskList();
				newLocalTasklist.setGoogleId(remoteTaskList.getId());
				newLocalTasklist.setLastModification(transformDateTime(remoteTaskList.getUpdated()));
				newLocalTasklist.setTitle(remoteTaskList.getTitle());
				// Saving the tasklist to the database
				dbHandler.addTaskList(newLocalTasklist);
				Log.d("Google Sync", "... local tasklist for " + remoteTaskList.getId() + " created with id " + newLocalTasklist.getInternalId());
			} else if (!deletedLocalTaskLists.contains(localTaskList)) {
				Log.d("Google Sync", "Local tasklist for " + remoteTaskList.getId() + " found in database (id=" + localTaskList.getInternalId()
						+ ")");
				if (localTaskList.getLastModification() > transformDateTime(remoteTaskList.getUpdated())) {
					Log.d("Google Sync", "... the local tasklist is newer. Syncing it to Google Tasks.");
					// updating remote tasklist, due local timestamp is bigger
					remoteTaskList.setTitle(localTaskList.getTitle());
					// sending update command to tasks api
					remoteTaskList = apiManager.updateTaskList(localTaskList.getGoogleId(), remoteTaskList);
				} else {
					Log.d("Google Sync", "... the local tasklist is older. Syncing the Google tasklist to the database.");
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
		Log.d("Google Sync", "Found " + newLocalTaskLists.size() + " tasklists to sync to the Google server.");
		for (LocalTaskList newLocalTaskList : newLocalTaskLists) {
			Log.d("Google Sync", "Creating new remote tasklist (" + newLocalTaskList.getTitle() + "/" + newLocalTaskList.getInternalId() + ")");
			TaskList remoteTaskList = apiManager.insertTaskList(newLocalTaskList.getTitle());
			newLocalTaskList.setGoogleId(remoteTaskList.getId());
			dbHandler.updateTaskList(newLocalTaskList);
			remoteTaskLists.add(remoteTaskList);
			Log.d("Google Sync", "... new remote tasklist created with id " + remoteTaskList.getId());
		}

		// checking remotly deleted tasklists (existing in database, but not
		// remotely)
		List<LocalTaskList> localTaskLists = dbHandler.getTaskLists();
		for (LocalTaskList localTaskList : localTaskLists) {
			if (searchRemoteTaskListForLocalTaskList(remoteTaskLists, localTaskList) == null) {
				Log.d("Google Sync", "No remote tasklist found for local tasklist " + localTaskList.getInternalId() + " ... deleting it locally.");
				dbHandler.deleteTaskList(localTaskList.getInternalId());
			}
		}

		// TODO: remote delete case is too risky, so no remote delete done at
		// this point.
	}

	/**
	 * Performs a sync from the database to the Google Tasks Api for tasks.
	 * 
	 * @param dbHandler
	 *            the database handler to access the local tasks and task lists.
	 * @param apiManager
	 *            the api manager to access the remote tasks and task lists.
	 * @throws GoogleSyncException
	 *             if something goes wrong while syncing.
	 */
	private void startSyncTasks(DatabaseHandler dbHandler, GoogleTaskApiManager apiManager) throws GoogleSyncException {
		List<LocalTaskList> localTaskLists = dbHandler.getTaskLists();
		List<LocalTask> deletedLocalTasks = dbHandler.getDeletedTask();
		for (LocalTaskList localTaskList : localTaskLists) {
			Log.d("Google Sync", "Syncing tasks for tasklist (" + localTaskList.getTitle() + "/" + localTaskList.getInternalId() + ").");
			List<Task> remoteTasks = apiManager.getTasks(localTaskList.getGoogleId());
			for (Task remoteTask : remoteTasks) {
				Log.d("Google Sync", "Remote task (" + remoteTask.getTitle() + "/" + remoteTask.getId() + ") found");
				LocalTask localTask = dbHandler.getTaskByGoogleId(remoteTask.getId());
				if (localTask == null) {
					Log.d("Google Sync", "Local task for " + remoteTask.getId() + " not found in database. Creating it in database ...");
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
					Log.d("Google Sync", "... local task for " + remoteTask.getId() + " created with id " + newLocalTask.getInternalId());
				} else if (!deletedLocalTasks.contains(localTask)) {
					Log.d("Google Sync", "Local task for " + remoteTask.getId() + " found in database (id=" + localTask.getInternalId() + ")");
					if (localTask.getLastModification() > transformDateTime(remoteTask.getUpdated())) {
						Log.d("Google Sync", "... the local task is newer. Syncing it to Google Tasks.");
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
						Log.d("Google Sync", "... the local task is older. Syncing the Google task to the database.");
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
			Log.d("Google Sync", "Found " + newLocalTasks.size() + " tasks to sync to the Google server.");
			for (LocalTask newLocalTask : newLocalTasks) {
				Log.d("Google Sync", "Creating new remote task (" + newLocalTask.getTitle() + "/" + newLocalTask.getInternalId() + ")");
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
				Log.d("Google Sync", "... new remote task created with id " + newRemoteTask.getId());
			}

			// checking remotly deleted tasklists (existing in database, but not
			// remotely)
			List<LocalTask> localTasks = dbHandler.getTasks(localTaskList);
			for (LocalTask localTask : localTasks) {
				if (searchRemoteTaskForLocalTask(remoteTasks, localTask) == null) {
					Log.d("Google Sync", "No remote task found for local task " + localTask.getInternalId() + " ... deleting it locally.");
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
