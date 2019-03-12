package uk.co.syski.client.android.view;

import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.view.adapters.RAMAdapter;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.viewmodel.SystemRAMViewModel;

public class RAMActivity extends AppCompatActivity {

    private static final String TAG = "RAMActivity";
    ListView listView;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        initViews();

        final Activity thisActivity = this;
        SystemRAMViewModel model = ViewModelProviders.of(this).get(SystemRAMViewModel.class);
        model.get().observe(this, new Observer<List<RAMEntity>>() {
            @Override
            public void onChanged(@Nullable List<RAMEntity> ramEntities) {
                RAMAdapter adapter = new RAMAdapter(thisActivity, ramEntities);
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });

    }

    private void initViews(){
        prefs = this.getSharedPreferences(getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        listView = findViewById(R.id.ramListView);

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
