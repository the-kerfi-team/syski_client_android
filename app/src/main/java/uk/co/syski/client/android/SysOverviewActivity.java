package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.adapters.SysListOverviewAdapter;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class SysOverviewActivity extends AppCompatActivity {

    TextView textView;
    SystemEntity systemEntity;
    ListView listView;

    String[] listItems = {
            "SystemEntity",
            "CPUEntity"
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
        systemEntity = getSystem(sysId);

        textView.setText(systemEntity.HostName);

        SysListOverviewAdapter adapter = new SysListOverviewAdapter(this, images, listItems);
        listView.setAdapter(adapter);

    }

    private SystemEntity getSystem(String sysId){
        List<SystemEntity> sysList = null;
        try {
            sysList = SyskiCacheThread.getInstance().SystemThreads.GetSystems(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        systemEntity = sysList.get(0);
        return systemEntity;
    }

    private void initViews(){
        textView = findViewById(R.id.txtDetails);
        listView = findViewById(R.id.compList);
    }
}
