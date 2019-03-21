package uk.co.syski.client.android.view.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;

public class RAMAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<RAMEntity> listItems;

    private View listItem;

    public RAMAdapter(Activity context, List<RAMEntity> listItems) {
        super(context, R.layout.activity_ram);

        ArrayList<RAMEntity> listWithRepeats = new ArrayList<>();

        for (int i = 0; i < listItems.size(); i++) {
            listWithRepeats.add(listItems.get(i));
            listWithRepeats.add(listItems.get(i));
        }

        addAll(listWithRepeats);

        this.context = context;
        this.listItems = listWithRepeats;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        if (position % 2 == 0)
        {
            listItem = inflater.inflate(R.layout.fragment_double_headed_value, null, true);

            ImageView imageView = listItem.findViewById(R.id.imageView);
            TextView firstHeadingView = listItem.findViewById(R.id.firstHeadingView);
            TextView firstValueView = listItem.findViewById(R.id.firstValueView);
            TextView secondHeadingView = listItem.findViewById(R.id.secondHeadingView);
            TextView secondValueView = listItem.findViewById(R.id.secondValueView);

            imageView.setImageResource(R.drawable.ic_gpu);
            firstHeadingView.setText("Model");
            firstValueView.setText(listItems.get(position).ModelName);
            secondHeadingView.setText("Manufacturer");
            secondValueView.setText(listItems.get(position).ManufacturerName);
        }
        else
        {
            listItem = inflater.inflate(R.layout.fragment_headed_value, null, true);

            ImageView image = listItem.findViewById(R.id.imageView);
            TextView heading = listItem.findViewById(R.id.headingView);
            TextView value = listItem.findViewById(R.id.valueView);

            image.setImageResource(R.drawable.ic_memory_size);
            heading.setText("Size");
            value.setText(formatBytes(listItems.get(position).MemoryBytes));
        }

        return listItem;
    }

    public String formatBytes(long bytes) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String ramUnits = sp.getString("pref_general_ram_unit", context.getString(R.string.pref_general_storage_unit));
        switch (ramUnits) {
            case "TB":
                bytes /= 1024;
            case "GB":
                bytes /= 1024;
            case "MB":
                bytes /= 1024;
            case "KB":
                bytes /= 1024;
                break;
            default:
                return bytes + "B";
        }

        return bytes + " " + ramUnits;
    }
}
