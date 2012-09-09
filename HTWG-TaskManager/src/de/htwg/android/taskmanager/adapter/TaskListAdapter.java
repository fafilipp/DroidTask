package de.htwg.android.taskmanager.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
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
		// TODO Auto-generated method stub
		return tasklists.get(groupPosition).getTaskList().get(childPosition);
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View view, ViewGroup parent) {
		// TODO Auto-generated method stub

		LocalTask localTask = tasklists.get(groupPosition).getTaskList()
				.get(childPosition);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.child_row, null);

		TextView tv_child_title = (TextView) view.findViewById(R.id.grp_child);
		tv_child_title.setText(localTask.getTitle());

		return view;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub

		return tasklists.get(groupPosition).getTaskList().size();
	}

	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return tasklists.get(groupPosition);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return tasklists.size();
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isLastGroup, View view,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		LocalTaskList tasklist = tasklists.get(groupPosition);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.group_row, null);

		TextView tv_group_title = (TextView) view.findViewById(R.id.grp_name);
		tv_group_title.setText(tasklist.getTitle());

		return view;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}
