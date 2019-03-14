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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.adapters.SysListOverviewAdapter;
import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.UserEntity;

import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.model.OperatingSystemModel;
import uk.co.syski.client.android.viewmodel.SystemSummaryViewModel;

public class SystemOverviewActivity extends AppCompatActivity {

    private static final String TAG = "SystemOverviewActivity";

    TextView host, manufacturer, model, os;
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
            "Motherboard",
            "OS"
    };

    //TODO: Replace placeholders with corresponding icons
    Integer[] images = {
            R.drawable.ic_cpu,
            //TODO: Find better Icon
            R.drawable.ic_gpu,
            R.drawable.ic_storage,
            R.drawable.ic_gpu,
            //TODO: Find better Icon
            R.drawable.ic_gpu,
            R.drawable.ic_pc
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_overview);
        initViews();

        SystemSummaryViewModel viewModel = ViewModelProviders.of(this).get(SystemSummaryViewModel.class);
        viewModel.get().observe(this, new Observer<SystemEntity>() {
            @Override
            public void onChanged(@Nullable SystemEntity systemEntity) {
                host.setText(systemEntity.HostName);
                manufacturer.setText(systemEntity.ManufacturerName);
                model.setText(systemEntity.ModelName);
            }
        });

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
                    case 2: dest = SystemStorageActivity.class;
                            break;
                    case 3: dest = GPUActivity.class;
                            break;
                    case 4: dest = MOBOActivity.class;
                            break;
                    case 5: dest = SystemOSActivity.class;
                            break;
                    default: dest = null;
                }

                Intent intent = new Intent(SystemOverviewActivity.this, dest);
                startActivity(intent);
            }
        });

    }

    public void shutdownOnClick(View v) {
        UserEntity user = null;
        user = Repository.getInstance().getUserRepository().getUser();
        if (user == null || user.TokenExpiry == null || Calendar.getInstance().getTime().after(user.TokenExpiry))
        {
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new APITokenRequest(getApplicationContext(), user.Id));
        }
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new APISystemShutdownRequest(getApplicationContext(), system.Id));
    }

    public void restartOnClick(View v) {
        UserEntity user = null;
        user = Repository.getInstance().getUserRepository().getUser();
        if (user == null || user.TokenExpiry == null || Calendar.getInstance().getTime().after(user.TokenExpiry))
        {
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new APITokenRequest(getApplicationContext(), user.Id));
        }
        //VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(new APISystemRestartRequest(getApplicationContext(), system.Id));
    }

    private void initViews(){
        host = findViewById(R.id.txtHostname);
        manufacturer = findViewById(R.id.txtManufacturer);
        model = findViewById(R.id.txtModel);
        listView = findViewById(R.id.compList);
        img = findViewById(R.id.imgOverview);
        //TODO: Add actual logic, todo with OS
        img.setImageResource(R.drawable.ic_pc);
        linearLayout = findViewById(R.id.linOverview);
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
