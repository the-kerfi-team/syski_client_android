package uk.co.syski.client.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.entity.CPU;
import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class cpuActivity extends AppCompatActivity {

    List<CPU> cpuList;
    TextView model,manufacturer,architecture,clock,core,thread;
    CPU cpu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        initViews();

        String sysId = this.getIntent().getStringExtra("SYSTEMID");

        try {
            cpuList = SyskiCacheThread.getInstance().CPUThreads.GetCPUs(UUID.fromString(sysId));
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(cpuList.size() > 0) {
            cpu = cpuList.get(0);
            model.setText(cpu.ModelName);
            manufacturer.setText(cpu.ManufacturerName);
            architecture.setText(cpu.ArchitectureName);
            clock.setText(cpu.ClockSpeed);
            core.setText(cpu.CoreCount);
            thread.setText(cpu.ThreadCount);
        }

    }

    private void initViews() {
        model = findViewById(R.id.cpuModel);
        manufacturer = findViewById(R.id.cpuMan);
        architecture = findViewById(R.id.cpuArch);
        clock = findViewById(R.id.cpuClock);
        core = findViewById(R.id.cpuCore);
        thread = findViewById(R.id.cpuThread);
    }
}
