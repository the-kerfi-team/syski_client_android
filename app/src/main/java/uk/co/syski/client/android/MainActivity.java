package uk.co.syski.client.android;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import uk.co.syski.client.android.data.SyskiCache;

public class MainActivity extends AppCompatActivity {
    public MainActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SyskiCache.BuildDatabase(getApplicationContext());
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openSysList(View view) {
        Intent intent = new Intent(this, SysListMenu.class);
        startActivity(intent);
    }

}