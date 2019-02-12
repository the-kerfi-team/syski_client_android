package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class SysListActivity extends AppCompatActivity {

    Integer[] imageArray = {
            R.drawable.placeholder,
            R.drawable.placeholder,
            R.drawable.placeholder
    };

    String[] hostnames = {
            "winsys",
            "unixsys",
            "testsys"
    };

    String[] sysNames = {
            "Test Windows System",
            "Test UNIX System",
            "Test System"
    };

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_list);

        SysListAdapter adapter = new SysListAdapter(this,sysNames,hostnames,imageArray);

        listView = findViewById(R.id.sysList);
        listView.setAdapter(adapter);

    }



}
