package uk.co.syski.client.android.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import uk.co.syski.client.android.R;

public class SysListOverviewAdapter extends ArrayAdapter {

    //to reference the Activity
    private final Activity context;

    //to store images
    private final Integer[] imageIDarray;

    //List Items
    private String[] listItems;

    //Views
    ImageView compImg;
    TextView compName;
    TextView compDesc;
    View listItem;
    LinearLayout lin;

    public SysListOverviewAdapter(Activity context, Integer[] images, String[] listItems){
        super(context, R.layout.component_list_item, listItems);
        this.context = context;
        this.imageIDarray = images;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        initViews();

        compName.setText(listItems[position]);
        //TODO: Tweak when component descriptions implemented
        lin.removeView(compDesc);
        compImg.setImageResource(imageIDarray[position]);

        return listItem;
    }

    public void initViews(){
        LayoutInflater inflater = context.getLayoutInflater();
        listItem = inflater.inflate(R.layout.component_list_item, null, true);
        compImg = listItem.findViewById(R.id.compImg);
        compName = listItem.findViewById(R.id.compName);
        compDesc = listItem.findViewById(R.id.compDesc);
        lin = listItem.findViewById(R.id.linComp);
    }

}
