package uk.co.syski.client.android.view.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.StorageEntity;

public class StorageAdapter extends ArrayAdapter{

    private final Activity context;

    //List Items
    private List<StorageEntity> listItems;

    //Views
    TextView model,manufacturer,type,size;
    View storageItem;

    public StorageAdapter(Activity context, List<StorageEntity> storageEntities){
        super(context, R.layout.storage_item, storageEntities);
        this.context = context;
        this.listItems = storageEntities;
    }

    public void setDataSet(List<StorageEntity> storageEntities)
    {
        listItems = storageEntities;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        initViews();
        model.setText(listItems.get(position).ModelName);
        manufacturer.setText(listItems.get(position).ManufacturerName);
        type.setText(listItems.get(position).MemoryTypeName);
        size.setText(""+listItems.get(position).MemoryBytes);

        return storageItem;
    }

    public void initViews(){
        LayoutInflater inflater = context.getLayoutInflater();
        storageItem = inflater.inflate(R.layout.storage_item, null, true);
        model = storageItem.findViewById(R.id.i_txtStgModel);
        manufacturer = storageItem.findViewById(R.id.i_txtStgMan);
        type = storageItem.findViewById(R.id.i_txtStgType);
        size = storageItem.findViewById(R.id.i_txtStgSize);
    }

}
