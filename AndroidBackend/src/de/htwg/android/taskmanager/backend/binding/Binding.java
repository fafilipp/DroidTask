package de.htwg.android.taskmanager.backend.binding;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.htwg.android.taskmanager.backend.entity.ListOfTaskList;

public class Binding {

	public void marshall(ListOfTaskList listOfTaskLists) throws MarshallingException {
		Serializer serializer = new Persister();
		File result = new File("example.xml");
		try {
			serializer.write(listOfTaskLists, result);
		} catch (Exception e) {
			throw new MarshallingException(e);
		}
	}

	public ListOfTaskList unmarshall() throws MarshallingException {
		Serializer serializer = new Persister();
		File source = new File("example.xml");
		ListOfTaskList listOfTaskLists = null;
		try {
			listOfTaskLists = serializer.read(ListOfTaskList.class, source);
		} catch (Exception e) {
			throw new MarshallingException(e);
		}
		return listOfTaskLists;
	}

}
