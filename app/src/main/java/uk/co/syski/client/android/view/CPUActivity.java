package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.model.HeadedValueModel;
import uk.co.syski.client.android.view.adapters.ComponentDataListAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
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
    }

    private void updateStaticUI(CPUEntity cpuEntity) {
        ArrayList<HeadedValueModel> cpuData = new ArrayList<>();

        cpuData.add(
            new HeadedValueModel(
                R.drawable.ic_architecture,
                "Architecture",
                cpuEntity.ArchitectureName
            )
        );
        cpuData.add(
            new HeadedValueModel(
                R.drawable.ic_clock,
                "Clock Speed",
                cpuEntity.ClockSpeed + "Hz"
            )
        );
        cpuData.add(
            new HeadedValueModel(
                R.drawable.ic_core,
                "Core Count",
                Integer.toString(cpuEntity.CoreCount)
            )
        );
        cpuData.add(
            new HeadedValueModel(
                R.drawable.ic_thread,
                "Thread Count",
                Integer.toString(cpuEntity.ThreadCount)
            )
        );

        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(
            R.drawable.ic_cpu,
            "Model",
            cpuEntity.ModelName,
            "Manufacturer",
            cpuEntity.ManufacturerName
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();

        ListView dataList = findViewById(R.id.listView);
        dataList.setAdapter(new ComponentDataListAdapter(this, cpuData));
    }

    private void updateRealTimeUI(CPUDataEntity cpuDataEntity) {
        HeadedValueFragment loadModel = HeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "CPU Load",
            cpuDataEntity.Load + "%"
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.loadFragment, loadModel).commit();

        HeadedValueFragment processesModel = HeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "CPU Processes",
            Float.toString(cpuDataEntity.Processes));

        getSupportFragmentManager().beginTransaction().replace(R.id.processesFragment, processesModel).commit();
    }
}
