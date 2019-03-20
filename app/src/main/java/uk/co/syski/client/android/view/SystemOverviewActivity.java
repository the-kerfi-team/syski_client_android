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
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.VolleySingleton;
import uk.co.syski.client.android.api.requests.auth.APITokenRequest;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.entity.UserEntity;

import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.model.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.HeadedValueListAdapter;
import uk.co.syski.client.android.view.fragment.HeadedValueFragment;
import uk.co.syski.client.android.viewmodel.SystemSummaryViewModel;

public class SystemOverviewActivity extends AppCompatActivity {

    private static final String TAG = "SystemOverviewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_overview);

        SystemSummaryViewModel viewModel = ViewModelProviders.of(this).get(SystemSummaryViewModel.class);
        viewModel.get().observe(this, new Observer<SystemEntity>() {
            @Override
            public void onChanged(@Nullable SystemEntity systemEntity) {
                updateStaticUI(systemEntity);
            }
        });

        HeadedValueFragment shutdownModel = HeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "Shutdown System",
            "Tap here"
        );

        View shutdownFragment = findViewById(R.id.shutdownFragment);
        shutdownFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                shutdownOnClick(v);
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.shutdownFragment, shutdownModel).commit();

        HeadedValueFragment restartModel = HeadedValueFragment.newInstance(
            R.drawable.placeholder,
            "Restart System",
            "Tap here"
        );

        getSupportFragmentManager().beginTransaction().replace(R.id.restartFragment, restartModel).commit();

        View restartFragment = findViewById(R.id.restartFragment);
        restartFragment.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartOnClick(v);
            }
        });
    }

    private void updateStaticUI(SystemEntity systemEntity) {
        View topFragment = findViewById(R.id.topFragment);

        ImageView imageView = topFragment.findViewById(R.id.imageView);
        TextView firstHeadingView = topFragment.findViewById(R.id.firstHeadingView);
        TextView firstValueView = topFragment.findViewById(R.id.firstValueView);
        TextView secondHeadingView = topFragment.findViewById(R.id.secondHeadingView);
        TextView secondValueView = topFragment.findViewById(R.id.secondValueView);

        imageView.setImageResource(R.drawable.ic_pc);
        firstHeadingView.setText("Model");
        firstValueView.setText(systemEntity.ModelName);
        secondHeadingView.setText("Manufacturer");
        secondValueView.setText(systemEntity.ManufacturerName);

        ArrayList<HeadedValueModel> systemData = new ArrayList<>();

        systemData.add(
            new HeadedValueModel(
                R.drawable.placeholder,
                "Host Name",
                systemEntity.HostName
            )
        );
        systemData.add(
            new HeadedValueModel(
                R.drawable.ic_version,
                "Last Updated",
                systemEntity.LastUpdated.toString()
            )
        );

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(new HeadedValueListAdapter(this, systemData));

        listView = findViewById(R.id.compList);
        listView.setAdapter(new HeadedValueListAdapter(this, getComponentList()));

        setComponentOnClickListeners(listView);
    }

    private void setComponentOnClickListeners(ListView listView) {
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

    private List<HeadedValueModel> getComponentList() {
        ArrayList<HeadedValueModel> listItems = new ArrayList<>();

        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_cpu,
                "View details for",
                "CPU"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_gpu,
                "View details for",
                "RAM"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_storage,
                "View details for",
                "Storage"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_gpu,
                "View details for",
                "GPU"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_gpu,
                "View details for",
                "Motherboard"
            )
        );
        listItems.add(
            new HeadedValueModel(
                R.drawable.ic_pc,
                "View details for",
                "Operating System"
            )
        );

        return listItems;
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
