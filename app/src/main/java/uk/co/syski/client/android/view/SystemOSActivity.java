package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.OperatingSystemEntity;
import uk.co.syski.client.android.model.OperatingSystemModel;
import uk.co.syski.client.android.viewmodel.OperatingSystemViewModel;

public class SystemOSActivity extends AppCompatActivity {

    private static final String TAG = "SystemOSActivity";

    TextView name,arch,vers;
    List<OperatingSystemModel> osList;
    GridLayout gridLayout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        initViews();

        OperatingSystemViewModel viewModel = ViewModelProviders.of(this).get(OperatingSystemViewModel.class);
        viewModel.get().observe(this, new Observer<List<OperatingSystemModel>>() {
            @Override
            public void onChanged(@Nullable List<OperatingSystemModel> osEntities) {
                if (osEntities.size() > 0) {
                    name.setText(osEntities.get(0).Name);
                    arch.setText(osEntities.get(0).ArchitectureName);
                    vers.setText(osEntities.get(0).Version);
                }
            }
        });
    }

    private void initViews(){
        name = findViewById(R.id.txtOSName);
        arch = findViewById(R.id.txtOSArch);
        vers = findViewById(R.id.txtOSVers);
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
    }
}
