package uk.co.syski.client.android.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.HeadedValueListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_IMAGE = "image";
    private static final String ARG_FIRST_HEADING = "firstHeading";
    private static final String ARG_FIRST_VALUE = "firstValue";
    private static final String ARG_SECOND_HEADING = "secondHeading";
    private static final String ARG_SECOND_VALUE = "secondValue";

    // TODO: Rename and change types of parameters
    private Integer image;
    private String firstHeading, firstValue, secondHeading, secondValue;

    private ArrayList<HeadedValueModel> headedValues;

    private ImageView imageView;
    private TextView firstHeadingView, firstValueView, secondHeadingView, secondValueView;
    private ListView listView;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(Integer image, String firstHeading, String firstValue, String secondHeading, String secondValue) {
        OverviewFragment fragment = new OverviewFragment();
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
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        imageView = view.findViewById(R.id.imageView);
        firstHeadingView = view.findViewById(R.id.firstHeadingView);
        firstValueView = view.findViewById(R.id.firstValueView);
        secondHeadingView = view.findViewById(R.id.secondHeadingView);
        secondValueView = view.findViewById(R.id.secondValueView);
        listView = view.findViewById(R.id.listView);

        if (image != null)
            imageView.setImageResource(image);

        firstHeadingView.setText(firstHeading);
        firstValueView.setText(firstValue);
        secondHeadingView.setText(secondHeading);
        secondValueView.setText(secondValue);

        if (headedValues != null)
            listView.setAdapter(new HeadedValueListAdapter(getActivity(), headedValues));
    }

    public void setListViewData(ArrayList<HeadedValueModel> headedValues) {
        this.headedValues = headedValues;
    }
}
