package uk.co.syski.client.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.MotherboardEntity;
import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class MOBOActivity extends AppCompatActivity {

    TextView model,manufacturer,version;
    List<MotherboardEntity> moboList;
    MotherboardEntity mobo;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobo);

        initViews();
        getMOBO();


    }

    private void initViews()
    {
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        model = findViewById(R.id.txtMoboModel);
        manufacturer = findViewById(R.id.txtMoboManufacturer);
        version = findViewById(R.id.txtMoboVersion);
    }

    private void getMOBO()
    {
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        try {
            moboList = SyskiCacheThread.getInstance().MotherboardThreads.GetMotherboards(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(moboList.size() > 0){
            mobo = moboList.get(0);
        }
    }
}
