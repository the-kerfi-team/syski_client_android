package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class SysOverviewActivity extends AppCompatActivity {

    TextView textView;
    System system;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_overview);
        initViews();

        String sysId = this.getIntent().getStringExtra("SYSTEMID");


        textView.setText(sysId);

    }

    private void initViews(){
        textView = findViewById(R.id.txtDetails);
    }
}
