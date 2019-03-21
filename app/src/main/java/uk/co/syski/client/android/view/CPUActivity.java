package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.model.fragment.DoubleHeadedValueModel;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.fragment.OverviewFragment;
import uk.co.syski.client.android.view.graph.VariableCPULoadGraph;
import uk.co.syski.client.android.view.graph.VariableCPUProcessesGraph;
import uk.co.syski.client.android.viewmodel.SystemCPUDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemCPUViewModel;

public class CPUActivity extends AppCompatActivity {

    private static final String TAG = "CPUActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        SystemCPUViewModel viewModel = ViewModelProviders.of(this).get(SystemCPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<CPUEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUEntity> cpuEntities) {
                if (cpuEntities.size() > 0) {
                    updateStaticUI(cpuEntities.get(0));
                }
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

    private void updateStaticUI(CPUEntity cpuEntity) {
        ArrayList<HeadedValueModel> cpuData = new ArrayList<>();

        cpuData.add(new HeadedValueModel(R.drawable.ic_architecture, "Architecture", cpuEntity.ArchitectureName));
        cpuData.add(new HeadedValueModel(R.drawable.ic_clock, "Clock Speed", cpuEntity.ClockSpeed + " MHz"));
        cpuData.add(new HeadedValueModel(R.drawable.ic_core, "Core Count", Integer.toString(cpuEntity.CoreCount)));
        cpuData.add(new HeadedValueModel(R.drawable.ic_thread, "Thread Count", Integer.toString(cpuEntity.ThreadCount)));

        OverviewFragment mainFragment = OverviewFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.ic_cpu,
                "Model",
                cpuEntity.ModelName,
                "Manufacturer",
                cpuEntity.ManufacturerName
            ),
            cpuData
        );

        getSupportFragmentManager().beginTransaction().add(R.id.mainFragment, mainFragment).commit();
    }

    private void updateRealTimeUI(CPUDataEntity cpuDataEntity) {
        HeadedValueFragment loadModel = HeadedValueFragment.newInstance(
            new HeadedValueModel(
                R.drawable.placeholder,
                "CPU Load",
                cpuDataEntity.Load + "%"
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.loadFragment, loadModel).commit();

        HeadedValueFragment processesModel = HeadedValueFragment.newInstance(
            new HeadedValueModel(
                R.drawable.placeholder,
                "CPU Processes",
                Integer.toString(Math.round(cpuDataEntity.Processes))
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.processesFragment, processesModel).commit();
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
