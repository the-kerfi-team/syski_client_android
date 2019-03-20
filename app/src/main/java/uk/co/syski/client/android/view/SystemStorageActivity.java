package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.data.entity.data.StorageDataEntity;
import uk.co.syski.client.android.view.adapters.StorageAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.graph.VariableStorageByteReadWriteGraph;
import uk.co.syski.client.android.view.graph.VariableStorageReadWriteGraph;
import uk.co.syski.client.android.view.graph.VariableStorageTimeGraph;
import uk.co.syski.client.android.view.graph.VariableStorageTransfersGraph;
import uk.co.syski.client.android.viewmodel.SystemStorageDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemStorageViewModel;

public class SystemStorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        SystemStorageViewModel model = ViewModelProviders.of(this).get(SystemStorageViewModel.class);
        model.get().observe(this, new Observer<List<StorageEntity>>() {
            @Override
            public void onChanged(@Nullable List<StorageEntity> StorageEntities) {
                updateStaticUI(StorageEntities);
            }
        });

        SystemStorageDataViewModel viewModel = ViewModelProviders.of(this).get(SystemStorageDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<StorageDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<StorageDataEntity> StorageEntities) {
                updateRealTimeUI(StorageEntities.get(StorageEntities.size() - 1));
            }
        });

        View timeFragment = findViewById(R.id.timeFragment);
        timeFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timeGraph = new Intent(v.getContext(), VariableStorageTimeGraph.class);
                startActivity(timeGraph);
            }
        });

        View transfersFragment = findViewById(R.id.transfersFragment);
        transfersFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfersGraph = new Intent(v.getContext(), VariableStorageTransfersGraph.class);
                startActivity(transfersGraph);
            }
        });

        View readsWritesFragment = findViewById(R.id.readsWritesFragment);
        readsWritesFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent readsWritesGraph = new Intent(v.getContext(), VariableStorageReadWriteGraph.class);
                startActivity(readsWritesGraph);
            }
        });

        View byteReadsWritesFragment = findViewById(R.id.byteReadsWritesFragment);
        byteReadsWritesFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent byteReadsWritesGraph = new Intent(v.getContext(), VariableStorageByteReadWriteGraph.class);
                startActivity(byteReadsWritesGraph);
            }
        });
    }

    private void updateStaticUI(List<StorageEntity> StorageEntities) {
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new StorageAdapter(this, StorageEntities));
    }

    private void updateRealTimeUI(StorageDataEntity StorageDataEntity) {
        HeadedValueFragment timeFragment = HeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "Time",
            StorageDataEntity.Time + "s"
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.timeFragment, timeFragment).commit();

        HeadedValueFragment transfersFragment = HeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "Transfers",
            Float.toString(StorageDataEntity.Transfers)
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.transfersFragment, transfersFragment).commit();

        DoubleHeadedValueFragment readsWritesFragment = DoubleHeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "Reads",
            Float.toString(StorageDataEntity.Reads),
            "Writes",
            Float.toString(StorageDataEntity.Writes)
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.readsWritesFragment, readsWritesFragment).commit();

        DoubleHeadedValueFragment byteReadsWritesFragment = DoubleHeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "Byte Reads",
            Float.toString(StorageDataEntity.ByteReads),
            "Byte Writes",
            Float.toString(StorageDataEntity.ByteWrites)
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.byteReadsWritesFragment, byteReadsWritesFragment).commit();
    }
}
