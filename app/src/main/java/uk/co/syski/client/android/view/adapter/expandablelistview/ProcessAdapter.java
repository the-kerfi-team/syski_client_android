package uk.co.syski.client.android.view.adapter.expandablelistview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemProcessModel;

public class ProcessAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<SystemProcessModel> processModelList;

    public ProcessAdapter(Activity context) {
        this.context = context;
        this.processModelList = new LinkedList<>();
    }

    public void setData(List<SystemProcessModel> processModelList) {
        this.processModelList = processModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return processModelList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 4;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return processModelList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return processModelList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(R.layout.fragment_double_headed_value, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView firstHeadingView = convertView.findViewById(R.id.firstHeadingView);
        TextView firstValueView = convertView.findViewById(R.id.firstValueView);
        TextView secondHeadingView = convertView.findViewById(R.id.secondHeadingView);
        TextView secondValueView = convertView.findViewById(R.id.secondValueView);

        imageView.setImageResource(R.drawable.placeholder);
        firstHeadingView.setText("Name");
        firstValueView.setText("");
        secondHeadingView.setText("Path");
        secondValueView.setText("");

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpandableListView listView = (ExpandableListView) parent;
                if (!isExpanded) {
                    listView.expandGroup(groupPosition);
                } else {
                    listView.collapseGroup(groupPosition);
                }
            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = context.getLayoutInflater().inflate(R.layout.fragment_headed_value, parent, false);

        ImageView imageView = convertView.findViewById(R.id.imageView);
        TextView headingView = convertView.findViewById(R.id.headingView);
        TextView valueView = convertView.findViewById(R.id.valueView);

        switch (childPosition) {
            case 0:
                imageView.setImageResource(R.drawable.placeholder);
                headingView.setText("Memory Size");
                valueView.setText("");
                break;
            case 1:
                imageView.setImageResource(R.drawable.placeholder);
                headingView.setText("Kernel Time");
                valueView.setText("");
                break;
            case 2:
                imageView.setImageResource(R.drawable.placeholder);
                headingView.setText("Threads");
                valueView.setText("");
                break;
            case 3:
                imageView.setImageResource(R.drawable.placeholder);
                headingView.setText("Up Time");
                valueView.setText("");
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
