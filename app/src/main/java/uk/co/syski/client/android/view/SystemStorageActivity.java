package uk.co.syski.client.android.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.view.adapters.RAMAdapter;
import uk.co.syski.client.android.view.adapters.StorageAdapter;
import uk.co.syski.client.android.viewmodel.SystemStorageViewModel;

public class SystemStorageActivity extends AppCompatActivity {

    private static final String TAG = "SystemStorageActivity";

    List<StorageEntity> storageList;
    SharedPreferences prefs;
    ListView listView;
    StorageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        initViews();
        adapter = new StorageAdapter(this);
        listView.setAdapter(adapter);

        final Activity thisActivity = this;
        SystemStorageViewModel model = ViewModelProviders.of(this).get(SystemStorageViewModel.class);
        model.get().observe(this, new Observer<List<StorageEntity>>() {
            @Override
            public void onChanged(@Nullable List<StorageEntity> storageEntities) {
                adapter.setDataSet(storageEntities);
            }
        });

//        if(storage != null) {
//            manufacturer.setText(storage.ManufacturerName);
//            size.setText("" + storage.MemoryBytes);
//            type.setText(storage.MemoryTypeName);
//            name.setText(storage.ModelName);
//        } else {
//            //TODO: Once test data is available, change to remove views based on cpu fields
//            gridLayout.removeAllViewsInLayout();
//            Toast.makeText(this,"Storage Info not found",Toast.LENGTH_SHORT).show();
//        }

    }

    private void initViews() {
        listView = findViewById(R.id.stgListView);
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
