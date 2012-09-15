//package to.be.deleted;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//import de.htwg.android.taskmanager.backend.binding.Binding;
//import de.htwg.android.taskmanager.backend.binding.MarshallingException;
//import de.htwg.android.taskmanager.backend.entity.LocalTask;
//import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
//import de.htwg.android.taskmanager.backend.entity.LocalTaskLists;
//import de.htwg.android.taskmanager.backend.util.EStatus;
//
//public class BackendMain {
//
//	/**
//	 * @param args
//	 * @throws MarshallingException
//	 */
//	public static void main(String[] args) throws MarshallingException {
//
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//		java.util.Date d = null;
//		try {
//			d = sdf.parse("2000-12-13T14:00:00Z");
//		} catch (ParseException e1) {
//			e1.printStackTrace();
//		}
//
//		LocalTask task = new LocalTask();
//		task.setTitle("Test");
//		task.setNotes("Keine Notes");
//		task.setStatus(EStatus.COMPLETED);
//
//		LocalTask task1 = new LocalTask();
//		task1.setTitle("Test");
//		task1.setNotes("Keine Notes");
//		task1.setStatus(EStatus.COMPLETED);
//
//		LocalTaskList taskList = new LocalTaskList();
//		taskList.setTitle("TaskList 1");
//		taskList.addTaskToList(task);
//		taskList.addTaskToList(task1);
//
//		LocalTaskLists lotl = new LocalTaskLists();
//		lotl.addTaskList(taskList);
//
//		Binding binding = new Binding();
//		binding.marshall(lotl);
//
//		LocalTaskLists lotl_uma = binding.unmarshall();
//
//		for (LocalTaskList tasklist_read : lotl_uma.getListOfTaskList()) {
//			System.out.println(tasklist_read.getInternalId());
//			for (LocalTask task_read : tasklist_read.getTaskList()) {
//				System.out.println(task_read.getInternalId());
//			}
//		}
//	}
//}
