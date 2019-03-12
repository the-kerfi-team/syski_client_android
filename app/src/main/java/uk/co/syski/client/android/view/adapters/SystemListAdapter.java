package uk.co.syski.client.android.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.SystemEntity;

/**
 * SystemListAdapter - An ArrayAdapter class used for listing systems from an array in the system list
 */
public class SystemListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store images
    private final Integer image;

    private List<SystemEntity> mSystemEntityList;

    public SystemListAdapter(Activity context, Integer images, List<SystemEntity> systemEntityList)
    {
        super(context, R.layout.list_item, systemEntityList);

        this.context=context;
        this.image = images;
        mSystemEntityList = systemEntityList;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        //gets references to objects in the list item file
        TextView sysNameTextField = rowView.findViewById(R.id.sysName);
        TextView hostTextField = rowView.findViewById(R.id.sysHost);
        ImageView imageView = rowView.findViewById(R.id.sysImg);

        //sets display values from arrays
        sysNameTextField.setText(mSystemEntityList.get(position).HostName);
        hostTextField.setText(mSystemEntityList.get(position).HostName);
        imageView.setImageResource(image);

        return rowView;
    }
}