package uk.co.syski.client.android;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SysListAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store images
    private final Integer[] imageIDarray;

    //to store the list of names
    private final String[] nameArray;

    //to store the list of info
    private final String[] infoArray;

    public SysListAdapter(Activity context, String[] sysNames, String[] hostnames, Integer[] images){

        super(context,R.layout.list_item , sysNames);

        this.context=context;
        this.imageIDarray = images;
        this.nameArray = sysNames;
        this.infoArray = hostnames;

    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);

        //gets references to objects in the list item file
        TextView sysNameTextField = (TextView) rowView.findViewById(R.id.sysName);
        TextView hostTextField = (TextView) rowView.findViewById(R.id.sysHost);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.sysImg);

        //sets display values from arrays
        sysNameTextField.setText(nameArray[position]);
        hostTextField.setText(infoArray[position]);
        imageView.setImageResource(imageIDarray[position]);

        return rowView;
    }
}
