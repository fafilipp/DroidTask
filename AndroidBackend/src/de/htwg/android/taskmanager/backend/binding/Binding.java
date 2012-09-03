package de.htwg.android.taskmanager.backend.binding;

import java.io.File;
import java.util.Queue;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.htwg.android.taskmanager.backend.change.Change;
import de.htwg.android.taskmanager.backend.change.ChangeHistory;
import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;

public class Binding {

	private static final String DATA_XML = "data.xml"; 
	private static final String CHANGE_HISTORY_XML = "change_history.xml"; 
	
	public void marshall(LocalTaskLists listOfTaskLists) throws MarshallingException {
		Serializer serializer = new Persister();
		File dataXml = new File(DATA_XML);
		File changeHistoryXml = new File(CHANGE_HISTORY_XML);
		try {
			serializer.write(ChangeHistory.getChangeHistory(), changeHistoryXml);
			serializer.write(listOfTaskLists, dataXml);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarshallingException(e);
		}
	}

	public LocalTaskLists unmarshall() throws MarshallingException {
		Serializer serializer = new Persister();
		File dataXml = new File(DATA_XML);
		File changeHistoryXml = new File(CHANGE_HISTORY_XML);
		LocalTaskLists listOfTaskLists = null;
		Queue<Change> changeHistory = null;
		try {
			listOfTaskLists = serializer.read(LocalTaskLists.class, dataXml);
			changeHistory = serializer.read(Queue.class, changeHistoryXml);
		} catch (Exception e) {
			throw new MarshallingException(e);
		}
		ChangeHistory.resetChangeHistory(changeHistory);
		return listOfTaskLists;
	}

}
