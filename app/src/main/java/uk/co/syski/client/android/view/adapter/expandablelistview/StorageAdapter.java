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

import java.util.LinkedList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemStorageModel;

public class StorageAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<SystemStorageModel> storageModelEntities;

    public StorageAdapter(Activity context) {
        this.context = context;
        this.storageModelEntities = new LinkedList<>();
    }

    public void setData(List<SystemStorageModel> storageModelEntities) {
        this.storageModelEntities = storageModelEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return storageModelEntities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return storageModelEntities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return storageModelEntities.get(groupPosition);
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

        imageView.setImageResource(R.drawable.storage_icon);
        firstHeadingView.setText("Model");
        firstValueView.setText(storageModelEntities.get(groupPosition).getModelName());
        secondHeadingView.setText("Manufacturer");
        secondValueView.setText(storageModelEntities.get(groupPosition).getManufacturerName());

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

        imageView.setImageResource(R.drawable.memory_size_icon);
        headingView.setText("Size");
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        valueView.setText(storageModelEntities.get(groupPosition).getMemoryBytesAsString(sp.getString("pref_general_storage_unit", context.getString(R.string.pref_general_storage_unit_default))));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
