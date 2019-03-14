package uk.co.syski.client.android.view.adapters;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.model.HeadedValueModel;

public class RAMAdapter extends ArrayAdapter{

    private final Activity context;

    //List Items
    private List<RAMEntity> listItems;

    //Views
    ImageView image;
    TextView model,manufacturer;
    ListView listView;
    View ramItem;

    public RAMAdapter(Activity context, List<RAMEntity> listItems){
        super(context, R.layout.component_list_item, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        initViews();

        image.setImageResource(R.drawable.ic_gpu);

        model.setText(listItems.get(position).ModelName);
        manufacturer.setText(listItems.get(position).ManufacturerName);

        HeadedValueModel size = new HeadedValueModel("Size", formatBytes(listItems.get(position).MemoryBytes), R.drawable.ic_memory_size);

        List<HeadedValueModel> dataValues = new ArrayList<>();
        dataValues.add(size);

        listView.setAdapter(new ComponentListAdapter(context, dataValues));

        return ramItem;
    }

    public void initViews(){
        LayoutInflater inflater = context.getLayoutInflater();
        ramItem = inflater.inflate(R.layout.component_overview_base, null, true);
        model = ramItem.findViewById(R.id.valueModel);
        manufacturer = ramItem.findViewById(R.id.valueManufacturer);
        listView = ramItem.findViewById(R.id.dataList);
        image = ramItem.findViewById(R.id.imageComponent);
    }

    public String formatBytes(long bytes) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String storageUnits = sp.getString("pref_general_ram_unit", context.getString(R.string.pref_general_storage_unit));
        switch (storageUnits) {
            case "TB":
                bytes /= 1024;
            case "GB":
                bytes /= 1024;
            case "MB":
                bytes /= 1024;
            case "KB":
                bytes /= 1024;
                break;
        }

        return bytes + storageUnits;
    }

}
