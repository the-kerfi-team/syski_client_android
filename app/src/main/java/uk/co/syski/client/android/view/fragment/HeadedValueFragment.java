package uk.co.syski.client.android.view.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;

public class HeadedValueFragment extends Fragment {

    private HeadedValueModel model;

    private ImageView imageView;
    private TextView headingView;
    private TextView valueView;

    public HeadedValueFragment() {}

    public static HeadedValueFragment newInstance(HeadedValueModel model) {
        HeadedValueFragment fragment = new HeadedValueFragment();
        fragment.setModel(model);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_headed_value, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.imageView);
        headingView = view.findViewById(R.id.headingView);
        valueView = view.findViewById(R.id.valueView);

        if (model.image != null)
            imageView.setImageResource(model.image);

        headingView.setText(model.heading);
        valueView.setText(model.value);
    }

    private void setModel(HeadedValueModel model) {
        this.model = model;
    }
}
