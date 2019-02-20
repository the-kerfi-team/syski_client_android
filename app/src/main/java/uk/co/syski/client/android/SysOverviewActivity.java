package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.adapters.SysListOverviewAdapter;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class SysOverviewActivity extends AppCompatActivity {

    TextView textView;
    System system;
    ListView listView;

    String[] listItems = {
            "System",
            "CPU"
    };
    Integer[] images = {
            R.drawable.placeholder,
            R.drawable.placeholder
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_overview);
        initViews();

        String sysId = this.getIntent().getStringExtra("SYSTEMID");
        system = getSystem(sysId);

        textView.setText(system.HostName);

        SysListOverviewAdapter adapter = new SysListOverviewAdapter(this, images, listItems);
        listView.setAdapter(adapter);

    }

    private System getSystem(String sysId){
        List<System> sysList = null;
        try {
            sysList = SyskiCacheThread.getInstance().SystemThreads.GetSystems(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        system = sysList.get(0);
        return system;
    }

    private void initViews(){
        textView = findViewById(R.id.txtDetails);
        listView = findViewById(R.id.compList);
    }
}
