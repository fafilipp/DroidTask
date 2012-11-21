package de.htwg.android.taskmanager.google.task.api.util;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.SERVER_API_KEY;

import java.io.IOException;

import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.services.tasks.TasksRequest;

/**
 * This class initializes a Json Http Request to the Google Tasks Api.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class GoogleTaskJsonHttpRequestInitializer implements JsonHttpRequestInitializer {

	/**
	 * It initializes the given request setting the Server Api Key.
	 */
	public void initialize(JsonHttpRequest request) throws IOException {
		final TasksRequest tasksRequest = (TasksRequest) request;
		tasksRequest.setKey(SERVER_API_KEY);
	}

}
