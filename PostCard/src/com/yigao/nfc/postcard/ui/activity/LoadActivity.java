
package com.yigao.nfc.postcard.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;

import com.nfc.wang.postcard.R;

public class LoadActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_activity_layout);

        final long delayDuration = 1000L;
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                launchMainFunctionActivity();
            }
        }, delayDuration);
    }

    private void launchMainFunctionActivity() {
        Intent intent = new Intent(this, PostCardMainActivity.class);
        startActivity(intent);
        finish();
    }
}
