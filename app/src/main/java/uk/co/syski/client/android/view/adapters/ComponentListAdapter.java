package uk.co.syski.client.android.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.model.HeadedValueModel;
import uk.co.syski.client.android.R;

public class ComponentListAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<HeadedValueModel> listItems;

    ImageView image;
    TextView heading, value;
    View listView;

    public ComponentListAdapter(Activity context, List<HeadedValueModel> listItems) {
        super(context, R.layout.component_overview_base, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        initViews();

        image.setImageResource(listItems.get(position).image);
        heading.setText(listItems.get(position).heading);
        value.setText(listItems.get(position).value);

        return listView;
    }

    public void initViews(){
        LayoutInflater inflater = context.getLayoutInflater();
        listView = inflater.inflate(R.layout.data_list_item, null, true);
        heading = listView.findViewById(R.id.heading);
        value = listView.findViewById(R.id.value);
        image = listView.findViewById(R.id.image);
    }
}

