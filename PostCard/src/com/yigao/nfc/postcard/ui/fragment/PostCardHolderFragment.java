
package com.yigao.nfc.postcard.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfc.wang.postcard.R;

public class PostCardHolderFragment extends Fragment implements OnClickListener {

    private ImageButton mBackButton;
    private ImageButton mRigthButton;
    private LinearLayout mRightTopMenu;
    private TextView mImportTextView;
    private TextView mInputTextView;
    private FragmentManager mFragmentManager;

    public PostCardHolderFragment() {
        super();
    }
    
    public PostCardHolderFragment(FragmentManager fm) {
        super();
        mFragmentManager = fm;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View holderLayout = inflater.inflate(R.layout.post_card_holder_layout, null, false);
        mBackButton = (ImageButton) holderLayout.findViewById(R.id.action_bar_back);
        mRigthButton = (ImageButton) holderLayout.findViewById(R.id.action_bar_right);
        mRightTopMenu = (LinearLayout) holderLayout.findViewById(R.id.right_top_menu);
        mImportTextView = (TextView) holderLayout.findViewById(R.id.import_contact);
        mInputTextView = (TextView) holderLayout.findViewById(R.id.input_contact);
        
        mBackButton.setOnClickListener(this);
        mRigthButton.setOnClickListener(this);
        mImportTextView.setOnClickListener(this);
        mInputTextView.setOnClickListener(this);
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
        }else if (v == mBackButton) {
            Log.d("morning", "mBackButton click");
        }else if (v == mImportTextView) {
            mFragmentManager.beginTransaction().add(R.id.main_activity_root, new PostCardImportContactsFragment(mFragmentManager)).commitAllowingStateLoss();
            // 跳到 PostCardImportContactFragment  选择系统联系人
        }else if (v == mInputTextView) {
            // 跳到 PostCardInputContactFragment   手动输入联系人
        }
    }
}
