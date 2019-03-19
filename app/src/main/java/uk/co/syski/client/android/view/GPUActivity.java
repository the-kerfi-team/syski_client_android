package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.viewmodel.SystemGPUViewModel;

public class GPUActivity extends AppCompatActivity {

    private static final String TAG = "GPUActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);

        SystemGPUViewModel viewModel = ViewModelProviders.of(this).get(SystemGPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<GPUEntity>>() {
            @Override
            public void onChanged(@Nullable List<GPUEntity> gpuEntities) {
                if (gpuEntities.size() > 0) {
                    updateStaticUI(gpuEntities.get(0));
                }
            }
        });
    }

    private void updateStaticUI(GPUEntity gpuEntity) {
        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(
            R.drawable.ic_gpu,
            "Model",
            gpuEntity.ModelName,
            "Manufacturer",
            gpuEntity.ManufacturerName
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();
    }
}
