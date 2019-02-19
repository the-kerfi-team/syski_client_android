package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SysOverviewActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sys_overview);
        initViews();

        String ext = getIntent().getStringExtra("placeholdertxt");
        textView.setText(ext);

    }

    private void initViews(){
        textView = findViewById(R.id.txtDetails);
    }
}
