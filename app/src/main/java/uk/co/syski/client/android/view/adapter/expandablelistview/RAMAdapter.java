package uk.co.syski.client.android.view.adapter.expandablelistview;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;

public class RAMAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<RAMEntity> ramEntities;

    public RAMAdapter(Activity context, List<RAMEntity> ramEntities) {
        this.context = context;
        this.ramEntities = ramEntities;
    }

    @Override
    public int getGroupCount() {
        return ramEntities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ramEntities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ramEntities.get(groupPosition);
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
        firstValueView.setText(ramEntities.get(groupPosition).ModelName);
        secondHeadingView.setText("Manufacturer");
        secondValueView.setText(ramEntities.get(groupPosition).ManufacturerName);

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

        imageView.setImageResource(R.drawable.ic_memory_size);
        headingView.setText("Size");
        valueView.setText(formatBytes(ramEntities.get(groupPosition).MemoryBytes));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public String formatBytes(long bytes) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String ramUnits = sp.getString("pref_general_ram_unit", context.getString(R.string.pref_general_storage_unit));
        switch (ramUnits) {
            case "TB":
                bytes /= 1024;
            case "GB":
                bytes /= 1024;
            case "MB":
                bytes /= 1024;
            case "KB":
                bytes /= 1024;
                break;
            default:
                return bytes + " B";
        }

        return bytes + " " + ramUnits;
    }
}