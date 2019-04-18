package uk.co.syski.client.android.view.adapter.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.view.SystemOverviewActivity;

public class SystemListAdapter extends RecyclerView.Adapter<SystemListAdapter.SystemViewHolder> {

    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    List<SystemEntity> systemEntities;

    Activity context;

    public SystemListAdapter(Activity context) {
        this.context = context;
        this.systemEntities = new LinkedList<>();
        prefs = context.getSharedPreferences(context.getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        prefEditor = prefs.edit();
    }

    public void setData(List<SystemEntity> systemEntities)
    {
        this.systemEntities = systemEntities;
        notifyDataSetChanged();
    }

    public static class SystemViewHolder extends RecyclerView.ViewHolder {
        public View view;

        public SystemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
        }
    }

    @Override
    public SystemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_headed_value, parent, false);
        SystemViewHolder systemViewHolder = new SystemViewHolder(view);
        return systemViewHolder;
    }

    @Override
    public void onBindViewHolder(SystemViewHolder holder, final int position) {
        ImageView imageView = holder.view.findViewById(R.id.imageView);
        TextView headingView = holder.view.findViewById(R.id.headingView);
        TextView valueView = holder.view.findViewById(R.id.valueView);

        imageView.setImageResource(R.drawable.ic_pc);
        headingView.setText(R.string.txt_details_for);
        valueView.setText(systemEntities.get(position).HostName);

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSystemOverview(systemEntities.get(position).Id.toString());
            }
        });
    }

    private void openSystemOverview(String systemId) {
        Intent intent = new Intent(context,SystemOverviewActivity.class);
        prefEditor.putString(context.getString(R.string.preference_sysID_key),systemId);
        prefEditor.apply();
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return systemEntities.size();
    }
}
