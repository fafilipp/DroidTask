package de.htwg.android.taskmanager.google.asynctasks.util;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.SERVER_API_KEY;

import java.io.IOException;

import com.google.api.client.http.json.JsonHttpRequest;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.services.tasks.TasksRequest;

public class GoogleTaskJsonHttpRequestInitializer implements JsonHttpRequestInitializer {

	public void initialize(JsonHttpRequest request) throws IOException {
		final TasksRequest tasksRequest = (TasksRequest) request;
		tasksRequest.setKey(SERVER_API_KEY);
	}

}
