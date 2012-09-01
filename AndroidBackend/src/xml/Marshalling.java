package xml;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import entity.ListOfTaskList;

public class Marshalling {

	public void SaveToXML(ListOfTaskList listOfTaskLists){
		Serializer serializer = new Persister();
		File result = new File("example.xml");

		try {
			serializer.write(listOfTaskLists, result);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
