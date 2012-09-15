package to.be.deleted;

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

import de.htwg.android.taskmanager.google.task.api.IGoogleTaskApiManager;
import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

public class LocalTaskManager implements IGoogleTaskApiManager {

	private Tasks service;

	public void deleteTask(String taskListId, String taskId) throws GoogleSyncException {
		try {
			getGoogleSyncManagerService().tasks().delete(taskListId, taskId).execute();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	public void deleteTaskList(String taskListId) throws GoogleSyncException {
		try {
			getGoogleSyncManagerService().tasklists().delete(taskListId).execute();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	/**
	 * Gets the Google Sync Manager Service, with the google authorization.
	 * (Implemented as single ton, for reinitialize call
	 * "reinitializeGoogleSyncManagerService".
	 */
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

	/**
	 * Performs an local authorization. The user has to copy and paste the link
	 * in the console and enter the code google provides.
	 * 
	 * @param httpTransport
	 *            HTTP transport protocol
	 * @param jsonFactory
	 *            JSON factory
	 * @return the HttpRequestInitializer, which is needed for authorize the
	 *         google sync manager service
	 */
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

	public List<TaskList> getTaskLists() throws GoogleSyncException {
		try {
			return getGoogleSyncManagerService().tasklists().list().execute().getItems();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	public List<Task> getTasks(String taskListId) throws GoogleSyncException {
		try {
			return getGoogleSyncManagerService().tasks().list(taskListId).execute().getItems();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	public Task insertTask(String taskListId, Task task) throws GoogleSyncException {
		try {
			return getGoogleSyncManagerService().tasks().insert(taskListId, task).execute();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	public TaskList insertTaskList(String title) throws GoogleSyncException {
		try {
			TaskList taskList = new TaskList();
			taskList.setTitle(title);
			return getGoogleSyncManagerService().tasklists().insert(taskList).execute();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	public Task updateTask(String taskListId, String taskId, Task task) throws GoogleSyncException {
		try {
			return getGoogleSyncManagerService().tasks().update(taskListId, taskId, task).execute();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

	public TaskList updateTaskList(String taskListId, TaskList taskList) throws GoogleSyncException {
		try {
			return getGoogleSyncManagerService().tasklists().update(taskListId, taskList).execute();
		} catch (IOException e) {
			throw new GoogleSyncException(e);
		}
	}

}
