package uk.co.syski.client.android.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ExpandableListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.entity.data.SystemProcessesEntity;
import uk.co.syski.client.android.model.viewmodel.SystemCPUModel;
import uk.co.syski.client.android.view.adapter.expandablelistview.ProcessAdapter;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.viewmodel.SystemProcessesViewModel;

public class ProcessListActivity extends SyskiActivity {

    ExpandableListView processList;
    ProcessAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_list);

        optionsMenu = new SyskiOptionsMenu();

        processList = findViewById(R.id.processList);
        adapter = new ProcessAdapter(this);
        processList.setAdapter(adapter);

        SystemProcessesViewModel viewModel = ViewModelProviders.of(this).get(SystemProcessesViewModel.class);
        viewModel.get().observe(this, new Observer<List<SystemProcessesEntity>>() {
            @Override
            public void onChanged(@Nullable List<SystemProcessesEntity> systemProcessesEntities) {
                adapter.setData(systemProcessesEntities);
                processList.deferNotifyDataSetChanged();
            }
        });
    }
}
