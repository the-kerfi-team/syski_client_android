package uk.co.syski.client.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.RAMEntity;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class RAMActivity extends AppCompatActivity {

    TextView model,manufacturer,type,size;
    List<RAMEntity> ramList;
    RAMEntity ram;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ram);

        initViews();
        getRAM();

        model.setText(ram.ModelName);
        manufacturer.setText(ram.ManufacturerName);
        type.setText(ram.MemoryTypeName);
        size.setText(""+ram.MemoryBytes);


    }

    private void initViews(){
        model = findViewById(R.id.txtRamModel);
        manufacturer = findViewById(R.id.txtRamManufacturer);
        type = findViewById(R.id.txtRamType);
        size = findViewById(R.id.txtRamSize);
        prefs = this.getSharedPreferences(
                getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
    }

    private void getRAM(){
        String sysId = prefs.getString(getString(R.string.preference_sysID_key), null);
        try {
            ramList = SyskiCacheThread.getInstance().RAMThreads.GetRAMs(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(ramList.size() > 0){
            ram = ramList.get(0);
        }
    }

}