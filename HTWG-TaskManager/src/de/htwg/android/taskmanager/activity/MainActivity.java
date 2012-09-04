package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.AsyncTasksManager;
import de.htwg.android.taskmanager.google.sync.GoogleSyncException;
import de.htwg.android.taskmanager.google.sync.IGoogleTaskManager;

public class MainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		AccountManager accountManager = AccountManager.get(this);
		Account[] accounts = accountManager.getAccountsByType(GOOGLE_ACCOUNT_TYPE);
		Log.d(LOG_TAG, "amount of accounts = " + accounts.length);
		if (accounts.length < 1) {
			Log.i(LOG_TAG, "no google accounts found");
		} else {
			Log.d(LOG_TAG, "account name = " + accounts[0].name);
			try {
				IGoogleTaskManager taskManager = new AsyncTasksManager(this, accounts[0]);
				for (TaskList taskList : taskManager.getTaskLists()) {
					Log.i(LOG_TAG, taskList.getTitle());
				}
			} catch (GoogleSyncException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
}
