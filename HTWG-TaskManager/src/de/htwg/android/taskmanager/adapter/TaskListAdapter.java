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

	public TaskListAdapter(Context context, List<LocalTaskList> tasklists) {
		this.context = context;
		this.tasklists = tasklists;
	}

	public Object getChild(int groupPosition, int childPosition) {
		return tasklists.get(groupPosition).getTaskList().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public int getChildrenCount(int groupPosition) {
		return tasklists.get(groupPosition).getTaskList().size();
	}

	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
		LocalTask localTask = tasklists.get(groupPosition).getTaskList().get(childPosition);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.child_row, null);
		TextView tvChildTitle = (TextView) view.findViewById(R.id.grp_child);
		tvChildTitle.setText(localTask.getTitle());
		return view;
	}

	public Object getGroup(int groupPosition) {
		return tasklists.get(groupPosition);
	}

	public int getGroupCount() {
		return tasklists.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastGroup, View view, ViewGroup parent) {
		LocalTaskList tasklist = tasklists.get(groupPosition);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.group_row, null);
		TextView tvGroupTitle = (TextView) view.findViewById(R.id.grp_name);
		tvGroupTitle.setText(tasklist.getTitle());
		return view;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
