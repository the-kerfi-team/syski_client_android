package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.model.HeadedValueModel;
import uk.co.syski.client.android.view.adapters.ComponentDataListAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.viewmodel.MotherboardViewModel;

public class MOBOActivity extends AppCompatActivity {

    private static final String TAG = "MOBOActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);

        MotherboardViewModel viewModel = ViewModelProviders.of(this).get(MotherboardViewModel.class);
        viewModel.get().observe(this, new Observer<List<MotherboardEntity>>() {
            @Override
            public void onChanged(@Nullable List<MotherboardEntity> motherboardEntities) {
                if (motherboardEntities.size() > 0) {
                    updateStaticUI(motherboardEntities.get(0));
                }
            }
        });
    }

    private void updateStaticUI(MotherboardEntity motherboardEntity) {
        ArrayList<HeadedValueModel> motherboardData = new ArrayList<>();

        motherboardData.add(
            new HeadedValueModel(
                R.drawable.ic_version,
                "Version",
                motherboardEntity.Version
            )
        );

        DoubleHeadedValueFragment topFragment = DoubleHeadedValueFragment.newInstance(
            R.drawable.ic_gpu,
            "Model",
            motherboardEntity.ModelName,
            "Manufacturer",
            motherboardEntity.ManufacturerName
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.topFragment, topFragment).commit();

        ListView dataList = findViewById(R.id.listView);
        dataList.setAdapter(new ComponentDataListAdapter(this, motherboardData));
    }
}
