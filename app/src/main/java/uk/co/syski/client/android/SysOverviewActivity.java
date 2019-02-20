package uk.co.syski.client.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class dest;
                switch (position) {
                    case 0: dest = cpuActivity.class;
                            break;
                    default: dest = null;
                }

                Intent intent = new Intent(SysOverviewActivity.this, dest);

                //Add extra here to identify the system
                String extra = system.Id.toString();
                intent.putExtra("SYSTEMID",extra);
                startActivity(intent);
            }
        });

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
