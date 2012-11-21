package de.htwg.android.taskmanager.google.task.api.util;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.APPLICATION_NAME;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.AUTH_TOKEN_TYPE;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.api.client.auth.oauth2.draft10.AccessProtectedResource;
import com.google.api.client.extensions.android2.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessProtectedResource;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.tasks.Tasks;

/**
 * This class is necessary to get the Google Tasks Api object.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
@SuppressWarnings("deprecation")
public class GoogleTaskApiUtil {

	/**
	 * Gets the Google Task Api Service to perform online operations.
	 * 
	 * @param activity
	 *            the activity which is performing the sync.
	 * @param account
	 *            the Google Account which should be authentificated.
	 * @return the Google Tasks Api service
	 * @throws OperationCanceledException
	 *             Exception if the operation is been cancelled.
	 * @throws IOException
	 *             Exception if problems while input or output operation
	 *             communicating with the server.
	 * @throws AuthenticatorException
	 *             Exception if no authentification is possible.
	 */
	public Tasks getTasksService(Activity activity, Account account) throws OperationCanceledException, IOException, AuthenticatorException {
		AccountManager accountManager = AccountManager.get(activity);
		AccountManagerFuture<Bundle> amFuture = accountManager.getAuthToken(account, AUTH_TOKEN_TYPE, null, activity,
				new GoogleTaskAccountManagerCallback(), null);
		String token = amFuture.getResult().getString(AccountManager.KEY_AUTHTOKEN);
		Log.d(LOG_TAG, "token == null -> " + (token == null));
		HttpTransport transport = AndroidHttp.newCompatibleTransport();
		AccessProtectedResource accessProtectedResource = new GoogleAccessProtectedResource(token);
		Tasks.Builder builder = new Tasks.Builder(transport, new JacksonFactory(), accessProtectedResource);
		builder.setApplicationName(APPLICATION_NAME);
		builder.setJsonHttpRequestInitializer(new GoogleTaskJsonHttpRequestInitializer());
		Tasks service = builder.build();
		return service;
	}

}
