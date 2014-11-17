
package com.yigao.nfc.postcard.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.fragment.PostCardDetailsFragment;
import com.yigao.nfc.postcard.ui.fragment.PostCardEditableFragment;

public class PostCardMainActivity extends FragmentActivity {

    private PostCardDetailsFragment mDetailsFragment;

    private PostCardEditableFragment mEditableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        PostCard postCard = new PostCard();
        postCard.setContactName("Jim");
        mEditableFragment = new PostCardEditableFragment(postCard);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.main_activity_root, mEditableFragment)
                .commitAllowingStateLoss();
    }
}
