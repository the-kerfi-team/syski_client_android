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

public class HeadedValueFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMAGE = "image";
    private static final String ARG_HEADING = "heading";
    private static final String ARG_VALUE = "value";

    private Integer image;
    private String heading;
    private String value;

    private ImageView imageView;
    private TextView headingView;
    private TextView valueView;

    public HeadedValueFragment() {
        // Required empty public constructor
    }

    public static HeadedValueFragment newInstance(Integer image, String heading, String value) {
        HeadedValueFragment fragment = new HeadedValueFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putString(ARG_HEADING, heading);
        args.putString(ARG_VALUE, value);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getInt(ARG_IMAGE);
            heading = getArguments().getString(ARG_HEADING);
            value = getArguments().getString(ARG_VALUE);
        }
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

        if (image != null)
            imageView.setImageResource(image);
        headingView.setText(heading);
        valueView.setText(value);
    }
}
