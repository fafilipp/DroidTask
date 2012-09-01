package xml;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import entity.ListOfTaskList;

public class Unmarshalling {

	public ListOfTaskList getFromXML() {
		Serializer serializer = new Persister();
		File source = new File("example.xml");
		ListOfTaskList listOfTaskLists = null;
		try {
			listOfTaskLists = serializer.read(ListOfTaskList.class, source);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfTaskLists;
	}
	
}
