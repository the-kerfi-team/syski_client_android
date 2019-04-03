package uk.co.syski.client.android.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.fragment.DoubleHeadedValueModel;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.listview.HeadedValueListAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OverviewFragment extends Fragment {

    private DoubleHeadedValueModel model;
    private ArrayList<HeadedValueModel> listItems;

    private ListView listView;

    public OverviewFragment() {
        // Required empty public constructor
    }

    public static OverviewFragment newInstance(DoubleHeadedValueModel model, ArrayList<HeadedValueModel> listItems) {
        OverviewFragment fragment = new OverviewFragment();
        fragment.setModel(model);
        fragment.setListViewData(listItems);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(model);
        getFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();

        listView = view.findViewById(R.id.listView);
        if (listItems != null)
            listView.setAdapter(new HeadedValueListAdapter(getActivity(), listItems));
    }

    private void setModel(DoubleHeadedValueModel model) {
        this.model = model;
    }

    private void setListViewData(ArrayList<HeadedValueModel> listItems) {
        this.listItems = listItems;
    }
}
