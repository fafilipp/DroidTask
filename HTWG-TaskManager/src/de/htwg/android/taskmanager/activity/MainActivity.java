package de.htwg.android.taskmanager.activity;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.GOOGLE_ACCOUNT_TYPE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import de.htwg.android.taskmanager.activity.R;

import com.google.api.services.tasks.model.Task;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.AsyncTasksManager;
import de.htwg.android.taskmanager.google.asynctasks.DeleteTaskAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.DeleteTaskListAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.GetTaskAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.GetTaskListAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.InsertTaskAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.InsertTaskListAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.UpdateTaskAsyncTask;
import de.htwg.android.taskmanager.google.asynctasks.UpdateTaskListAsyncTask;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.codehaus.jackson.annotate.JsonTypeInfo.As;

import de.htwg.android.taskmanager.activity.R;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.GetTaskListAsyncTask;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;

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
				AsyncTasksManager googleManager = new AsyncTasksManager(this, accounts[0]);
				for(TaskList taskList : googleManager.getTaskLists()) {
					Log.i(LOG_TAG, taskList.getTitle());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			} catch (TimeoutException e) {
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
