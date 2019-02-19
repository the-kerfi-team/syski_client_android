package uk.co.syski.client.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.SyskiCache;
import uk.co.syski.client.android.data.entity.System;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class MainActivity extends AppCompatActivity {
    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SyskiCache.BuildDatabase(getApplicationContext());

        new Thread(new Runnable() {
            @Override
            public void run() {
                SyskiCache.GetDatabase().clearAllTables();
            }
        }).start();


        System system = new System();
        UUID uuid = UUID.fromString("38400000-8cf0-11bd-b23e-10b96e4ef00d");
        system.Id = uuid.randomUUID();
        system.HostName = "Earth";

        System system1 = new System();
        system1.Id = uuid.randomUUID();
        system1.HostName = "Mars";

        try {
            SyskiCacheThread.getInstance().SystemThreads.InsertAll(system);
            SyskiCacheThread.getInstance().SystemThreads.InsertAll(system1);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSysList(View view) {
        Intent intent = new Intent(this, SysListMenu.class);
        startActivity(intent);
        finish();
    }

}