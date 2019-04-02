package uk.co.syski.client.android.view;

import android.app.PendingIntent;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.api.APIThread;
import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.repository.Repository;
import uk.co.syski.client.android.model.fragment.HeadedValueModel;
import uk.co.syski.client.android.view.adapter.HeadedValueListAdapter;
import uk.co.syski.client.android.viewmodel.SystemListViewModel;

public class SystemListMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "SystemListMenu";
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_list_menu);

        //Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Setup drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        prefs = this.getSharedPreferences(getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        prefEditor = prefs.edit();

        //Setup List.
        SystemListViewModel model = ViewModelProviders.of(this).get(SystemListViewModel.class);
        model.get().observe(this, new Observer<List<SystemEntity>>() {
            @Override
            public void onChanged(@Nullable final List<SystemEntity> systemEntities) {
                setupList(systemEntities);
            }
        });

        Intent intent = new Intent(this, SystemListMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        //Firebase Tests
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
                        Toast.makeText(SystemListMenu.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void setupList(final List<SystemEntity> systemEntities) {
        ArrayList<HeadedValueModel> listItems = new ArrayList<>();
        for (int i = 0; i < systemEntities.size(); i++)
            listItems.add(new HeadedValueModel(R.drawable.ic_pc, "View details for", systemEntities.get(i).HostName));

        ListView listView = findViewById(R.id.sysList);
        listView.setAdapter(new HeadedValueListAdapter(this, listItems));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSystemOverview(systemEntities.get(position).Id.toString());
            }
        });
    }

    private void openSystemOverview(String systemId) {
        Intent intent = new Intent(SystemListMenu.this, SystemOverviewActivity.class);
        prefEditor.putString(getString(R.string.preference_sysID_key), systemId);
        prefEditor.apply();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sys_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_qr) {
            initQR();
        } else if (id == R.id.nav_settings) {
            //Handle Settings
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
        } else if (id == R.id.nav_logout) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SyskiCache.GetDatabase().clearAllTables();
                }
            }).start();
            Repository.getInstance().getUserRepository().setActiveUserId(null);
            APIThread.getInstance(getApplicationContext()).disable();
            prefEditor.remove(getString(R.string.preference_sysID_key)).commit();
            getSharedPreferences(getString(R.string.preference_usrID_key), Context.MODE_PRIVATE).edit().remove(getString(R.string.preference_usrID_key)).commit();
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //Unhighlight item
        return false;
    }

    private void initQR() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan a QR Code");
        integrator.setCameraId(0);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
            if (result.getContents() == null) {
                if (sp.getBoolean("pref_developer_mode", Boolean.parseBoolean(getString(R.string.pref_developer_mode_default)))) {
                    Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
                }
            } else {
                if (sp.getBoolean("pref_developer_mode", Boolean.parseBoolean(getString(R.string.pref_developer_mode_default)))) {
                    Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
                try {
                    boolean systemFound = false;
                    UUID systemId = UUID.fromString(result.getContents());

                    SystemListViewModel model = ViewModelProviders.of(this).get(SystemListViewModel.class);

                    //Checks if system exists
                    List<SystemEntity> sys = model.get().getValue();
                    for (SystemEntity system : sys) {
                        if (system.Id.equals(systemId)) {
                            systemFound = true;
                        }
                    }

                    if (systemFound) {
                        Intent intent = new Intent(this, SystemOverviewActivity.class);
                        prefEditor.putString(getString(R.string.preference_sysID_key), systemId.toString());
                        prefEditor.apply();
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Error: System does not exist", Toast.LENGTH_LONG).show();
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(this, "Error: QR Code does not represent a system", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
