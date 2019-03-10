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
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.adapters.StorageAdapter;
import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;
import uk.co.syski.client.android.view.SystemListMenu;

public class StorageActivity extends AppCompatActivity {

    private static final String TAG = "StorageActivity";

    List<StorageEntity> storageList;
    SharedPreferences prefs;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        initViews();
        getStorage();


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

        if(storageList.size() > 0) {
            StorageAdapter adapter = new StorageAdapter(this, storageList);
            listView.setAdapter(adapter);
        } else{
            Toast.makeText(this,"Storage not found",Toast.LENGTH_SHORT).show();
        }

    }

    private void initViews() {
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        listView = findViewById(R.id.stgListView);
    }

    private void getStorage() {
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        try {
            Log.d(TAG, "Querying database");
            storageList = SyskiCacheThread.getInstance().StorageThreads.GetStorages(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(storageList.size() <= 0){
            Log.i(TAG,"Query returned no storage entitity");
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
