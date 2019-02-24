package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RAMActivity extends AppCompatActivity {

    TextView model,manufacturer,type,size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        initViews();
    }

    private void initViews(){
        model = findViewById(R.id.txtRamModel);
        manufacturer = findViewById(R.id.txtRamManufacturer);
        type = findViewById(R.id.txtRamType);
        size = findViewById(R.id.txtRamSize);
    }

}
