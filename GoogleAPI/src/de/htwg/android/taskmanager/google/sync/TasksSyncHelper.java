package de.htwg.android.taskmanager.google.sync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;

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
import com.google.api.services.tasks.Tasks.TasksOperations.Insert;
import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.backend.binding.Binding;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;
import de.htwg.android.taskmanager.backend.util.EStatus;


public class TasksSyncHelper {

	/**
	 * The google task service.
	 */
	private Tasks service;
	private Binding binding;

	/**
	 * Creates the XML-representation for the current Google data set.
	 * 
	 * @throws Exception
	 *             throws an exception if something goes wrong, e.g. with the
	 *             authorization.
	 */
	public void createXMLforGoogleData() throws Exception {
		LocalTaskLists listOfTasklist = new LocalTaskLists();
		List<TaskList> taskLists = getAllTasklists();
		for (TaskList taskList : taskLists) {
			LocalTaskList eTaskList = taskListTransformation(taskList);
			listOfTasklist.addTaskList(eTaskList);
			List<Task> tasks = getAllTasksForTasklist(taskList);
			for (Task task : tasks) {
				LocalTask eTask = taskTransformation(task);
				eTaskList.addTaskToList(eTask);
			}
		}
		getBinding().marshall(listOfTasklist);
	}

	/**
	 * Deletes a task from the Google Task data set.
	 * 
	 * @param taskList
	 *            the task list in which this tasks is been saved
	 * @param task
	 *            the task which has to be deleted
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public void deleteTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().delete(taskList.getId(), task.getId()).execute();
	}

	/**
	 * Deletes a task list from the Google Task data set.
	 * 
	 * @param taskList
	 *            the task list which has to be deleted
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public void deleteTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().delete(taskList.getId()).execute();
	}

	/**
	 * Retrieves all task lists from the Google Task data set.
	 * 
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public List<TaskList> getAllTasklists() throws IOException {
		return getGoogleSyncManagerService().tasklists().list().execute().getItems();
	}

	/**
	 * Retrieves all tasks from the Google Task data set for a given task list.
	 * 
	 * @param taskList
	 *            the corresponding task list for the tasks
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public List<Task> getAllTasksForTasklist(TaskList taskList) throws IOException {
		return getAllTasksForTasklist(taskList, null);
	}

	/**
	 * Retrieves all tasks from the Google Task data set for a given task list.
	 * 
	 * @param taskList
	 *            the corresponding task list for the tasks
	 * @param lastUpdate
	 *            filter tasks. get tasks where update is bigger then the given
	 *            date.
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public List<Task> getAllTasksForTasklist(TaskList taskList, String lastUpdate) throws IOException {
		com.google.api.services.tasks.Tasks.TasksOperations.List tasksOperations = getGoogleSyncManagerService().tasks().list(
				taskList.getId());
		tasksOperations.setShowDeleted(true);
		tasksOperations.setShowHidden(true);
		tasksOperations.setUpdatedMin(lastUpdate);
		return tasksOperations.execute().getItems();
	}

	/**
	 * Returns a Singleton for Binding
	 * @return the binding object
	 */
	public Binding getBinding() {
		if(binding == null) {
			binding = new Binding();
		}
		return binding;
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

	/**
	 * Performs an authorization on the android phone.
	 * 
	 * @param httpTransport
	 *            HTTP transport protocol
	 * @param jsonFactory
	 *            JSON factory
	 * @return the HttpRequestInitializer, which is needed for authorize the
	 *         google sync manager service
	 */
	private HttpRequestInitializer getPhoneAccess(HttpTransport httpTransport, JacksonFactory jsonFactory) {
		return null;
	}

	/**
	 * Inserts a task list in a tasklist of the Google Task data set.
	 * 
	 * @param taskList
	 *            the task list in where the task will be inserted
	 * @param task
	 *            the task which has to be inserted
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public Task insertNewTask(TaskList taskList, LocalTask task) throws IOException {
		Insert insert = getGoogleSyncManagerService().tasks().insert(taskList.getId(), taskTransformation(task));
		Task newTask = insert.execute();
		return newTask;
	}

	/**
	 * Inserts a task list in the Google Task data set.
	 * 
	 * @param taskList
	 *            the task list which has to be inserted
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public void insertNewTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().insert(taskList).execute();
	}

	/**
	 * Reinitializes the google sync manager service.
	 */
	private Tasks reinitializeGoogleSyncManagerService() {
		this.service = null;
		return getGoogleSyncManagerService();
	}

	/**
	 * Transforms a local represented task list into a google represented task
	 * list
	 * 
	 * @param eTaskList
	 *            the local represented task list
	 * @return the Google represented task list
	 */
	public TaskList taskListTransformation(LocalTaskList eTaskList) {
		TaskList taskList = new TaskList();
//		taskList.setKind("tasks#taskList");
//		taskList.setId(eTaskList.getId());
//		taskList.setTitle(eTaskList.getTitle());
//		taskList.setUpdated(transformDateTime(eTaskList.getUpdate()));
		return taskList;
	}

	/**
	 * Transforms a Google represented task list into a local represented task
	 * list
	 * 
	 * @param taskList
	 *            the Google represented task list
	 * @return local represented task list
	 */
	public LocalTaskList taskListTransformation(TaskList taskList) {
		LocalTaskList eTaskList = new LocalTaskList();
//		eTaskList.setId(taskList.getId());
//		eTaskList.setTitle(taskList.getTitle());
//		eTaskList.setUpdate(transformDateTime(taskList.getUpdated()));
//		eTaskList.setDeleted(false);
		return eTaskList;
	}

	/**
	 * Transforms a local represented task list into a google represented task
	 * 
	 * @param eTask
	 *            the local represented task
	 * @return the Google represented task
	 */
	public Task taskTransformation(LocalTask eTask) {
		Task task = new Task();
//		task.setTitle(eTask.getTitle());
//		task.setNotes(eTask.getNotes());
//		switch (eTask.getStatus()) {
//		case NEEDS_ACTION:
//			task.setStatus("needsAction");
//			break;
//		case COMPLETED:
//			task.setStatus("completed");
//			task.setCompleted(transformDateTime(eTask.getCompleted()));
//			break;
//		default:
//			break;
//		}
//		task.setDue(transformDateTime(eTask.getDue()));
//		task.setDeleted(eTask.isDeleted());
//		task.setHidden(eTask.isHidden());
		return task;
	}

	/**
	 * Transforms a Google represented task into a local represented task
	 * 
	 * @param task
	 *            the Google represented task
	 * @return local represented task
	 */
	public LocalTask taskTransformation(Task task) {
		LocalTask eTask = new LocalTask();
//		eTask.setId(task.getId());
//		eTask.setLastModification(transformDateTime(task.getUpdated()));
//		eTask.setParentTask(task.getParent());
//		eTask.setPosition(task.getPosition());
//		eTask.setTitle(task.getTitle());
//		eTask.setNotes(task.getNotes());
//		if (task.getStatus().equals("needsAction")) {
//			eTask.setStatus(EStatus.NEEDS_ACTION);
//		} else {
//			eTask.setStatus(EStatus.COMPLETED);
//		}
//		eTask.setDue(transformDateTime(task.getDue()));
//		eTask.setCompleted(transformDateTime(task.getCompleted()));
//		eTask.setDeleted(task.getDeleted() == null ? false : task.getDeleted());
//		eTask.setHidden(task.getHidden() == null ? false : task.getHidden());
		return eTask;
	}

	/**
	 * Transforms a Google Date Time into a Timestamp for the local
	 * representation
	 * 
	 * @param d
	 *            the DateTime attribute (Google)
	 * @return null if datetime is null, otherwise the corresponding TimeStamp
	 *         attribute in DateTime format
	 * 
	 */
	private Timestamp transformDateTime(DateTime d) {
		return d != null ? new Timestamp(d.getValue()) : null;
	}

	/**
	 * Transforms a Timestamp into Google DateTime online storage.
	 * 
	 * @param t
	 *            the Timestamp attribute
	 * @return null if Timestamp is null, otherwise the corresponding DateTime
	 *         attribute in Timestamp format
	 */
	private DateTime transformDateTime(Timestamp t) {
		return t != null ? new DateTime(t.getTime()) : null;
	}

	/**
	 * Updates a task in the Google Task data set for a task list.
	 * 
	 * @param taskList
	 *            the task list on where the task has to be updated
	 * @param task
	 *            the task which has to be updated
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public void updateTask(TaskList taskList, Task task) throws IOException {
		getGoogleSyncManagerService().tasks().update(taskList.getId(), task.getId(), task).execute();
	}

	/**
	 * Updates a task list in the Google Task data set.
	 * 
	 * @param taskList
	 *            the task list which has to be updated
	 * @throws IOException
	 *             if the Google authorization fails.
	 */
	public void updateTaskList(TaskList taskList) throws IOException {
		getGoogleSyncManagerService().tasklists().update(taskList.getId(), taskList).execute();
	}
}
