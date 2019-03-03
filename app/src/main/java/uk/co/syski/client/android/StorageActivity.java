package uk.co.syski.client.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.entity.StorageEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class StorageActivity extends AppCompatActivity {

    TextView manufacturer,size,type,name;
    List<StorageEntity> storageList;
    StorageEntity storage;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);

        initViews();
        getStorage();


        if(storage != null) {
            manufacturer.setText(storage.ManufacturerName);
            size.setText("" + storage.MemoryBytes);
            type.setText(storage.MemoryTypeName);
            name.setText(storage.ModelName);
        } else {
            manufacturer.setVisibility(View.INVISIBLE);
            size.setVisibility(View.INVISIBLE);
            type.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
            Toast.makeText(this,"Storage Info not found",Toast.LENGTH_SHORT).show();
        }

    }

    private void initViews() {
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        manufacturer = findViewById(R.id.txtStorageManufacturer);
        size = findViewById(R.id.txtStorageSize);
        type = findViewById(R.id.txtStorageType);
        name = findViewById(R.id.txtStoreModel);
    }

    private void getStorage() {
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        try {
            storageList = SyskiCacheThread.getInstance().StorageThreads.GetStorages(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(storageList.size() > 0){
            storage = storageList.get(0);
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
