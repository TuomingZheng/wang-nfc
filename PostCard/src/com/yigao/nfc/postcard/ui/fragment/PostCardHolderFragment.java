
package com.yigao.nfc.postcard.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.adapter.ContactCompanyAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactEmailAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactMobileAdapter;
import com.yigao.nfc.postcard.ui.view.DropDownLayout;
import com.yigao.nfc.postcard.ui.view.DropDownLayout.OnDropDownListener;

public class PostCardHolderFragment extends Fragment implements OnClickListener {

    private ImageButton mBackButton;
    private ImageButton mRigthButton;
    private LinearLayout mRightTopMenu;

    public PostCardHolderFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View holderLayout = inflater.inflate(R.layout.post_card_holder_layout, null, false);
        mBackButton = (ImageButton) holderLayout.findViewById(R.id.action_bar_back);
        mBackButton.setOnClickListener(this);
        mRigthButton = (ImageButton) holderLayout.findViewById(R.id.action_bar_right);
        mRigthButton.setOnClickListener(this);
        mRightTopMenu = (LinearLayout) holderLayout.findViewById(R.id.right_top_menu);
        return holderLayout;
    }

    @Override
    public void onClick(View v) {
        if(v == mRigthButton){
            if(mRightTopMenu.getVisibility() == View.VISIBLE){
                mRightTopMenu.setVisibility(View.GONE);
            }else {
                mRightTopMenu.setVisibility(View.VISIBLE);
            }
        }
    }
}
