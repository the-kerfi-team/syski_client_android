package uk.co.syski.client.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class GPUActivity extends AppCompatActivity {

    private static final String TAG = "GPUActivity";
    List<GPUEntity> gpuList;
    GPUEntity gpu;
    SharedPreferences prefs;
    GridLayout gridLayout;

    //Views
    TextView model,man,arch,clock,core,thread,type,size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpu);

        initViews();
        getGPU();

        if(gpu != null) {
            model.setText(gpu.ModelName);
            man.setText(gpu.ManufacturerName);
            arch.setText(gpu.ArchitectureName);
            clock.setText("" + gpu.ClockSpeed);
            core.setText("" + gpu.CoreCount);
            thread.setText("" + gpu.ThreadCount);
            type.setText(gpu.MemoryTypeName);
            size.setText("" + gpu.MemoryBytes);
        }else {
            //TODO: Once test data is available, change to remove views based on cpu fields
            gridLayout.removeAllViewsInLayout();
            Toast.makeText(this,"GPU not found",Toast.LENGTH_SHORT).show();
        }

    }

    private void initViews(){
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        model = findViewById(R.id.txtGPUModel);
        man = findViewById(R.id.txtGPUMan);
        arch = findViewById(R.id.txtGPUArch);
        clock = findViewById(R.id.txtGPUClock);
        core = findViewById(R.id.txtGPUCore);
        thread = findViewById(R.id.txtGPUThread);
        type = findViewById(R.id.txtGPUType);
        size = findViewById(R.id.txtGPUSize);
        gridLayout = findViewById(R.id.grdGPU);
    }

    private void getGPU(){

        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        try {
            Log.d(TAG, "Querying database");
            gpuList = SyskiCacheThread.getInstance().GPUThreads.GetGPUs(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
            Intent settings = new Intent(this, SysListMenu.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
