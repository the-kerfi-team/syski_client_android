package uk.co.syski.client.android.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemBIOSModel;
import uk.co.syski.client.android.view.adapter.listview.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.SystemBIOSViewModel;

public class BIOSActivity extends SyskiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);

        optionsMenu = new SyskiOptionsMenu();



        SystemBIOSViewModel viewModel = ViewModelProviders.of(this).get(SystemBIOSViewModel.class);
        viewModel.get().observe(this, new Observer<SystemBIOSModel>() {
            @Override
            public void onChanged(@Nullable SystemBIOSModel systemBIOSModel) {
                updateStaticUI(systemBIOSModel);
            }
        });
    }

    private void updateStaticUI(SystemBIOSModel biosEntity) {
        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.bios_icon,
               "Manufacturer",
                biosEntity.getManufacturerName(),
               "Caption",
                biosEntity.getCaption()
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();

        ArrayList<HeadedValueModel> biosData = new ArrayList<>();

        biosData.add(new HeadedValueModel(R.drawable.version_icon, "Version", biosEntity.getVersion()));
        biosData.add(new HeadedValueModel(R.drawable.date_icon, "Date", biosEntity.getDate()));

        ListView dataList = findViewById(R.id.listView);
        dataList.setAdapter(new HeadedValueListAdapter(this, biosData));
    }

}
