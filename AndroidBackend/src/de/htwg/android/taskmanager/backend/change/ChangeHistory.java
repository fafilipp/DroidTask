package de.htwg.android.taskmanager.backend.change;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.simpleframework.xml.ElementList;

public class ChangeHistory {

	@ElementList
	private static Queue<Change> changeHistory;

	public static Queue<Change> getChangeHistory() {
		if (changeHistory == null) {
			changeHistory = new LinkedBlockingQueue<Change>();
		}
		return changeHistory;
	}
	
	public static void resetChangeHistory(Queue<Change> changeHistory) {
		ChangeHistory.changeHistory = changeHistory;
	}

}
