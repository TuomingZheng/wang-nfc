
package com.yigao.nfc.postcard.ui.activity;

import java.io.IOException;
import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.DataBaseUtil;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.fragment.LoadingFragment;
import com.yigao.nfc.postcard.ui.fragment.PostCardDetailsFragment;
import com.yigao.nfc.postcard.ui.fragment.PostCardEditableFragment;
import com.yigao.nfc.postcard.ui.fragment.PostCardHolderFragment;

public class PostCardMainActivity extends FragmentActivity implements OnClickListener {

    private PostCardDetailsFragment mDetailsFragment;
    private PostCardEditableFragment mEditableFragment;
    private PostCardHolderFragment mHolderFragment;

    // 名片夹的layout
    private LinearLayout mPostHolderLayout;
    // 名片夹的imageview
    private ImageView mPostHolderImageView;
    // 拍照的button
    private Button mCaptureButton;

    private Handler mMainHandler;

    private NfcAdapter mNfcAdapter;

    private boolean mWriteMode;

    private PostCard mWritePostCard;

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        mMainHandler = new Handler();

        launchLoadingFragment();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initNfc();
    }

    private void initNfc() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter == null) {
            Toast.makeText(this, R.string.post_card_no_nfc_hardware_error,
                    Toast.LENGTH_LONG).show();
            return;
        }
        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(
                NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (MalformedMimeTypeException e) {
        }
        mNdefExchangeFilters = new IntentFilter[] {
                ndefDetected
        };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(
                NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] {
                tagDetected
        };

        // 进入的时候 设为NFC只读模式
        disableTagWriteMode();
        enableNdefExchangeMode();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.d("morning", "onNewIntent");
        // NDEF exchange mode
        if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.d("morning", "onNewIntent ACTION_NDEF_DISCOVERED");
            NdefMessage[] msgs = getNdefMessages(intent);
            String body = new String(msgs[0].getRecords()[0].getPayload());
            Gson gson = new Gson();
            PostCard postCard = gson.fromJson(body, PostCard.class);
            if (postCard != null) {
                Log.d("morning", "读取到NFC联系人===" + postCard.getContactName());
                ArrayList<PostCard> list = new ArrayList<PostCard>();
                list.add(postCard);
                new DataBaseUtil(this).insertPostCards(list);
                mHolderFragment.notifyDataWhenInsertNfcCard();
            }
        }

        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Log.d("morning", "onNewIntent ACTION_TAG_DISCOVERED");
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            writeTag(getNoteAsNdef(), detectedTag);
        }
    }

    boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;

        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();

                if (!ndef.isWritable()) {
                    toast("Tag is read-only.");
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    toast("Tag capacity is " + ndef.getMaxSize() + " bytes, message is " + size
                            + " bytes.");
                    return false;
                }

                ndef.writeNdefMessage(message);
                toast("写入数据成功.");
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        toast("Formatted tag and wrote message");
                        return true;
                    } catch (IOException e) {
                        toast("Failed to format tag.");
                        return false;
                    }
                } else {
                    toast("Tag doesn't support NDEF.");
                    return false;
                }
            }
        } catch (Exception e) {
            toast("写入数据失败");
        }

        return false;
    }

    private NdefMessage getNoteAsNdef() {
        if (mWritePostCard == null) {
            return null;
        }
        Gson gson = new Gson();
        // String postCard = gson.toJson(mWritePostCard);
        String postCard = gson.toJson(mWritePostCard, PostCard.class);
        Log.d("morning", "write post card is ==="
                + gson.fromJson(postCard, PostCard.class).getContactName());
        byte[] textBytes = postCard.getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }

    private void toast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT);
    }

    public void setWriteMode(boolean writeMode, PostCard postCard) {
        Log.d("morning", "setWriteMode is " + writeMode);
        mWriteMode = writeMode;
        if (writeMode) {
            mWritePostCard = postCard;
            enableTagWriteMode();
            disableNdefExchangeMode();
        }
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    private void disableNdefExchangeMode() {
        mNfcAdapter.disableForegroundDispatch(this);
    }

    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] {
                tagDetected
        };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
                        msg
                };
            }
        } else {
            Log.d("morning", "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void initView() {
        mPostHolderImageView = (ImageView) findViewById(R.id.post_card_holder_iv);
        mPostHolderLayout = (LinearLayout) findViewById(R.id.post_card_holder);
        mCaptureButton = (Button) findViewById(R.id.post_card_capture_button);
        mPostHolderLayout.setOnClickListener(this);
        mCaptureButton.setOnClickListener(this);
    }

    private void launchLoadingFragment() {
        LoadingFragment loadingFragment = new LoadingFragment();

        final FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.loading_fragment_holder, loadingFragment)
                .addToBackStack("Loading").commitAllowingStateLoss();

        final long hideLoadingFragmentDelay = 1000L;
        mMainHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                fm.popBackStack();
                launchHolderFragment(fm);
            }
        }, hideLoadingFragmentDelay);
    }

    private void launchHolderFragment(FragmentManager fm) {
        mHolderFragment = new PostCardHolderFragment(fm);
        fm.beginTransaction().add(R.id.main_activity_root, mHolderFragment)
                .commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        if (view == mPostHolderLayout) {
            // 点击名片夹
        } else if (view == mCaptureButton) {
            // TODO 点击拍照

        }
    }
}
