package de.htwg.android.taskmanager.google.task.api;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
import de.htwg.android.taskmanager.google.task.api.util.GoogleSyncException;

public class GoogleSyncManager extends AsyncTask<Void, Void, Void> {

	private Activity activity;
	private Account account;
	
	public GoogleSyncManager(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected Void doInBackground(Void... params) {
		DatabaseHandler dbHandler = new DatabaseHandler(activity);
		GoogleTaskApiManager apiManager = new GoogleTaskApiManager(activity, account);
		
		try {
			apiManager.getTaskLists();
		} catch (GoogleSyncException googleSyncException) {
			
		}
		return null;
	}

}
