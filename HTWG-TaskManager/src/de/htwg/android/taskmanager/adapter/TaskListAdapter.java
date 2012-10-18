package de.htwg.android.taskmanager.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import de.htwg.android.taskmanager.activity.R;
import de.htwg.android.taskmanager.backend.entity.LocalTask;
import de.htwg.android.taskmanager.backend.entity.LocalTaskList;

public class TaskListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<LocalTaskList> tasklists;

	/**
	 * Creates a TaskListAdapter
	 * 
	 * @param context
	 *            the context for this list adapter
	 * @param tasklists
	 *            the input data --> list of LocalTaskList
	 */
	public TaskListAdapter(Context context, List<LocalTaskList> tasklists) {
		this.context = context;
		this.tasklists = tasklists;
	}

	/**
	 * Gets a child (LocalTask) for an given group and childposition.
	 */
	public Object getChild(int groupPosition, int childPosition) {
		return tasklists.get(groupPosition).getTaskList().get(childPosition);
	}

	/**
	 * Returns the child position of the given group and child position
	 * (LocalTask).
	 */
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
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
	 * Returns the group position for a given group position.
	 */
	public long getGroupId(int groupPosition) {
		return groupPosition;
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
