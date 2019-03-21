package uk.co.syski.client.android.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import uk.co.syski.client.android.R;

public class DoubleHeadedValueFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMAGE = "image";
    private static final String ARG_FIRST_HEADING = "firstHeading";
    private static final String ARG_FIRST_VALUE = "firstValue";
    private static final String ARG_SECOND_HEADING = "secondHeading";
    private static final String ARG_SECOND_VALUE = "secondValue";

    private Integer image;
    private String firstHeading, firstValue, secondHeading, secondValue;

    private ImageView imageView;
    private TextView firstHeadingView, firstValueView, secondHeadingView, secondValueView;


    public DoubleHeadedValueFragment() {
        // Required empty public constructor
    }

    public static DoubleHeadedValueFragment newInstance(Integer image, String firstHeading, String firstValue, String secondHeading, String secondValue) {
        DoubleHeadedValueFragment fragment = new DoubleHeadedValueFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putString(ARG_FIRST_HEADING, firstHeading);
        args.putString(ARG_FIRST_VALUE, firstValue);
        args.putString(ARG_SECOND_HEADING, secondHeading);
        args.putString(ARG_SECOND_VALUE, secondValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getInt(ARG_IMAGE);
            firstHeading = getArguments().getString(ARG_FIRST_HEADING);
            firstValue = getArguments().getString(ARG_FIRST_VALUE);
            secondHeading = getArguments().getString(ARG_SECOND_HEADING);
            secondValue = getArguments().getString(ARG_SECOND_VALUE);
        }
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

        if (image != null)
            imageView.setImageResource(image);

        firstHeadingView.setText(firstHeading);
        firstValueView.setText(firstValue);
        secondHeadingView.setText(secondHeading);
        secondValueView.setText(secondValue);
    }

}
