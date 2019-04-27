package uk.co.syski.client.android.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.view.adapter.listview.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.fragment.OverviewFragment;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.SystemSummaryViewModel;

/**
 * Activity for displaying an overview of a specified system
 */
public class SystemOverviewActivity extends SyskiActivity {

    private static final String TAG = "SystemOverviewActivity";
    private SystemSummaryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_overview);

        optionsMenu = new SyskiOptionsMenu();

        buildBaseUI();

        viewModel = ViewModelProviders.of(this).get(SystemSummaryViewModel.class);
        viewModel.get().observe(this, new Observer<SystemEntity>() {
            @Override
            public void onChanged(@Nullable SystemEntity systemEntity) {
                updateStaticUI(systemEntity);
            }
        });
    }

    private void buildBaseUI() {
        ListView listView = findViewById(R.id.compList);
        listView.setAdapter(new HeadedValueListAdapter(this, getComponentList()));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openComponentOverview(position);
            }
        });

        HeadedValueFragment shutdownModel = HeadedValueFragment.newInstance(new HeadedValueModel(R.drawable.shutdown_icon, "Shutdown System", "Tap here"));
        getSupportFragmentManager().beginTransaction().add(R.id.shutdownFragment, shutdownModel).commit();

        View shutdownFragment = findViewById(R.id.shutdownFragment);
        shutdownFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutdownOnClick(v);
            }
        });

        HeadedValueFragment restartModel = HeadedValueFragment.newInstance(new HeadedValueModel(R.drawable.restart_icon, "Restart System", "Tap here"));
        getSupportFragmentManager().beginTransaction().add(R.id.restartFragment, restartModel).commit();

        View restartFragment = findViewById(R.id.restartFragment);
        restartFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartOnClick(v);
            }
        });
    }

    private void updateStaticUI(SystemEntity systemEntity) {
        ArrayList<HeadedValueModel> systemData = new ArrayList<>();

        systemData.add(
            new HeadedValueModel(
                R.drawable.name_icon,
                "Host Name",
                systemEntity.HostName
            )
        );

        OverviewFragment overviewFragment = OverviewFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.pc_icon,
                "Model",
                systemEntity.ModelName,
                "Manufacturer",
                systemEntity.ManufacturerName
            ),
            systemData
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.overviewFragment, overviewFragment).commit();

        findViewById(R.id.overviewFragment).setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
    }

    private void openComponentOverview(int position) {
        Class dest;
        switch (position) {
            case 0: dest = CPUActivity.class;
                break;
            case 1: dest = RAMActivity.class;
                break;
            case 2: dest = SystemStorageActivity.class;
                break;
            case 3: dest = GPUActivity.class;
                break;
            case 4: dest = MOBOActivity.class;
                break;
            case 5: dest = BIOSActivity.class;
                break;
            case 6: dest = SystemOSActivity.class;
                break;
            case 7: dest = ProcessListActivity.class;
                break;
            default: dest = null;
        }

        Intent intent = new Intent(uk.co.syski.client.android.view.activity.SystemOverviewActivity.this, dest);
        startActivity(intent);
    }

    private List<HeadedValueModel> getComponentList() {
        ArrayList<HeadedValueModel> listItems = new ArrayList<>();

        listItems.add(
            new HeadedValueModel(
                R.drawable.cpu_icon,
                "View details for",
                "CPU"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ram_icon,
                "View details for",
                "RAM"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.storage_icon,
                "View details for",
                "Storage"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.gpu_icon,
                "View details for",
                "GPU"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.motherboard_icon,
                "View details for",
                "Motherboard"
            )
        );
        listItems.add(
                new HeadedValueModel(
                        R.drawable.bios_icon,
                        "View details for",
                        "BIOS"
                )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.pc_icon,
                "View details for",
                "Operating System"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.process_icon,
                "View details of",
                "Processes"
            )
        );

        return listItems;
    }


    public void shutdownOnClick(View v) {
        viewModel.shutdownOnClick();
    }

    public void restartOnClick(View v) {
        viewModel.restartOnClick();
    }

}
