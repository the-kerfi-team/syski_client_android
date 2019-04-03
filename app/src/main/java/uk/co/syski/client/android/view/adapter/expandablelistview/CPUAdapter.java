package uk.co.syski.client.android.view.adapter.expandablelistview;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;

public class CPUAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<CPUEntity> cpuEntities;

    public CPUAdapter(Activity context, List<CPUEntity> cpuEntities) {
        this.context = context;
        this.cpuEntities = cpuEntities;
    }

    @Override
    public int getGroupCount() {
        return cpuEntities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 4;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return cpuEntities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return cpuEntities.get(groupPosition);
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
        firstValueView.setText(cpuEntities.get(groupPosition).ModelName);
        secondHeadingView.setText("Manufacturer");
        secondValueView.setText(cpuEntities.get(groupPosition).ManufacturerName);

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
                valueView.setText(cpuEntities.get(groupPosition).ArchitectureName);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_clock);
                headingView.setText("Clock Speed");
                valueView.setText(Integer.toString(cpuEntities.get(groupPosition).ClockSpeed));
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_core);
                headingView.setText("Core Count");
                valueView.setText(Integer.toString(cpuEntities.get(groupPosition).CoreCount));
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_thread);
                headingView.setText("Thread Count");
                valueView.setText(Integer.toString(cpuEntities.get(groupPosition).ThreadCount));
                break;
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
