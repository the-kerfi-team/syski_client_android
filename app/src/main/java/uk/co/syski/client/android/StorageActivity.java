package uk.co.syski.client.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class StorageActivity extends AppCompatActivity {

    TextView manufacturer,size,type,name;
    SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
    private void initViews() {
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        manufacturer = findViewById(R.id.txtStorageManufacturer);
        size = findViewById(R.id.txtStorageSize);
        type = findViewById(R.id.txtStorageType);
        name = findViewById(R.id.txtStoreModel);
    }
}
