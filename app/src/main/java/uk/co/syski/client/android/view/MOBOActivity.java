package uk.co.syski.client.android.view;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.List;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.viewmodel.MotherboardViewModel;

public class MOBOActivity extends AppCompatActivity {

    private static final String TAG = "MOBOActivity";
    TextView model,manufacturer,version;
    List<MotherboardEntity> moboList;
    MotherboardEntity mobo;
    SharedPreferences prefs;
    GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobo);

        initViews();


        MotherboardViewModel viewModel = ViewModelProviders.of(this).get(MotherboardViewModel.class);
        viewModel.get().observe(this, new Observer<List<MotherboardEntity>>() {
            @Override
            public void onChanged(@Nullable List<MotherboardEntity> moboEntities) {
                if (moboEntities.size() > 0)
                {
                    model.setText(moboEntities.get(0).ModelName);
                    manufacturer.setText(moboEntities.get(0).ManufacturerName);
                    version.setText(moboEntities.get(0).Version);
                }
            }
        });


    }

    private void initViews()
    {
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        model = findViewById(R.id.txtMoboModel);
        manufacturer = findViewById(R.id.txtMoboManufacturer);
        version = findViewById(R.id.txtMoboVersion);
        gridLayout = findViewById(R.id.grdMobo);
    }
}
