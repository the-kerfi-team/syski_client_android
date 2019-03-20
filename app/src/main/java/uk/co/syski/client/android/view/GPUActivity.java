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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.view.fragment.DoubleHeadedValueFragment;
import uk.co.syski.client.android.viewmodel.SystemGPUViewModel;

public class GPUActivity extends AppCompatActivity {

    private static final String TAG = "GPUActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_overview);

        SystemGPUViewModel viewModel = ViewModelProviders.of(this).get(SystemGPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<GPUEntity>>() {
            @Override
            public void onChanged(@Nullable List<GPUEntity> gpuEntities) {
                if (gpuEntities.size() > 0) {
                    updateStaticUI(gpuEntities.get(0));
                }
            }
        });
    }

    private void updateStaticUI(GPUEntity gpuEntity) {
        View topFragment = findViewById(R.id.topFragment);

        ImageView imageView = topFragment.findViewById(R.id.imageView);
        TextView firstHeadingView = topFragment.findViewById(R.id.firstHeadingView);
        TextView firstValueView = topFragment.findViewById(R.id.firstValueView);
        TextView secondHeadingView = topFragment.findViewById(R.id.secondHeadingView);
        TextView secondValueView = topFragment.findViewById(R.id.secondValueView);

        imageView.setImageResource(R.drawable.ic_gpu);
        firstHeadingView.setText("Model");
        firstValueView.setText(gpuEntity.ModelName);
        secondHeadingView.setText("Manufacturer");
        secondValueView.setText(gpuEntity.ManufacturerName);
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
