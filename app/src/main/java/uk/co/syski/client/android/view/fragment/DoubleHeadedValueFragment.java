package uk.co.syski.client.android.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;

public class DoubleHeadedValueFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private DoubleHeadedValueModel model;

    private ImageView imageView;
    private TextView firstHeadingView, firstValueView, secondHeadingView, secondValueView;


    public DoubleHeadedValueFragment() {
        // Required empty public constructor
    }

    public static DoubleHeadedValueFragment newInstance(DoubleHeadedValueModel model) {
        DoubleHeadedValueFragment fragment = new DoubleHeadedValueFragment();
        fragment.setModel(model);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_double_headed_value, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.imageView);
        firstHeadingView = view.findViewById(R.id.firstHeadingView);
        firstValueView = view.findViewById(R.id.firstValueView);
        secondHeadingView = view.findViewById(R.id.secondHeadingView);
        secondValueView = view.findViewById(R.id.secondValueView);

        if (model.image != null)
            imageView.setImageResource(model.image);

        firstHeadingView.setText(model.firstHeading);
        firstValueView.setText(model.firstValue);
        secondHeadingView.setText(model.secondHeading);
        secondValueView.setText(model.secondValue);
    }


    private void setModel(DoubleHeadedValueModel model) {
        this.model = model;
    }

}
