package de.htwg.android.taskmanager.google.task.api.util;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;

/**
 * This class is necessary for the Google Tasks Api sync. It does nothing.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class GoogleTaskAccountManagerCallback implements AccountManagerCallback<Bundle> {

	/**
	 * Run-Method not implemented. Not necessary in this scope.
	 */
	public void run(AccountManagerFuture<Bundle> future) {
		// nothing here necessary...
	}

}
