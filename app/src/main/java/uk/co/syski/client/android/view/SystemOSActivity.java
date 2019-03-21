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
import uk.co.syski.client.android.model.fragment.HeadedValueModel;
import uk.co.syski.client.android.model.OperatingSystemModel;
import uk.co.syski.client.android.view.adapter.HeadedValueListAdapter;
import uk.co.syski.client.android.viewmodel.OperatingSystemViewModel;

public class SystemOSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        OperatingSystemViewModel viewModel = ViewModelProviders.of(this).get(OperatingSystemViewModel.class);
        viewModel.get().observe(this, new Observer<List<OperatingSystemModel>>() {
            @Override
            public void onChanged(@Nullable List<OperatingSystemModel> osEntities) {
                if (osEntities.size() > 0) {
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
            operatingSystemModel.Name
        ));
        osData.add(new HeadedValueModel(
            R.drawable.ic_architecture,
            "Architecture",
            operatingSystemModel.ArchitectureName
        ));
        osData.add(new HeadedValueModel(
            R.drawable.ic_version,
            "Version",
            operatingSystemModel.Version
        ));

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new HeadedValueListAdapter(this, osData));
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
