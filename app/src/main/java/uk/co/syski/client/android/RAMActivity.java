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

import uk.co.syski.client.android.adapters.RAMAdapter;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.view.SystemListMenu;

public class RAMActivity extends AppCompatActivity {

    private static final String TAG = "RAMActivity";
    ListView listView;
    List<RAMEntity> ramList;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);


        initViews();
        getRAM();


//        if (ram != null) {
//            model.setText(ram.ModelName);
//            manufacturer.setText(ram.ManufacturerName);
//            type.setText(ram.MemoryTypeName);
//            size.setText(""+ram.MemoryBytes);
//        } else {
//            model.setVisibility(View.INVISIBLE);
//            manufacturer.setVisibility(View.INVISIBLE);
//            type.setVisibility(View.INVISIBLE);
//            size.setVisibility(View.INVISIBLE);
//            Toast.makeText(this,"RAM not found",Toast.LENGTH_SHORT).show();
//        }

        if(ramList.size() > 0) {
            RAMAdapter adapter = new RAMAdapter(this, ramList);
            listView.setAdapter(adapter);
        } else{
            Toast.makeText(this,"RAM not found",Toast.LENGTH_SHORT).show();
        }



    }

    private void initViews(){
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        listView = findViewById(R.id.ramListView);

    }

    private void getRAM(){
        Log.d(TAG, "Querying database");
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);

        if(ramList.size() == 0){
            Log.i(TAG, "Query returned no RAM entity");
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
