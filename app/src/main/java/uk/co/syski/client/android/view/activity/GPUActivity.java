package uk.co.syski.client.android.view.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.SystemGPUModel;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.adapter.listview.DoubleHeadedValueListAdapter;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.DoubleHeadedValueModel;
import uk.co.syski.client.android.viewmodel.SystemGPUViewModel;

/**
 * Activity for displaying all GPU information for a system
 */
public class GPUActivity extends SyskiActivity {

    private static final String TAG = "GPUActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);

        optionsMenu = new SyskiOptionsMenu();

        final DoubleHeadedValueListAdapter adapter = new DoubleHeadedValueListAdapter(this);
        ListView listView = findViewById(R.id.gpuList);
        listView.setAdapter(adapter);

        SystemGPUViewModel viewModel = ViewModelProviders.of(this).get(SystemGPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<SystemGPUModel>>() {
            @Override
            public void onChanged(@Nullable List<SystemGPUModel> gpuEntities) {
                ArrayList<DoubleHeadedValueModel> listItems = new ArrayList<>();

                for (int i = 0; i < gpuEntities.size(); i++) {
                    listItems.add( new DoubleHeadedValueModel(
                            R.drawable.gpu_icon,
                            "Model",
                            gpuEntities.get(i).getModelName(),
                            "Manufacturer",
                            gpuEntities.get(i).getManufacturerName()
                    ));
                }
                adapter.setData(listItems);
            }
        });
    }
}
