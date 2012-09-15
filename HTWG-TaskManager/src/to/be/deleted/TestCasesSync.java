//package to.be.deleted;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import com.google.api.services.tasks.model.TaskList;
//
//import de.htwg.android.taskmanager.backend.database.DatabaseHandler;
//import de.htwg.android.taskmanager.backend.entity.LocalTask;
//import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
//import de.htwg.android.taskmanager.backend.util.EStatus;
//import de.htwg.android.taskmanager.google.sync.EntitiyTransformator;
//
//public class TestCasesSync {
//
//	private EntitiyTransformator transformator;
//	
//	public TestCasesSync(DatabaseHandler dbHandler) {
//		transformator = new EntitiyTransformator(dbHandler);
//	}
//	
////	private List<LocalTaskList> createDummyData() {
////		int taskLists = 3;
////		int tasks = 8;
////		List<TaskList> remoteDummy = new ArrayList<TaskList>();
////		List<LocalTaskList> localDummy = new ArrayList<LocalTaskList>();
////		for(int i = 0; i < taskLists; i++) {
////			LocalTaskList localTaskList = new LocalTaskList();
////			localTaskList.setTitle("Tasklist " + (i+1));
////			for(int j = 0; j < tasks; j++) {
////				LocalTask localTask = new LocalTask();
////				localTask.setTitle("Task " + (j+1));
////				localTask.setStatus(EStatus.NEEDS_ACTION);
////				localTask.setLastModification(new Date().getTime());
////				localTask.setDue(new Date().getTime() + 360000);
////				localTask.setNotes("Keine Notes zu " + localTask.getTitle());
////				localTaskList.addTaskToList(localTask);
////			}
////			localDummy.add(localTaskList);
////		}
////		
////		return localDummy;
////	}
//
//	private void localNewTaskList() {
//
//	}
//
//	private void localNewTask() {
//
//	}
//
//	private void localUpdateTaskList() {
//
//	}
//
//	private void localUpdateTask() {
//
//	}
//
//	private void localDeleteTaskList() {
//
//	}
//
//	private void localDeleteTask() {
//
//	}
//
//}
