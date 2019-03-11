package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.viewmodel.SystemCPUViewModel;

public class CPUActivity extends AppCompatActivity {

    private static final String TAG = "CPUActivity";
    TextView model,manufacturer,architecture,clock,core,thread;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        initViews();

        SystemCPUViewModel viewModel = ViewModelProviders.of(this).get(SystemCPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<CPUEntity>>() {
            @Override
            public void onChanged(@Nullable List<CPUEntity> cpuEntities) {
                model.setText(cpuEntities.get(0).ModelName);
                manufacturer.setText(cpuEntities.get(0).ManufacturerName);
                architecture.setText(cpuEntities.get(0).ArchitectureName);
                clock.setText("" + cpuEntities.get(0).ClockSpeed);
                core.setText("" + cpuEntities.get(0).CoreCount);
                thread.setText("" + cpuEntities.get(0).ThreadCount);
            }
        });
    }

    private void initViews() {
        model = findViewById(R.id.cpuModel);
        manufacturer = findViewById(R.id.cpuMan);
        architecture = findViewById(R.id.cpuArch);
        clock = findViewById(R.id.cpuClock);
        core = findViewById(R.id.cpuCore);
        thread = findViewById(R.id.cpuThread);
        gridLayout = findViewById(R.id.grdCPU);
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
