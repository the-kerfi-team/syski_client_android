package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.data.StorageDataEntity;
import uk.co.syski.client.android.model.viewmodel.SystemStorageModel;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.adapter.expandablelistview.StorageAdapter;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.graph.VariableStorageByteReadWriteGraph;
import uk.co.syski.client.android.view.graph.VariableStorageReadWriteGraph;
import uk.co.syski.client.android.view.graph.VariableStorageTimeGraph;
import uk.co.syski.client.android.view.graph.VariableStorageTransfersGraph;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.SystemStorageDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemStorageViewModel;

/**
 * Activity for displaying all storage information for a system
 */
public class SystemStorageActivity extends SyskiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        optionsMenu = new SyskiOptionsMenu();

        final StorageAdapter adapter = new StorageAdapter(this);
        ((ExpandableListView) findViewById(R.id.listView)).setAdapter(adapter);

        SystemStorageViewModel model = ViewModelProviders.of(this).get(SystemStorageViewModel.class);
        model.get().observe(this, new Observer<List<SystemStorageModel>>() {
            @Override
            public void onChanged(@Nullable List<SystemStorageModel> StorageEntities) {
                adapter.setData(StorageEntities);
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

    private void updateRealTimeUI(StorageDataEntity StorageDataEntity) {
        HeadedValueFragment timeFragment = HeadedValueFragment.newInstance(
                new HeadedValueModel(
                R.drawable.placeholder,
                "Time",
                StorageDataEntity.Time + "s"
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.timeFragment, timeFragment).commit();

        HeadedValueFragment transfersFragment = HeadedValueFragment.newInstance(
                new HeadedValueModel(
                R.drawable.placeholder,
                "Transfers",
                Float.toString(StorageDataEntity.Transfers)
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.transfersFragment, transfersFragment).commit();

        DoubleHeadedValueFragment readsWritesFragment = DoubleHeadedValueFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.placeholder,
                "Reads",
                Float.toString(StorageDataEntity.Reads),
                "Writes",
                Float.toString(StorageDataEntity.Writes)
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.readsWritesFragment, readsWritesFragment).commit();

        DoubleHeadedValueFragment byteReadsWritesFragment = DoubleHeadedValueFragment.newInstance(
            new DoubleHeadedValueModel(
                R.drawable.placeholder,
                "Byte Reads",
                Float.toString(StorageDataEntity.ByteReads),
                "Byte Writes",
                Float.toString(StorageDataEntity.ByteWrites)
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.byteReadsWritesFragment, byteReadsWritesFragment).commit();
    }
}
