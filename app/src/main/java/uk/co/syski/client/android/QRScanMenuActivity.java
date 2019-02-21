package uk.co.syski.client.android;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import uk.co.syski.client.android.data.thread.SyskiCacheThread;

public class QRScanMenuActivity extends AppCompatActivity {

    Button btnScan;
    TextView qrTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan_menu);
        initViews();
    }

    private void initViews(){
        btnScan = findViewById(R.id.btnScan);
        qrTxt = findViewById(R.id.txtQR);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runScanner();
            }
        });
        runScanner();
    }

    private void runScanner(){
        try {

            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, 0);

        } catch (Exception e) {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                boolean systemNotFound = true;
                String contents = data.getStringExtra("SCAN_RESULT");

                try {
                    UUID systemId = UUID.fromString(contents);
                    try {
                        systemNotFound = SyskiCacheThread.getInstance().SystemThreads.GetSystems(systemId).isEmpty();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!systemNotFound)
                    {
                        Intent intent = new Intent(this, SysOverviewActivity.class);
                        intent.putExtra("SYSTEMID", contents);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        qrTxt.setText("Error: System does not exist");
                    }
                }
                catch (IllegalArgumentException e)
                {
                    qrTxt.setText("Error: QR Code does not represent a system");
                }
            }
        }
    }
}
