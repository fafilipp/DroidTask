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

@SuppressWarnings("deprecation")
public class GoogleTaskApiUtil {

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
