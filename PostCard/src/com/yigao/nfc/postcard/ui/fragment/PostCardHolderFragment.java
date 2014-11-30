
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
    private boolean mIsNfcEnable;

    public PostCardHolderFragment() {
        super();
    }

    public PostCardHolderFragment(FragmentManager fm, boolean isNfcEnable) {
        super();
        mFragmentManager = fm;
        mIsNfcEnable = isNfcEnable;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("morning", "PostCardHolderFragment onAttach is called ");
        if (activity instanceof OnPostCardInputActionListener) {
            mInputActionListener = (OnPostCardInputActionListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    public void initData() {
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
                    for (int i = 0; i < mData.size(); i++) {
                        mSelectedList.add(false);
                    }
                    mAdapter = new PostCardListAdapter(getActivity(), mData, mSelectedList,
                            mIsNfcEnable, mInputActionListener);
                    mListView.setOnItemClickListener(new OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> arg0, View view, int position,
                                long arg3) {
                            if (mInputActionListener != null) {
                                PostCard card = mAdapter.getItem(position);
                                mInputActionListener.onReviewPostCardContact(card);
                            }
                        }
                    });
                    mListView.setAdapter(mAdapter);
                } else {
                    mMutiModeTextView.setEnabled(false);
                    mMutiModeTextView.setClickable(false);
                }
            }
        }.execute();
    }

    public void notifyDataWhenInsertNfcCard() {
        initData();
    }

    private void updateMenuLayout() {
        if (mRightTopMenu.getVisibility() == View.VISIBLE) {
            mRightTopMenu.setVisibility(View.GONE);
        } else {
            mRightTopMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mRigthButtonMenu) {
            updateMenuLayout();
        } else if (v == mMutiModeTextView) {
            updateMenuLayout();
            mAdapter.setMutiMode(true);
            mAdapter.notifyDataSetChanged();
            mRightTopMenu.setVisibility(View.GONE);
            mRightButtonDelete.setVisibility(View.VISIBLE);
            mRigthButtonMenu.setVisibility(View.GONE);
        } else if (v == mImportTextView) {
            updateMenuLayout();
            mFragmentManager
                    .beginTransaction()
                    .replace(
                            R.id.main_activity_root,
                            new PostCardImportContactsFragment(mFragmentManager))
                    .addToBackStack("Import").commitAllowingStateLoss();
            // 跳到 PostCardImportContactFragment 选择系统联系人
        } else if (v == mInputTextView) {
            updateMenuLayout();
            mInputActionListener.onInputPostCardManual();
        } else if (v == mRightButtonDelete) {
            // 删除本地postCard
            ArrayList<PostCard> datas = new ArrayList<PostCard>();
            ArrayList<Boolean> selectDatas = new ArrayList<Boolean>();
            if (mSelectedList != null) {
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (mSelectedList.get(i)) {
                        datas.add(mData.get(i));
                        selectDatas.add(mSelectedList.get(i));
                    }
                }
            }
            if (datas.size() > 0) {
                new DataBaseUtil(getActivity()).deletePostCards(datas);
                mData.removeAll(datas);
                mSelectedList.removeAll(selectDatas);
                mAdapter.setMutiMode(false);
                mAdapter.notifyDataSetChanged();
                mRightTopMenu.setVisibility(View.GONE);
                mRightButtonDelete.setVisibility(View.GONE);
                mRigthButtonMenu.setVisibility(View.VISIBLE);
            }
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

        public void onReviewPostCardContact(PostCard card);
    }
}
