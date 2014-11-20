
package com.yigao.nfc.postcard.ui.fragment;

import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.app.PostCardApplication;

public class LoadingFragment extends PostCardBaseFragment {

    public LoadingFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(PostCardApplication.getInstance());
        if (nfcAdapter == null) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    showToastForLong(R.string.post_card_no_nfc_hardware_error);
                }
            }, 1000);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.load_fragment_layout, container, false);
    }
}
