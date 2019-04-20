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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.viewmodel.OperatingSystemModel;
import uk.co.syski.client.android.view.activity.SyskiActivity;
import uk.co.syski.client.android.view.adapter.listview.HeadedValueListAdapter;
import uk.co.syski.client.android.view.menu.SyskiOptionsMenu;
import uk.co.syski.client.android.view.model.HeadedValueModel;
import uk.co.syski.client.android.viewmodel.OperatingSystemViewModel;

/**
 * Activity for displaying all Operating System information for a system
 */
public class SystemOSActivity extends SyskiActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        optionsMenu = new SyskiOptionsMenu();

        OperatingSystemViewModel viewModel = ViewModelProviders.of(this).get(OperatingSystemViewModel.class);
        viewModel.get().observe(this, new Observer<List<OperatingSystemModel>>() {
            @Override
            public void onChanged(@Nullable List<OperatingSystemModel> osEntities) {
                if (osEntities != null && osEntities.size() > 0) {
                    updateStaticUI(osEntities.get(0));
                }
            }
        });
    }

    private void updateStaticUI(OperatingSystemModel operatingSystemModel) {
        ArrayList<HeadedValueModel> osData = new ArrayList<>();

        osData.add(new HeadedValueModel(
            R.drawable.ic_pc,
            "Name",
            operatingSystemModel.getName()
        ));
        osData.add(new HeadedValueModel(
            R.drawable.ic_architecture,
            "Architecture",
            operatingSystemModel.getArchitectureName()
        ));
        osData.add(new HeadedValueModel(
            R.drawable.ic_version,
            "Version",
            operatingSystemModel.getVersion()
        ));

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new HeadedValueListAdapter(this, osData));
    }
}
