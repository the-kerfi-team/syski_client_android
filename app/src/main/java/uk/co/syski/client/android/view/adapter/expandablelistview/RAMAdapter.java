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
import uk.co.syski.client.android.model.viewmodel.SystemRAMModel;

public class RAMAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private List<SystemRAMModel> ramModelEntities;

    public RAMAdapter(Activity context) {
        this.context = context;
        this.ramModelEntities = new LinkedList<>();
    }

    public void setData(List<SystemRAMModel> ramModelEntities) {
        this.ramModelEntities = ramModelEntities;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return ramModelEntities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return ramModelEntities.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return ramModelEntities.get(groupPosition);
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

        imageView.setImageResource(R.drawable.ram_icon);
        firstHeadingView.setText(R.string.txtModel);
        firstValueView.setText(ramModelEntities.get(groupPosition).getModelName());
        secondHeadingView.setText(R.string.txtManufacturer);
        secondValueView.setText(ramModelEntities.get(groupPosition).getManufacturerName());

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
        headingView.setText(R.string.txtSize);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        valueView.setText(ramModelEntities.get(groupPosition).getMemoryBytesAsString(sp.getString("pref_general_ram_unit", context.getString(R.string.pref_general_ram_unit_default))));

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}