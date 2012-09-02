package de.htwg.android.taskmanager.google.sync.main;

import de.htwg.android.taskmanager.google.sync.TasksSyncHelper;

public class Main {

	public static void main(String[] args) throws Exception {
		TasksSyncHelper syncManager = new TasksSyncHelper();
		syncManager.createXMLforGoogleData();
		
//		// Timestamps
//		Timestamp t1 = new Timestamp(2012, 8, 12, 8, 0, 0, 0);
//		Timestamp t2 = new Timestamp(2012, 8, 12, 8, 0, 0, 0);
//		System.out.println(t1.after(null));
	}

}
