package de.htwg.android.taskmanager.google.task.api;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.test.ActivityInstrumentationTestCase2;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.activity.MainActivity;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

public class GoogleSyncManagerTest extends ActivityInstrumentationTestCase2<MainActivity> {

	private static final String NEW_TASKLIST_NAME = "TaskList for the GoogleSyncManagerTest";
	private static final String UPD_TASKLIST_NAME = "Updated TaskList for GoogleSyncManagerTest";
	private static final String NEW_TASK_NAME = "Task for the GoogleSyncManagerTest";
	private static final String UPD_TASK_NAME = "Updated Task for GoogleSyncManagerTest";

	public GoogleSyncManagerTest() {
		super(MainActivity.class);
	}

	private GoogleTaskApiManager apiManager;
	private GoogleSyncManager syncManager;
	private DatabaseHandler dbHandler;

	protected void setUp() throws Exception {
		super.setUp();
		AccountManager accountManager = AccountManager.get(getActivity());
		Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
		if (accounts.length == 0) {
			fail("No Google Account");
		} else {
			apiManager = new GoogleTaskApiManager(getActivity(), accounts[0]);
			syncManager = new GoogleSyncManager(getActivity(), accounts[0]);
			dbHandler = new DatabaseHandler(getActivity());
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void test_000_GoogleAccountExists() {
		AccountManager accountManager = AccountManager.get(getActivity());
		Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
		if (accounts.length == 0) {
			fail("No Google Account");
		} else {
			apiManager = new GoogleTaskApiManager(getActivity(), accounts[0]);
			syncManager = new GoogleSyncManager(getActivity(), accounts[0]);
			dbHandler = new DatabaseHandler(getActivity());
		}
	}

	public void test_101_AddTasklistForSync() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			TaskList taskList = apiManager.insertTaskList(NEW_TASKLIST_NAME);
			syncManager.execute();
			syncManager.get(1000, TimeUnit.DAYS);
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(taskList.getId());
			assertNotNull(localTaskList);
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	public void test_102_UpdateTaskListForSync() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			TaskList taskList = searchTaskList(NEW_TASKLIST_NAME);
			assertNotNull(taskList);
			taskList.setTitle(UPD_TASKLIST_NAME);
			taskList = apiManager.updateTaskList(taskList.getId(), taskList);
			syncManager.execute();
			syncManager.get(1000, TimeUnit.DAYS);
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(taskList.getId());
			assertEquals(localTaskList.getTitle(), taskList.getTitle());
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	public void test_201_AddTaskForSync() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			TaskList taskList = searchTaskList(UPD_TASKLIST_NAME);
			assertNotNull(taskList);
			Task task = new Task();
			task.setTitle(NEW_TASK_NAME);
			task = apiManager.insertTask(taskList.getId(), task);
			syncManager.execute();
			syncManager.get(1000, TimeUnit.DAYS);
			LocalTask localTask = dbHandler.getTaskByGoogleId(task.getId());
			assertNotNull(localTask);
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	public void test_202_UpdateTaskForSync() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			TaskList taskList = searchTaskList(UPD_TASKLIST_NAME);
			assertNotNull(taskList);
			Task task = searchTask(taskList.getId(), NEW_TASK_NAME);
			task.setTitle(UPD_TASK_NAME);
			task = apiManager.updateTask(taskList.getId(), task.getId(), task);
			syncManager.execute();
			syncManager.get(1000, TimeUnit.DAYS);
			LocalTask localTask = dbHandler.getTaskByGoogleId(task.getId());
			assertEquals(localTask.getTitle(), task.getTitle());
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	public void test_990_DeleteTask() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			TaskList taskList = searchTaskList(UPD_TASKLIST_NAME);
			assertNotNull(taskList);
			Task task = searchTask(taskList.getId(), UPD_TASK_NAME);
			assertNotNull(task);
			LocalTask localTask = dbHandler.getTaskByGoogleId(task.getId());
			dbHandler.deleteTaskFinal(localTask.getInternalId());
			apiManager.deleteTask(taskList.getId(), task.getId());
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	public void test_991_DeleteTaskList() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			TaskList taskList = searchTaskList(UPD_TASKLIST_NAME);
			assertNotNull(taskList);
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(taskList.getId());
			dbHandler.deleteTaskListFinal(localTaskList.getInternalId());
			apiManager.deleteTaskList(taskList.getId());
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	private TaskList searchTaskList(String title) throws GoogleSyncException {
		List<TaskList> listOftaskList = apiManager.getTaskLists();
		for (TaskList taskList : listOftaskList) {
			if (taskList.getTitle().equals(title)) {
				return taskList;
			}
		}
		return null;
	}

	private Task searchTask(String taskListId, String title) throws GoogleSyncException {
		List<Task> listOftask = apiManager.getTasks(taskListId);
		for (Task task : listOftask) {
			if (task.getTitle().equals(title)) {
				return task;
			}
		}
		return null;
	}

}
