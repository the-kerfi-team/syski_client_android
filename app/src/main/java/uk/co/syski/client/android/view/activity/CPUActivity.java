package uk.co.syski.client.android.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.data.CPUDataEntity;
import uk.co.syski.client.android.model.viewmodel.SystemCPUModel;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.adapter.expandablelistview.CPUAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.graph.VariableCPULoadGraph;
import uk.co.syski.client.android.view.graph.VariableCPUProcessesGraph;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.SystemCPUDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemCPUViewModel;

/**
 * Activity for displaying all CPU information for a system
 */
public class CPUActivity extends SyskiActivity {

    private static final String TAG = "CPUActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        optionsMenu = new SyskiOptionsMenu();

        final CPUAdapter cpuAdapter = new CPUAdapter(this);
        ((ExpandableListView) findViewById(R.id.cpuList)).setAdapter(cpuAdapter);

        DisplayMetrics display = this.getResources().getDisplayMetrics();
        int width = display.widthPixels;
        ExpandableListView listView = findViewById(R.id.cpuList);
        listView.setIndicatorBounds(width-100, width);

        SystemCPUViewModel viewModel = ViewModelProviders.of(this).get(SystemCPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<SystemCPUModel>>() {
            @Override
            public void onChanged(@Nullable List<SystemCPUModel> cpuEntities) {
                cpuAdapter.setData(cpuEntities);
            }
        });

        SystemCPUDataViewModel realTimeViewModel = ViewModelProviders.of(this).get(SystemCPUDataViewModel.class);
        realTimeViewModel.get().observe(this, new Observer<List<CPUDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUDataEntity> cpuEntities) {
                if (cpuEntities.size() > 0) {
                    updateRealTimeUI(cpuEntities.get(cpuEntities.size() - 1));
                }
            }
        });

        View loadFragment = findViewById(R.id.loadFragment);
        loadFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cpuGraph = new Intent(v.getContext(), VariableCPULoadGraph.class);
                startActivity(cpuGraph);
            }
        });

        View processesFragment = findViewById(R.id.processesFragment);
        processesFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cpuGraph = new Intent(v.getContext(), VariableCPUProcessesGraph.class);
                startActivity(cpuGraph);
            }
        });
    }

    private void updateRealTimeUI(CPUDataEntity cpuDataEntity) {
        HeadedValueFragment loadModel = HeadedValueFragment.newInstance(
            new HeadedValueModel(
                R.drawable.graph_icon,
                "CPU Load",
                cpuDataEntity.Load + "%"
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.loadFragment, loadModel).commit();

        HeadedValueFragment processesModel = HeadedValueFragment.newInstance(
            new HeadedValueModel(
                R.drawable.graph_icon,
                "CPU Processes",
                Integer.toString(Math.round(cpuDataEntity.Processes))
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.processesFragment, processesModel).commit();
    }
}
