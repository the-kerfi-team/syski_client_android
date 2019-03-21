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
import android.widget.ListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.data.RAMDataEntity;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.RAMAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.view.graph.VariableRAMGraph;
import uk.co.syski.client.android.viewmodel.SystemRAMDataViewModel;
import uk.co.syski.client.android.viewmodel.SystemRAMViewModel;

public class RAMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        SystemRAMViewModel model = ViewModelProviders.of(this).get(SystemRAMViewModel.class);
        model.get().observe(this, new Observer<List<RAMEntity>>() {
            @Override
            public void onChanged(@Nullable List<RAMEntity> ramEntities) {
                updateStaticUI(ramEntities);
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

    private void updateStaticUI(List<RAMEntity> ramEntities) {
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new RAMAdapter(this, ramEntities));
    }

    private void updateRealTimeUI(RAMDataEntity ramDataEntity) {
        HeadedValueFragment freeRAMFragment = HeadedValueFragment.newInstance(
            new HeadedValueModel(
                R.drawable.placeholder,
                "Free RAM",
                ramDataEntity.Free + "MB"
            )
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.freeRAMFragment, freeRAMFragment).commit();
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
