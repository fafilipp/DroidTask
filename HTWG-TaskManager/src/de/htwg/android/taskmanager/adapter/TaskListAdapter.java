package de.htwg.android.taskmanager.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import de.htwg.android.taskmanager.activity.R;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;
import de.htwg.android.taskmanager.backend.util.EStatus;

/**
 * The task list adapter for the expandable list view. It fills the expandable
 * list views with information about task lists and tasks. The groups are
 * expandable and show the title of every task list. If the task list is
 * expanded the tasks in this task list will be showed as childs.
 * 
 * @author Filippelli, Gerhart, Gillet
 * 
 */
public class TaskListAdapter extends BaseExpandableListAdapter {

	/**
	 * The context (the activity) where this adapter is been used.
	 */
	private Context context;

	/**
	 * The list of local task lists which will be get from the database. Each
	 * task list contains a list of tasks.
	 */
	private List<LocalTaskList> tasklists;

	/**
	 * Creates a TaskListAdapter object
	 * 
	 * @param context
	 *            the context for this list adapter
	 * @param tasklists
	 *            the data --> list of LocalTaskList
	 */
	public TaskListAdapter(Context context, List<LocalTaskList> tasklists) {
		this.context = context;
		this.tasklists = tasklists;
	}

	/**
	 * Gets a child (LocalTask) for an given group and child position.
	 */
	public Object getChild(int groupPosition, int childPosition) {
		return tasklists.get(groupPosition).getTaskList().get(childPosition);
	}

	/**
	 * Returns the child internal id as int hashcode.
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return tasklists.get(groupPosition).getTaskList().get(childPosition).getInternalId().hashCode();
	}

	/**
	 * Returns the size of the tasks for a given task list position (group
	 * position).
	 */
	public int getChildrenCount(int groupPosition) {
		return tasklists.get(groupPosition).getTaskList().size();
	}

	/**
	 * Gets the View for an given group and child position. On the View the task
	 * title will be shown.
	 */
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		LocalTask localTask = tasklists.get(groupPosition).getTaskList().get(childPosition);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.child_row, null);
		TextView tvChildTitle = (TextView) view.findViewById(R.id.grp_child);
		tvChildTitle.setText(localTask.getTitle());
		if (localTask.getStatus().equals(EStatus.COMPLETED)) {
			tvChildTitle.setTextColor(Color.GREEN);
		} else if (localTask.getStatus().equals(EStatus.NEEDS_ACTION)) {
			if (localTask.getDue() != 0 && localTask.getDue() < System.currentTimeMillis()) {
				tvChildTitle.setTextColor(Color.RED);
			}
		}
		return view;
	}

	/**
	 * Gets the LocalTaskList object for a given group position.
	 */
	public Object getGroup(int groupPosition) {
		return tasklists.get(groupPosition);
	}

	/**
	 * Returns the size of the list of LocalTaskList
	 */
	public int getGroupCount() {
		return tasklists.size();
	}

	/**
	 * Returns the group internal id as int hashcode.
	 */
	public long getGroupId(int groupPosition) {
		return tasklists.get(groupPosition).getInternalId().hashCode();
	}

	/**
	 * Returns the View for a given group position. The View shows up the task
	 * list title.
	 */
	public View getGroupView(int groupPosition, boolean isLastGroup, View view, ViewGroup parent) {
		LocalTaskList tasklist = tasklists.get(groupPosition);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.group_row, null);
		TextView tvGroupTitle = (TextView) view.findViewById(R.id.grp_name);
		tvGroupTitle.setText(tasklist.getTitle());
		return view;
	}

	/**
	 * Returns always false, because this list doesn't have stable ids.
	 */
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * Returns true, because the childs (the tasks) should be selectable.
	 */
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
