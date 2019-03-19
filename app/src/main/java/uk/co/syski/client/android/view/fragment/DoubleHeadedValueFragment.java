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
    private static final String ARG_FIRSTHEADING = "firstHeading";
    private static final String ARG_FIRSTVALUE = "firstValue";
    private static final String ARG_SECONDHEADING = "secondHeading";
    private static final String ARG_SECONDVALUE = "secondValue";

    private Integer image;
    private String firstHeading, firstValue, secondHeading, secondValue;

    private ImageView imageView;
    private TextView firstHeadingView, firstValueView, secondHeadingView, secondValueView;


    public DoubleHeadedValueFragment() {
        // Required empty public constructor
    }

    public static HeadedValueFragment newInstance(Integer image, String firstHeading, String firstValue, String secondHeading, String secondValue) {
        HeadedValueFragment fragment = new HeadedValueFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_IMAGE, image);
        args.putString(ARG_FIRSTHEADING, firstHeading);
        args.putString(ARG_FIRSTVALUE, firstValue);
        args.putString(ARG_SECONDHEADING, secondHeading);
        args.putString(ARG_SECONDVALUE, secondValue);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            image = getArguments().getInt(ARG_IMAGE);
            firstHeading = getArguments().getString(ARG_FIRSTHEADING);
            firstValue = getArguments().getString(ARG_FIRSTVALUE);
            secondHeading = getArguments().getString(ARG_SECONDHEADING);
            secondValue = getArguments().getString(ARG_SECONDVALUE);
        }

        imageView = getActivity().findViewById(R.id.imageView);
        firstHeadingView = getActivity().findViewById(R.id.firstHeadingView);
        firstValueView = getActivity().findViewById(R.id.firstValueView);
        secondHeadingView = getActivity().findViewById(R.id.secondHeadingView);
        secondValueView = getActivity().findViewById(R.id.secondValueView);

        imageView.setImageResource(image);
        firstHeadingView.setText(firstHeading);
        firstValueView.setText(firstValue);
        secondHeadingView.setText(secondHeading);
        secondValueView.setText(secondValue);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_headed_value, container, false);
    }

}
