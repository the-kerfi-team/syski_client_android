package uk.co.syski.client.android.view;

import android.app.PendingIntent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import uk.co.syski.client.android.R;
import uk.co.syski.client.android.data.entity.SystemEntity;
import uk.co.syski.client.android.viewmodel.SystemListViewModel;

public class NFCReceiverActivity extends AppCompatActivity {

    private static final String TAG = "CPUActivity";

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter writeTagFilters[];
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreceiver);

        prefs = this.getSharedPreferences(getString(R.string.preference_sysID_key), Context.MODE_PRIVATE);
        prefEditor = prefs.edit();

        Log.d(TAG,"Creating NFC Adapter");
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[] { tagDetected };
    }

    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }
    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = null;
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "Error occurred when encoding NFC payload");
            Log.e("UnsupportedEncoding", e.toString());
        }

        if (prefs.getBoolean("pref_developer_mode", Boolean.parseBoolean(getString(R.string.pref_developer_mode_default))))
        {
            Toast.makeText(this, "Scanned: " +text, Toast.LENGTH_LONG).show();
        }

        try {
            boolean systemNotFound = true;
            UUID systemId = UUID.fromString(text);

            Log.i(TAG, "Looking up SysID using NFC");
            SystemListViewModel model = ViewModelProviders.of(this).get(SystemListViewModel.class);
            List<SystemEntity> sys = model.get().getValue();
            for(SystemEntity system : sys){
                if(system.Id.equals(systemId)){
                    systemNotFound = false;
                }
            }

            if (!systemNotFound) {
                Intent intent = new Intent(this, SystemOverviewActivity.class);
                prefEditor.putString(getString(R.string.preference_sysID_key),systemId.toString());
                prefEditor.apply();
                startActivity(intent);
            } else {
                Toast.makeText(this, "Error: System does not exist", Toast.LENGTH_SHORT).show();
                Log.w(TAG,"NFC System Does Not Exist");
            }
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Error: NFC Tag does not represent a system", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "NFC Tag does not represent a system");
        }
    }

}
