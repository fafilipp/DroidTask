package de.htwg.android.taskmanager.google.asynctasks;

import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.LOG_TAG;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.MAX_WAIT_TIME;
import static de.htwg.android.taskmanager.util.constants.GoogleTaskConstants.MAX_WAIT_TIME_UNIT;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import android.accounts.Account;
import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.google.api.services.tasks.Tasks;
import com.google.api.services.tasks.model.TaskList;

import de.htwg.android.taskmanager.google.asynctasks.util.GoogleTaskAsyncTasksUtil;

public class GetTaskListAsyncTask extends AsyncTask<Void, Void, List<TaskList>> {

	private Activity activity;
	private Account account;

	public GetTaskListAsyncTask(Activity activity, Account account) {
		this.activity = activity;
		this.account = account;
	}

	@Override
	protected List<TaskList> doInBackground(Void... nothing) {
		List<TaskList> list = null;
		try {
			GoogleTaskAsyncTasksUtil asyncUtil = new GoogleTaskAsyncTasksUtil();
			Tasks service = asyncUtil.getTasksService(activity, account);
			Log.d(LOG_TAG, "getting all task lists");
			list = service.tasklists().list().execute().getItems();
			Log.d(LOG_TAG, "getting all task lists succeeded (size = " + list.size() + ")");
		} catch (Exception e) {
			Log.e(LOG_TAG, "exception thrown while getting all task lists = (" + e.getClass().getName() + ")" + e.getMessage());
		}
		return list;
	}

	public List<TaskList> getTaskLists() throws InterruptedException, ExecutionException, TimeoutException {
		AsyncTask<Void, Void, List<TaskList>> asyncTaskReturn = execute();
		return asyncTaskReturn.get(MAX_WAIT_TIME, MAX_WAIT_TIME_UNIT);
	}
}
