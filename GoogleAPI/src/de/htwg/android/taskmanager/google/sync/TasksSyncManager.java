package de.htwg.android.taskmanager.google.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import entity.EStatus;
import entity.ListOfTaskList;

@SuppressWarnings("deprecation")
public class TasksSyncManager {

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

	private Tasks service;

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

	public List<TaskList> getAllTasklists() throws IOException {
		return getGoogleSyncManagerService().tasklists().list().execute().getItems();
	}

	public List<Task> getAllTasksForTasklist(TaskList taskList) throws IOException {
		return getGoogleSyncManagerService().tasks().list(taskList.getId()).execute().getItems();
	}

	public void insertNewTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().insert(taskList).execute();
	}

	public void updateTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().update(taskList.getId(), taskList).execute();
	}

	public void deleteTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().delete(taskList.getId()).execute();
	}

	public void insertNewTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().insert(taskList.getId(), task).execute();
	}

	public void updateTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().update(taskList.getId(), task.getId(), task).execute();
	}

	public void deleteTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().delete(taskList.getId(), task.getId()).execute();
	}

	
	public static void main(String[] args) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		java.util.Date d = sdf.parse("1970-01-01T00:00:00Z");
		TasksSyncManager syncManager = new TasksSyncManager();
		ListOfTaskList listOfTasklist = new ListOfTaskList();
		List<TaskList> taskLists = syncManager.getAllTasklists();
		for (TaskList taskList : taskLists) {
			entity.TaskList eTaskList = new entity.TaskList();
			listOfTasklist.add_TaskList_To_ListOfTaskLists(eTaskList);
			eTaskList.setTitle(taskList.getTitle());
			eTaskList.setUpdate(new Timestamp(d.getTime()));
			eTaskList.setDeleted(false);
			List<Task> tasks = syncManager.getAllTasksForTasklist(taskList);
			for (Task task : tasks) {
				entity.Task eTask = new entity.Task();
				eTask.setLastSynchOnline(new Timestamp(d.getTime()));
				eTask.setLastModification(new Timestamp(d.getTime()));
				eTask.setParentTask(task.getParent() == null? "" : task.getParent());
				eTask.setPosition(task.getPosition());
				eTask.setTitle(task.getTitle());
				eTask.setNotes(task.getNotes() == null? "" : task.getNotes());
				if (task.getStatus().equals("needsAction")) {
					eTask.setStatus(EStatus.needsAction);
				} else {
					eTask.setStatus(EStatus.completed);
				}
				eTask.setDue(new Timestamp(d.getTime()));
				eTask.setCompleted(new Timestamp(d.getTime()));
				eTask.setDeleted(false);
				eTask.setHidden(false);
				eTaskList.addTaskToList(eTask);
			}
		}
		Marshalling m = new Marshalling();
		m.SaveToXML(listOfTasklist);
	}

}
