package uk.co.syski.client.android.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import uk.co.syski.client.android.R;

public class CPUActivity extends AppCompatActivity {

    private static final String TAG = "CPUActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
    }
}
