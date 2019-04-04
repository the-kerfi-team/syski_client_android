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
import uk.co.syski.client.android.model.viewmodel.SystemCPUModel;

public class CPUAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<SystemCPUModel> cpuModelEntities;

    public CPUAdapter(Activity context) {
        this.context = context;
        this.cpuModelEntities = new LinkedList<>();
    }

    public void setData(List<SystemCPUModel> cpuModelEntities) {
        this.cpuModelEntities = cpuModelEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return cpuModelEntities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 4;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cpuModelEntities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cpuModelEntities.get(groupPosition);
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

        imageView.setImageResource(R.drawable.ic_cpu);
        firstHeadingView.setText("Model");
        firstValueView.setText(cpuModelEntities.get(groupPosition).getModelName());
        secondHeadingView.setText("Manufacturer");
        secondValueView.setText(cpuModelEntities.get(groupPosition).getManufacturerName());

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
                imageView.setImageResource(R.drawable.ic_architecture);
                headingView.setText("Architecture");
                valueView.setText(cpuModelEntities.get(groupPosition).getArchitectureName());
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_clock);
                headingView.setText("Clock Speed");
                valueView.setText(cpuModelEntities.get(groupPosition).getClockSpeedAsString());
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_core);
                headingView.setText("Core Count");
                valueView.setText(cpuModelEntities.get(groupPosition).getCoreCountAsString());
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_thread);
                headingView.setText("Thread Count");
                valueView.setText(cpuModelEntities.get(groupPosition).getThreadCountAsString());
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}