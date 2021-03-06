package uk.co.syski.client.android.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemMotherboardModel;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.adapter.listview.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.MotherboardViewModel;

/**
 * Activity for displaying all Motherboard information for a system
 */
public class MOBOActivity extends SyskiActivity {

    private static final String TAG = "MOBOActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);

        optionsMenu = new SyskiOptionsMenu();

        MotherboardViewModel viewModel = ViewModelProviders.of(this).get(MotherboardViewModel.class);
        viewModel.get().observe(this, new Observer<SystemMotherboardModel>() {
            @Override
            public void onChanged(@Nullable SystemMotherboardModel motherboardEntity) {
                updateStaticUI(motherboardEntity);
            }
        });
    }

    private void updateStaticUI(SystemMotherboardModel motherboardEntity) {
        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.motherboard_icon,
                "Model",
                motherboardEntity.getModelName(),
                "Manufacturer",
                motherboardEntity.getManufacturerName()
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();

        ArrayList<HeadedValueModel> motherboardData = new ArrayList<>();
        motherboardData.add(
            new HeadedValueModel(
                R.drawable.version_icon,
                "Version",
                motherboardEntity.getVersion()
            )
        );

        ListView dataList = findViewById(R.id.listView);
        dataList.setAdapter(new HeadedValueListAdapter(this, motherboardData));
    }
}
