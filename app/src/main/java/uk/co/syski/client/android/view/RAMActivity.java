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
import uk.co.syski.client.android.model.database.entity.data.RAMDataEntity;
import uk.co.syski.client.android.model.viewmodel.SystemRAMModel;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.adapter.expandablelistview.RAMAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.graph.VariableRAMGraph;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.SystemRAMDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemRAMViewModel;

/**
 * Activity for displaying all RAM information for a system
 */
public class RAMActivity extends SyskiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        optionsMenu = new SyskiOptionsMenu();

        final RAMAdapter adapter = new RAMAdapter(this);
        ((ExpandableListView) findViewById(R.id.listView)).setAdapter(adapter);

        SystemRAMViewModel model = ViewModelProviders.of(this).get(SystemRAMViewModel.class);
        model.get().observe(this, new Observer<List<SystemRAMModel>>() {
            @Override
            public void onChanged(@Nullable List<SystemRAMModel> ramEntities) {
                adapter.setData(ramEntities);
            }
        });

        SystemRAMDataViewModel viewModel = ViewModelProviders.of(this).get(SystemRAMDataViewModel.class);
        viewModel.get().observe(this, new Observer<List<RAMDataEntity>>() {
            @Override
            public void onChanged(@Nullable List<RAMDataEntity> ramEntities) {
                updateRealTimeUI(ramEntities.get(ramEntities.size() - 1));
            }
        });

        View freeRAMFragment = findViewById(R.id.freeRAMFragment);
        freeRAMFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ramGraph = new Intent(v.getContext(), VariableRAMGraph.class);
                startActivity(ramGraph);
            }
        });
    }

    private void updateRealTimeUI(RAMDataEntity ramDataEntity) {
        HeadedValueFragment freeRAMFragment = HeadedValueFragment.newInstance(
            new HeadedValueModel(
                R.drawable.graph_icon,
                "Free RAM",
                ramDataEntity.Free + "MB"
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.freeRAMFragment, freeRAMFragment).commit();
    }
}
