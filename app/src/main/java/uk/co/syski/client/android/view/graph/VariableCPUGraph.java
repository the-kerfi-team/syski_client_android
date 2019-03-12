package uk.co.syski.client.android.view.graph;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jjoe64.graphview.GraphView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.entity.data.CPUDataEntity;
import uk.co.syski.client.android.data.repository.SystemCPUDataRepository;
import uk.co.syski.client.android.viewmodel.SystemCPULoadDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemCPUViewModel;

public class VariableCPUGraph extends AppCompatActivity {

    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_variable_cpugraph);
        graph = findViewById(R.id.graph);

        SystemCPULoadDataViewModel viewModel = ViewModelProviders.of(this).get(SystemCPULoadDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<CPUDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUDataEntity> cpuEntities) {
                if (cpuEntities.size() > 0)
                {
                    // TODO Plot graph?
                }
            }
        });

    }
}
