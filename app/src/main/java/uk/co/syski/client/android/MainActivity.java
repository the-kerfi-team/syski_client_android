package uk.co.syski.client.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.arch.persistence.room.*;
import uk.co.syski.client.android.data.CacheDatabase;

public class MainActivity extends AppCompatActivity {

    public final CacheDatabase Database = Room.databaseBuilder(getApplicationContext(), CacheDatabase.class, "SyskiCache").build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSysList(View view){
        Intent intent = new Intent(this, SysListMenu.class);
        startActivity(intent);
    }
}
