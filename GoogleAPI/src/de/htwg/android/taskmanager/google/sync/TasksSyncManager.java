package de.htwg.android.taskmanager.google.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

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

	public void deleteTask(TaskList taskList, Task task)  throws IOException {
		getGoogleSyncManagerService().tasks().delete(taskList.getId(), task.getId()).execute();
	}

	public static void main(String[] args) throws IOException {
		TasksSyncManager syncManager = new TasksSyncManager();
		List<TaskList> taskLists = syncManager.getAllTasklists();
		for (TaskList taskList : taskLists) {
			System.out.println(taskList.getTitle());
			List<Task> tasks = syncManager.getAllTasksForTasklist(taskList);
			for (Task task : tasks) {
				System.out.println(task.getTitle());
			}
			System.out.println();
		}
	}

}
