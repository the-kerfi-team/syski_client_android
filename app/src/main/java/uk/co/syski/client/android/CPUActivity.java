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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.CPUEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class CPUActivity extends AppCompatActivity {

    private static final String TAG = "CPUActivity";
    List<CPUEntity> cpuList;
    TextView model,manufacturer,architecture,clock,core,thread;
    CPUEntity cpu;
    GridLayout gridLayout;

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);

        initViews();
        getCPU();

        if(cpu != null) {
            model.setText(cpu.ModelName);
            manufacturer.setText(cpu.ManufacturerName);
            architecture.setText(cpu.ArchitectureName);
            clock.setText("" + cpu.ClockSpeed);
            core.setText("" + cpu.CoreCount);
            thread.setText("" + cpu.ThreadCount);
        } else {
            //TODO: Once test data is available, change to remove views based on cpu fields
            gridLayout.removeAllViewsInLayout();
            Toast.makeText(this,"CPU not found",Toast.LENGTH_SHORT).show();
        }
    }

    private void initViews() {
        model = findViewById(R.id.cpuModel);
        manufacturer = findViewById(R.id.cpuMan);
        architecture = findViewById(R.id.cpuArch);
        clock = findViewById(R.id.cpuClock);
        core = findViewById(R.id.cpuCore);
        thread = findViewById(R.id.cpuThread);
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        gridLayout = findViewById(R.id.grdCPU);
    }

    private void getCPU() {
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);

        try {
            Log.d(TAG, "Querying database");
            cpuList = SyskiCacheThread.getInstance().CPUThreads.GetCPUs(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(cpuList.size() > 0) {
            cpu = cpuList.get(0);
        } else {
            Log.i(TAG, "Query returned no CPU Entities");
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
