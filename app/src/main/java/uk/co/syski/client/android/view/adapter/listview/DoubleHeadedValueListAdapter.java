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
import uk.co.syski.client.android.model.fragment.DoubleHeadedValueModel;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;

public class DoubleHeadedValueListAdapter extends ArrayAdapter {

    private final Activity context;
    private final List<DoubleHeadedValueModel> listItems;

    ImageView imageView;
    TextView firstHeadingView, firstValueView, secondHeadingView, secondValueView;

    View view;

    public DoubleHeadedValueListAdapter(Activity context, List<DoubleHeadedValueModel> listItems) {
        super(context, R.layout.fragment_overview, listItems);
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        initViews();

        imageView.setImageResource(listItems.get(position).image);
        firstHeadingView.setText(listItems.get(position).firstHeading);
        firstValueView.setText(listItems.get(position).firstValue);
        secondHeadingView.setText(listItems.get(position).secondHeading);
        secondValueView.setText(listItems.get(position).secondValue);

        return this.view;
    }

    public void initViews(){
        LayoutInflater inflater = context.getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_double_headed_value, null, false);
        imageView = view.findViewById(R.id.imageView);
        firstHeadingView = view.findViewById(R.id.firstHeadingView);
        firstValueView = view.findViewById(R.id.firstValueView);
        secondHeadingView = view.findViewById(R.id.secondHeadingView);
        secondValueView = view.findViewById(R.id.secondValueView);

    }
}
