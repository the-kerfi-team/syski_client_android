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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.adapters.SysListOverviewAdapter;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;
import uk.co.syski.client.android.model.OperatingSystemModel;

public class SysOverviewActivity extends AppCompatActivity {

    private static final String TAG = "SysOverviewActivity";

    TextView host,manufacturer,model,os;
    ImageView img;
    SystemEntity systemEntity;
    ListView listView;
    OperatingSystemModel sysOS;
    LinearLayout linearLayout;

    List<OperatingSystemModel> osList;

    String[] listItems = {
            "CPU",
            "RAM",
            "Storage",
            "GPU",
            "Motherboard"
    };

    //TODO: Replace placeholders with corresponding icons
    Integer[] images = {
            R.drawable.ic_cpu,
            //TODO: Find better Icon
            R.drawable.ic_gpu,
            R.drawable.ic_storage,
            R.drawable.ic_gpu,
            //TODO: Find better Icon
            R.drawable.ic_gpu
    };

    SharedPreferences prefs;
    SystemEntity system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_overview);
        initViews();
        getOS();
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        system = getSystem(sysId);


        host.setText(system.HostName);
        manufacturer.setText(system.ManufacturerName);
        model.setText(system.ModelName);

        if(sysOS != null) {
            os.setText(sysOS.Name);
        } else {
            linearLayout.removeView(os);
        }

        SysListOverviewAdapter adapter = new SysListOverviewAdapter(this, images, listItems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class dest;
                switch (position) {
                    case 0: dest = CPUActivity.class;
                            break;
                    case 1: dest = RAMActivity.class;
                            break;
                    case 2: dest = StorageActivity.class;
                            break;
                    case 3: dest = GPUActivity.class;
                            break;
                    case 4: dest = MOBOActivity.class;
                            break;
                    default: dest = null;
                }

                Intent intent = new Intent(SysOverviewActivity.this, dest);

                startActivity(intent);
            }
        });

    }

    private SystemEntity getSystem(String sysId){
        List<SystemEntity> sysList = null;
        try {
            sysList = SyskiCacheThread.getInstance().SystemThreads.GetSystems(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        systemEntity = sysList.get(0);
        return systemEntity;
    }

    private void initViews(){
        host = findViewById(R.id.txtHostname);
        manufacturer = findViewById(R.id.txtManufacturer);
        model = findViewById(R.id.txtModel);
        listView = findViewById(R.id.compList);
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        img = findViewById(R.id.imgOverview);
        os = findViewById(R.id.txtOS);
        //TODO: Add actual logic, todo with OS
        img.setImageResource(R.drawable.ic_pc);
        linearLayout = findViewById(R.id.linOverview);
    }

    private void getOS() {
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);

        try {
            Log.d(TAG, "Querying database");
            osList = SyskiCacheThread.getInstance().OperatingSystemThreads.GetOperatingSystems(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(osList.size() > 0) {
            sysOS = osList.get(0);
        } else {
            Log.i(TAG, "Query returned no OS Entity");
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
