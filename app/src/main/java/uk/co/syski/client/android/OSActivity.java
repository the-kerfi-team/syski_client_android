package uk.co.syski.client.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.GPUEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;
import uk.co.syski.client.android.model.OperatingSystemModel;

public class OSActivity extends AppCompatActivity {

    private static final String TAG = "OSActivity";

    TextView name,arch,vers;
    List<OperatingSystemModel> osList;
    OperatingSystemModel sysOS;
    GridLayout gridLayout;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_os);

        initViews();
        getOS();

        if(sysOS != null) {
            name.setText(sysOS.Name);
            arch.setText(sysOS.ArchitectureName);
            vers.setText(sysOS.Version);
        }else {
            Toast.makeText(this,"OS not found",Toast.LENGTH_SHORT).show();
        }

    }

    private void initViews(){
        name = findViewById(R.id.txtOSName);
        arch = findViewById(R.id.txtOSArch);
        vers = findViewById(R.id.txtOSVers);
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
    }

    private void getOS(){
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        try {
            Log.d(TAG, "Querying database");
            osList = SyskiCacheThread.getInstance().OperatingSystemThreads.GetOperatingSystems(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(osList.size() > 0){
            sysOS = osList.get(0);
        } else{
            Log.w(TAG, "Query returned no OS entity");
        }
    }
}
