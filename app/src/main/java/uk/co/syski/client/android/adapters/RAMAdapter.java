package uk.co.syski.client.android.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;

public class RAMAdapter extends ArrayAdapter{

    private final Activity context;

    //List Items
    private List<RAMEntity> listItems;

    //Views
    TextView model,manufacturer,type,size;
    View ramItem;

    public RAMAdapter(Activity context, List<RAMEntity> listItems){
        super(context, R.layout.component_list_item, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        initViews();

        model.setText(listItems.get(position).ModelName);
        manufacturer.setText(listItems.get(position).ManufacturerName);
        type.setText(listItems.get(position).MemoryTypeName);
        size.setText(""+listItems.get(position).MemoryBytes);

        return ramItem;
    }

    public void initViews(){
        LayoutInflater inflater = context.getLayoutInflater();
        ramItem = inflater.inflate(R.layout.ram_item, null, true);
        model = ramItem.findViewById(R.id.i_txtRAMModel);
        manufacturer = ramItem.findViewById(R.id.i_txtRAMMan);
        type = ramItem.findViewById(R.id.i_txtRAMType);
        size = ramItem.findViewById(R.id.i_txtRAMSize);
    }

}
