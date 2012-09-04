package de.htwg.android.taskmanager.util.constants;

import java.util.concurrent.TimeUnit;

public interface GoogleTaskConstants {

	String DATA_XML_PATH = "data.xml"; 
	String AUTH_TOKEN_TYPE = "Manage your tasks";
	String GOOGLE_ACCOUNT_TYPE = "com.google";
	String SERVER_API_KEY = "AIzaSyALNwJA_3Pm3KfsCjZhgzsJXx85GlKYmAI";
	String APPLICATION_NAME = "HTWG-Konstanz - Task Manager";
	String LOG_TAG = "taskmanager-async-task";
	
	long MAX_WAIT_TIME = 30;
	TimeUnit MAX_WAIT_TIME_UNIT = TimeUnit.SECONDS;
	
}
