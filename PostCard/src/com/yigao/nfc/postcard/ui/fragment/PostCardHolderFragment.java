
package com.yigao.nfc.postcard.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.DataBaseUtil;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.adapter.PostCardListAdapter;

public class PostCardHolderFragment extends Fragment implements OnClickListener {

    private ImageButton mBackButton;
    private ImageButton mRigthButtonMenu;
    private TextView mRightButtonDelete;
    private LinearLayout mRightTopMenu;
    private TextView mImportTextView;
    private TextView mMutiModeTextView;
    private TextView mInputTextView;
    private FragmentManager mFragmentManager;
    private ListView mListView;
    private RelativeLayout mEmptyViewLayout;
    private ArrayList<PostCard> mData = new ArrayList<PostCard>();
    private PostCardListAdapter mAdapter;
    private ArrayList<Boolean> mSelectedList = new ArrayList<Boolean>();

    private OnPostCardInputActionListener mInputActionListener;

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

        if (activity instanceof OnPostCardInputActionListener) {
            mInputActionListener = (OnPostCardInputActionListener) activity;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("morning", "onResume is called ");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("morning", "onCreateView is called");
        View holderLayout = inflater.inflate(R.layout.post_card_holder_layout, null, false);
        mBackButton = (ImageButton) holderLayout.findViewById(R.id.action_bar_back);
        mRigthButtonMenu = (ImageButton) holderLayout.findViewById(R.id.action_bar_menu);
        mRightButtonDelete = (TextView) holderLayout.findViewById(R.id.action_bar_delete);
        mRightTopMenu = (LinearLayout) holderLayout.findViewById(R.id.right_top_menu);
        mImportTextView = (TextView) holderLayout.findViewById(R.id.import_contact);
        mMutiModeTextView = (TextView) holderLayout.findViewById(R.id.muti_operate);
        mInputTextView = (TextView) holderLayout.findViewById(R.id.input_contact);
        mListView = (ListView) holderLayout.findViewById(R.id.contact_listview);
        mEmptyViewLayout = (RelativeLayout) holderLayout.findViewById(R.id.empty_view);

        mBackButton.setOnClickListener(this);
        mRigthButtonMenu.setOnClickListener(this);
        mRightButtonDelete.setOnClickListener(this);
        mImportTextView.setOnClickListener(this);
        mInputTextView.setOnClickListener(this);
        mMutiModeTextView.setOnClickListener(this);
        initData();
        return holderLayout;
    }

    private void initData() {
        mListView.setEmptyView(mEmptyViewLayout);

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                mData = new DataBaseUtil(getActivity()).queryPostCardsFromLocalDB();
                if (mData.size() > 0) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    Log.d("morning", "has contact");
                    for (int i = 0; i < mData.size(); i++) {
                        mSelectedList.add(false);
                    }
                    mAdapter = new PostCardListAdapter(getActivity(), mData, mSelectedList);
                    mListView.setAdapter(mAdapter);
                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {
                            if (mSelectedList.get(position)) {
                                mSelectedList.set(position, false);
                                ((ImageView) view.findViewById(R.id.contact_icon))
                                        .setImageResource(R.drawable.btn_check_off);
                            } else {
                                mSelectedList.set(position, true);
                                ((ImageView) view.findViewById(R.id.contact_icon))
                                        .setImageResource(R.drawable.btn_check_on);
                            }
                        }
                    });
                } else {
                    mMutiModeTextView.setEnabled(false);
                    mMutiModeTextView.setClickable(false);
                }
            }
        }.execute();
    }

    public void notifyDataWhenInsertNfcCard() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v == mRigthButtonMenu) {
            if (mRightTopMenu.getVisibility() == View.VISIBLE) {
                mRightTopMenu.setVisibility(View.GONE);
            } else {
                mRightTopMenu.setVisibility(View.VISIBLE);
            }
        } else if (v == mMutiModeTextView) {
            mAdapter.setMutiMode(true);
            mAdapter.notifyDataSetChanged();
            mRightTopMenu.setVisibility(View.GONE);
            mRightButtonDelete.setVisibility(View.VISIBLE);
            mRigthButtonMenu.setVisibility(View.GONE);
        } else if (v == mImportTextView) {
            mFragmentManager
                    .beginTransaction()
                    .add(R.id.main_activity_root,
                            new PostCardImportContactsFragment(mFragmentManager))
                    .commitAllowingStateLoss();
            // 跳到 PostCardImportContactFragment 选择系统联系人
        } else if (v == mInputTextView) {
            mInputActionListener.onInputPostCardManual();
        } else if (v == mRightButtonDelete) {
            // 删除本地postCard
            ArrayList<PostCard> datas = new ArrayList<PostCard>();
            if (mSelectedList != null) {
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (mSelectedList.get(i)) {
                        datas.add(mData.get(i));
                    }
                }
            }
            new DataBaseUtil(getActivity()).deletePostCards(datas);
        } else if (v == mBackButton) {
            if (mRightButtonDelete.getVisibility() == View.VISIBLE) {
                mAdapter.setMutiMode(false);
                mAdapter.notifyDataSetChanged();
                mRightButtonDelete.setVisibility(View.GONE);
                mRigthButtonMenu.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnPostCardInputActionListener {

        public void onInputPostCardManual();

        public void onInputPostCardNFC();

        public void onInputPostCardContacts();
    }
}
