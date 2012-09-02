package de.htwg.android.taskmanager.google.sync.main;

import de.htwg.android.taskmanager.google.sync.TasksSyncHelper;

public class Main {

	public static void main(String[] args) throws Exception {
		TasksSyncHelper syncManager = new TasksSyncHelper();
		syncManager.createXMLforGoogleData();
	}

}
