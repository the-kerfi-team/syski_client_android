package uk.co.syski.client.android.view.adapter.listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.model.HeadedValueModel;

public class HeadedValueListAdapter extends ArrayAdapter {

    private final Activity context;
    private List<HeadedValueModel> listItems;

    ImageView image;
    TextView heading, value;
    View listView;

    public HeadedValueListAdapter(Activity context, List<HeadedValueModel> listItems) {
        super(context, R.layout.fragment_overview, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    public void setData(List<HeadedValueModel> listItems)
    {
        this.listItems = listItems;
        clear();
        addAll(listItems);
        notifyDataSetChanged();
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
        listView = inflater.inflate(R.layout.fragment_headed_value, null, true);
        image = listView.findViewById(R.id.imageView);
        heading = listView.findViewById(R.id.headingView);
        value = listView.findViewById(R.id.valueView);
    }

}
