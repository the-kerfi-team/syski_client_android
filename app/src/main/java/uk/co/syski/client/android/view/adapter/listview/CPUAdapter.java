package uk.co.syski.client.android.view.adapter.listview;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.model.fragment.DoubleHeadedValueModel;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;

public class CPUAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<CPUEntity> listItems;

    public CPUAdapter(Activity context, List<CPUEntity> listItems) {
        super(context, R.layout.activity_cpu);

        ArrayList<CPUEntity> listWithRepeats = new ArrayList<>();

        for (int i = 0; i < listItems.size(); i++) {
            listWithRepeats.add(listItems.get(i));
            listWithRepeats.add(listItems.get(i));
            listWithRepeats.add(listItems.get(i));
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
        switch (position % 5) {
            case 0:
                return getDoubleHeadedValue(inflater, new DoubleHeadedValueModel(
                        R.drawable.ic_cpu,
                        "Model",
                        listItems.get(position).ModelName,
                        "Manufacturer",
                        listItems.get(position).ManufacturerName
                ));
            case 1:
                return getHeadedValue(inflater, new HeadedValueModel(
                        R.drawable.ic_architecture,
                        "Architecture",
                        listItems.get(position).ArchitectureName
                ));
            case 2:
                return getHeadedValue(inflater, new HeadedValueModel(
                        R.drawable.ic_clock,
                        "Clock Speed",
                        Integer.toString(listItems.get(position).ClockSpeed)
                ));
            case 3:
                return getHeadedValue(inflater, new HeadedValueModel(
                        R.drawable.ic_core,
                        "Core Count",
                        Integer.toString(listItems.get(position).CoreCount)
                ));
            case 4:
                return getHeadedValue(inflater, new HeadedValueModel(
                        R.drawable.ic_thread,
                        "Thread Count",
                        Integer.toString(listItems.get(position).ThreadCount)
                ));
        }

        return null;
    }

    private View getDoubleHeadedValue(LayoutInflater inflater, DoubleHeadedValueModel model) {
        View view = inflater.inflate(R.layout.fragment_double_headed_value, null, true);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView firstHeadingView = view.findViewById(R.id.firstHeadingView);
        TextView firstValueView = view.findViewById(R.id.firstValueView);
        TextView secondHeadingView = view.findViewById(R.id.secondHeadingView);
        TextView secondValueView = view.findViewById(R.id.secondValueView);

        imageView.setImageResource(model.image);
        firstHeadingView.setText(model.firstHeading);
        firstValueView.setText(model.firstValue);
        secondHeadingView.setText(model.secondHeading);
        secondValueView.setText(model.secondValue);

        return view;
    }

    private View getHeadedValue(LayoutInflater inflater, HeadedValueModel model) {
        View view = inflater.inflate(R.layout.fragment_headed_value, null, true);

        ImageView image = view.findViewById(R.id.imageView);
        TextView heading = view.findViewById(R.id.headingView);
        TextView value = view.findViewById(R.id.valueView);

        image.setImageResource(model.image);
        heading.setText(model.heading);
        value.setText(model.value);

        return view;
    }
}
