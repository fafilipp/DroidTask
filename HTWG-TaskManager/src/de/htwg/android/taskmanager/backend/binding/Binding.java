package de.htwg.android.taskmanager.backend.binding;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.DATA_XML_PATH;

import java.io.File;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;

public class Binding {

	/**
	 * Marshalls a LocalTaskLists Object in XML-format with the usage of the
	 * simple-xml library.
	 * 
	 * @param listOfTaskLists
	 *            the object which should be marshalled
	 * @throws MarshallingException
	 *             if something goes wrong while marshalling (object not
	 *             consistent or file not writable).
	 */
	public void marshall(LocalTaskLists listOfTaskLists) throws MarshallingException {
		Serializer serializer = new Persister();
		File dataXml = new File(DATA_XML_PATH);
		try {
			serializer.write(listOfTaskLists, dataXml);
		} catch (Exception e) {
			e.printStackTrace();
			throw new MarshallingException(e);
		}
	}

	/**
	 * Unmarshalls a LocalTaskLists Object from an XML-file with the usage of
	 * the simple-xml library.
	 * 
	 * @return the readed listOfTaskLists object
	 * @throws MarshallingException
	 *             if something goes wrong while unmarshalling (xml not valid/wf
	 *             or file not readable).
	 */
	public LocalTaskLists unmarshall() throws MarshallingException {
		Serializer serializer = new Persister();
		File dataXml = new File(DATA_XML_PATH);
		LocalTaskLists listOfTaskLists = null;
		try {
			listOfTaskLists = serializer.read(LocalTaskLists.class, dataXml);
		} catch (Exception e) {
			throw new MarshallingException(e);
		}
		return listOfTaskLists;
	}

}
