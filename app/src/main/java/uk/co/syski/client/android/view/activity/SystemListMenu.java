package uk.co.syski.client.android.view.activity;

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
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.model.database.SyskiCache;
import uk.co.syski.client.android.model.database.entity.SystemEntity;
import uk.co.syski.client.android.model.repository.Repository;
import uk.co.syski.client.android.view.adapter.recyclerview.SystemListAdapter;
import uk.co.syski.client.android.view.menu.SystemListOptionsMenu;
import uk.co.syski.client.android.viewmodel.SystemListViewModel;

/**
 * Activity displaying a list of a user's systems
 */
public class SystemListMenu extends SyskiActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "SystemListMenu";
    SharedPreferences prefs;
    SharedPreferences.Editor prefEditor;

    private SystemListViewModel viewModel;
    private List<SystemEntity> systemEntityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_list_menu);

        optionsMenu = new SystemListOptionsMenu();

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

        // Setup ListView
        final RecyclerView listView = findViewById(R.id.sysList);

        listView.setLayoutManager(new GridLayoutManager(this, 1));

        final SystemListAdapter adapter = new SystemListAdapter(this);
        listView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(SystemListViewModel.class);
        viewModel.get().observe(this, new Observer<List<SystemEntity>>() {
            @Override
            public void onChanged(@Nullable List<SystemEntity> systemEntities) {
                adapter.setData(systemEntities);
            }
        });

        ItemTouchHelper.SimpleCallback systemListCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // TODO: Delete System from API
                adapter.removeItem(viewHolder.getAdapterPosition());
                SystemEntity systemEntity = adapter.getItem(viewHolder.getAdapterPosition());
                viewModel.delete(systemEntity);

                // TODO: Snackbar Undo Functionality
                Snackbar snackbar = Snackbar.make(listView,"System Deleted", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        };

        new ItemTouchHelper(systemListCallback).attachToRecyclerView(listView);

        Intent intent = new Intent(this, SystemListMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);



        //Firebase
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
                        Log.d(TAG, "FCM " + msg);
                        //Toast.makeText(SystemListMenu.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

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

                    List<SystemEntity> sys = viewModel.get().getValue();
                    if (sys != null)
                    {
                        for(SystemEntity system : sys){
                            if(system.Id.equals(systemId)){
                                systemFound = true;
                            }
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
