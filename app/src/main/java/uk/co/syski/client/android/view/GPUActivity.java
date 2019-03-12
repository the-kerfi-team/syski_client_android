package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.view.SystemListMenu;
import uk.co.syski.client.android.viewmodel.SystemGPUViewModel;

public class GPUActivity extends AppCompatActivity {

    private static final String TAG = "GPUActivity";
    List<GPUEntity> gpuList;
    GPUEntity gpu;
    SharedPreferences prefs;
    GridLayout gridLayout;

    //Views
    TextView model,man;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);

        initViews();


        SystemGPUViewModel viewModel = ViewModelProviders.of(this).get(SystemGPUViewModel.class);
        viewModel.get().observe(this, new Observer<List<GPUEntity>>() {
            @Override
            public void onChanged(@Nullable List<GPUEntity> gpuEntities) {
                if (gpuEntities.size() > 0) {
                    gpu = gpuEntities.get(0);
                    model.setText(gpu.ModelName);
                    man.setText(gpu.ManufacturerName);
                }
            }
        });

        /*

        if(gpu != null) {
            model.setText(gpu.ModelName);
            man.setText(gpu.ManufacturerName);
        }else {
            //TODO: Once test data is available, change to remove views based on cpu fields
            gridLayout.removeAllViewsInLayout();
            Toast.makeText(this,"GPU not found",Toast.LENGTH_SHORT).show();
        }*/

    }

    private void initViews(){
        prefs = this.getSharedPreferences(getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        model = findViewById(R.id.txtGPUModel);
        man = findViewById(R.id.txtGPUMan);
        gridLayout = findViewById(R.id.grdGPU);
    }

    private void getGPU(){

        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);

        if(gpuList.size() > 0){
            gpu = gpuList.get(0);
        } else{
            Log.i(TAG, "Query returned no storage entity");
        }

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
