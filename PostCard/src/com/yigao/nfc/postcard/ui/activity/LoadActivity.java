
package com.yigao.nfc.postcard.ui.activity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.nfc.wang.postcard.R;

public class LoadActivity extends FragmentActivity {

    private NfcAdapter mNFCAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity_layout);

        mNFCAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNFCAdapter == null) {
            Toast.makeText(this, R.string.post_card_no_nfc_hardware_error,
                    Toast.LENGTH_LONG).show();
//            finish();
//            return;
        }

        final long delayDuration = 1000L;
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                launchMainFunctionActivity();
            }
        }, delayDuration);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        final String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            handleNFCDetection(intent);
        }
    }

    private void launchMainFunctionActivity() {
        Intent intent = new Intent(this, PostCardMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void handleNFCDetection(Intent intent) {
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMsgs != null) {
            NdefMessage[] msg = new NdefMessage[rawMsgs.length];
            for (int index = 0; index < rawMsgs.length; index++) {
                msg[index] = (NdefMessage) rawMsgs[index];
            }
        }
    }
}
