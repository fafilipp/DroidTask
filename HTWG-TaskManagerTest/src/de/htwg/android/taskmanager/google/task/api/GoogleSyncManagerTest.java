package de.htwg.android.taskmanager.google.task.api;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.google.api.services.tasks.model.TaskList;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.test.ActivityInstrumentationTestCase2;
import de.htwg.android.taskmanager.activity.MainActivity;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

public class GoogleSyncManagerTest extends ActivityInstrumentationTestCase2<MainActivity> {

	public GoogleSyncManagerTest() {
		super(MainActivity.class);
	}

	private GoogleTaskApiManager apiManager;
	private GoogleSyncManager syncManager;
	private DatabaseHandler dbHandler;
	private TaskList lastTaskList;
	
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
	
	public void test_010_AddTasklistForSync() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			lastTaskList = apiManager.insertTaskList("Tasklist for Testcase");
			syncManager.execute();
			syncManager.get(1000, TimeUnit.DAYS);
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(lastTaskList.getId());
			assertNotNull(localTaskList);
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

	public void test_011_UpdateTaskListForSync() throws InterruptedException, ExecutionException, TimeoutException {
		try {
			lastTaskList.setTitle("Updated Tasklist for Testcase");
			lastTaskList = apiManager.updateTaskList(lastTaskList.getId(), lastTaskList);
			syncManager.execute();
			syncManager.get(1000, TimeUnit.DAYS);
			LocalTaskList localTaskList = dbHandler.getTaskListByGoogleId(lastTaskList.getId());
			assertEquals(localTaskList.getTitle(), lastTaskList.getTitle());
		} catch (GoogleSyncException e) {
			fail("No remote connection.");
		}
	}

}
