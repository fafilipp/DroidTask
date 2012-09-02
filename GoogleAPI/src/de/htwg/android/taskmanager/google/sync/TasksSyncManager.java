package de.htwg.android.taskmanager.google.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;

import xml.Marshalling;

import com.google.api.client.auth.oauth2.draft10.AccessTokenResponse;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleAuthorizationCodeGrant;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAuthorizationRequestUrl;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import entity.EStatus;
import entity.ListOfTaskList;

@SuppressWarnings("deprecation")
public class TasksSyncManager {

	void syncLocalToGoogle(entity.TaskList taskList) {

	}

	void syncGoogleToLocal(TaskList taskList) {

	}

	void syncLocalToGoogle(entity.Task task) {

	}

	void syncGoogleToLocal(Task task) {

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
			
			
			if(remoteTaskList == null) {
				// insert the new task list to the remote server
				// TODO: insert or delete?
			} else if(localTaskList.getDeleted()){
				// delete the remote task list.
			} else {
				// check modification and sync date and update accordingly the right way
				// TODO: what should happen if both are been updated?
			}
		}
	}

	public static void main(String[] args) throws Exception {
		TasksSyncManager syncManager = new TasksSyncManager();
		syncManager.createXMLforGoogleData();
	}

	private Tasks service;

	public void createXMLforGoogleData() throws Exception {
		ListOfTaskList listOfTasklist = new ListOfTaskList();
		List<TaskList> taskLists = getAllTasklists();
		for (TaskList taskList : taskLists) {
			entity.TaskList eTaskList = taskListTransformation(taskList);
			listOfTasklist.add_TaskList_To_ListOfTaskLists(eTaskList);
			List<Task> tasks = getAllTasksForTasklist(taskList);
			for (Task task : tasks) {
				entity.Task eTask = taskTransformation(task);
				eTaskList.addTaskToList(eTask);
			}
		}
		Marshalling m = new Marshalling();
		m.SaveToXML(listOfTasklist);
	}

	public void deleteTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().delete(taskList.getId(), task.getId()).execute();
	}

	public void deleteTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().delete(taskList.getId()).execute();
	}

	public List<TaskList> getAllTasklists() throws IOException {
		return getGoogleSyncManagerService().tasklists().list().execute().getItems();
	}

	public List<Task> getAllTasksForTasklist(TaskList taskList) throws IOException {
		return getGoogleSyncManagerService().tasks().list(taskList.getId()).execute().getItems();
	}

	private Tasks getGoogleSyncManagerService() {
		if (service == null) {
			HttpTransport httpTransport = new NetHttpTransport();
			JacksonFactory jsonFactory = new JacksonFactory();
			HttpRequestInitializer accessProtectedResource = getLocalAccess(httpTransport, jsonFactory);
			Tasks service = new Tasks.Builder(httpTransport, jsonFactory, accessProtectedResource).setApplicationName(
					"Google-TasksAndroidSample/1.0").build();
			this.service = service;
		}
		return service;
	}

	private HttpRequestInitializer getLocalAccess(HttpTransport httpTransport, JacksonFactory jsonFactory) {
		// The clientId and clientSecret are copied from the API Access tab on
		// the Google APIs Console
		String clientId = "404772683687.apps.googleusercontent.com";
		String clientSecret = "sthWNcu7cGWS9CHuHbM8TkFU";

		// Or your redirect URL for web based applications.
		String redirectUrl = "urn:ietf:wg:oauth:2.0:oob";
		String scope = "https://www.googleapis.com/auth/tasks";

		// Step 1: Authorize -->
		String authorizationUrl = new GoogleAuthorizationRequestUrl(clientId, redirectUrl, scope).build();

		// Point or redirect your user to the authorizationUrl.
		System.out.println("Go to the following link in your browser:");
		System.out.println(authorizationUrl);

		// Read the authorization code from the standard input stream.
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("What is the authorization code?");

		try {
			String code = in.readLine();
			// End of Step 1 <--

			// Step 2: Exchange -->
			AccessTokenResponse response = new GoogleAuthorizationCodeGrant(httpTransport, jsonFactory, clientId, clientSecret, code,
					redirectUrl).execute();
			// End of Step 2 <--

			GoogleAccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(response.accessToken, httpTransport,
					jsonFactory, clientId, clientSecret, response.refreshToken);
			return accessProtectedResource;
		} catch (IOException e) {
			// TODO: Excecption
			return null;
		}
	}

	@SuppressWarnings("unused")
	private HttpRequestInitializer getPhoneAccess(HttpTransport httpTransport, JacksonFactory jsonFactory) {
		return null;
	}

	public void insertNewTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().insert(taskList.getId(), task).execute();
	}

	public void insertNewTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().insert(taskList).execute();
	}

	public entity.TaskList taskListTransformation(TaskList taskList) {
		entity.TaskList eTaskList = new entity.TaskList();
		eTaskList.setTitle(taskList.getTitle());
		eTaskList.setUpdate(transformDateTime(taskList.getUpdated()));
		eTaskList.setDeleted(false);
		return eTaskList;
	}
	
	public TaskList taskListTransformation(entity.TaskList eTaskList) {
		TaskList taskList = new TaskList();
		taskList.setId(eTaskList.getID());
		taskList.setEtag("");
		taskList.setKind("");
		taskList.setTitle(eTaskList.getTitle());
		taskList.setUpdated(new DateTime(eTaskList.getUpdate().getTime()));
		return taskList;
	}

	public entity.Task taskTransformation(Task task) {
		entity.Task eTask = new entity.Task();
		eTask.setLastModification(transformDateTime(task.getUpdated()));
		eTask.setParentTask(task.getParent());
		eTask.setPosition(task.getPosition());
		eTask.setTitle(task.getTitle());
		eTask.setNotes(task.getNotes());
		if (task.getStatus().equals("needsAction")) {
			eTask.setStatus(EStatus.needsAction);
		} else {
			eTask.setStatus(EStatus.completed);
		}
		eTask.setDue(transformDateTime(task.getDue()));
		eTask.setCompleted(transformDateTime(task.getCompleted()));
		eTask.setDeleted(task.getDeleted() == null ? false : task.getDeleted());
		eTask.setHidden(task.getHidden() == null ? false : task.getHidden());
		return eTask;
	}

	private Timestamp transformDateTime(DateTime d) {
		return d != null ? new Timestamp(d.getValue()) : null;
	}

	public void updateTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().update(taskList.getId(), task.getId(), task).execute();
	}

	public void updateTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().update(taskList.getId(), taskList).execute();
	}
}
