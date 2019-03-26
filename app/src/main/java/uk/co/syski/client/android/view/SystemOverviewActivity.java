package uk.co.syski.client.android.view;

import android.app.ActionBar;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.UserEntity;

import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.model.fragment.DoubleHeadedValueModel;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.fragment.OverviewFragment;
import uk.co.syski.client.android.viewmodel.SystemSummaryViewModel;

public class SystemOverviewActivity extends AppCompatActivity {

    private static final String TAG = "SystemOverviewActivity";

    SystemSummaryViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_overview);

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

        HeadedValueFragment shutdownModel = HeadedValueFragment.newInstance(new HeadedValueModel(R.drawable.placeholder, "Shutdown System", "Tap here"));
        getSupportFragmentManager().beginTransaction().add(R.id.shutdownFragment, shutdownModel).commit();

        View shutdownFragment = findViewById(R.id.shutdownFragment);
        shutdownFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutdownOnClick(v);
            }
        });

        HeadedValueFragment restartModel = HeadedValueFragment.newInstance(new HeadedValueModel(R.drawable.placeholder, "Restart System", "Tap here"));
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
                R.drawable.placeholder,
                "Host Name",
                systemEntity.HostName
            )
        );

        OverviewFragment overviewFragment = OverviewFragment.newInstance(
            new DoubleHeadedValueModel (
                R.drawable.ic_pc,
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
            case 5: dest = SystemOSActivity.class;
                break;
            default: dest = null;
        }

        Intent intent = new Intent(SystemOverviewActivity.this, dest);
        startActivity(intent);
    }

    private List<HeadedValueModel> getComponentList() {
        ArrayList<HeadedValueModel> listItems = new ArrayList<>();

        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_cpu,
                "View details for",
                "CPU"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_gpu,
                "View details for",
                "RAM"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_storage,
                "View details for",
                "Storage"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_gpu,
                "View details for",
                "GPU"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_gpu,
                "View details for",
                "Motherboard"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_pc,
                "View details for",
                "Operating System"
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_syslist) {
            Intent settings = new Intent(this, SystemListMenu.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
