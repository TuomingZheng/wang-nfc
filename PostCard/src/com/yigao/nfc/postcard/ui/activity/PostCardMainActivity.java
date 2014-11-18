
package com.yigao.nfc.postcard.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.fragment.PostCardDetailsFragment;
import com.yigao.nfc.postcard.ui.fragment.PostCardEditableFragment;

public class PostCardMainActivity extends FragmentActivity implements OnClickListener {

    private PostCardDetailsFragment mDetailsFragment;
    private PostCardEditableFragment mEditableFragment;
    
    //名片夹的layout
    private LinearLayout mPostHolderLayout;
    //名片夹的imageview
    private ImageView mPostHolderImageView;
    // 拍照的button
    private Button mCaptureButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);
        
        initView();

        PostCard postCard = new PostCard();
        postCard.setContactName("Jim");
        mEditableFragment = new PostCardEditableFragment(postCard);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.main_activity_root, mEditableFragment)
                .commitAllowingStateLoss();
    }

    private void initView() {
        mPostHolderImageView = (ImageView) findViewById(R.id.post_card_holder_iv);
        mPostHolderLayout = (LinearLayout) findViewById(R.id.post_card_holder);
        mCaptureButton = (Button) findViewById(R.id.post_card_capture_button);
        mPostHolderLayout.setOnClickListener(this);
        mCaptureButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == mPostHolderLayout){
            // 点击名片夹
        }else if (view == mCaptureButton) {
            //TODO 点击拍照
            
        }
    }
}
