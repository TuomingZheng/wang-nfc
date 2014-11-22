
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.DataBaseUtil;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.adapter.ContactListAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactListAdapter.ImportAdapterSelected;

public class PostCardImportContactsFragment extends Fragment implements OnClickListener,ImportAdapterSelected{

    private ImageButton mBackButton;
    private TextView mRigthButton;
    private TextView mImportTextView;
    private TextView mCancelTextView;
    private FragmentManager mFragmentManager;
    private ListView mListView;
    private RelativeLayout mEmptyViewLayout;
    private ArrayList<PostCard> mData = new ArrayList<PostCard>();
    private ContactListAdapter mAdapter;
    private ArrayList<Boolean> mSelectedList = new ArrayList<Boolean>();

    public PostCardImportContactsFragment() {
        super();
    }

    public PostCardImportContactsFragment(FragmentManager fm) {
        super();
        mFragmentManager = fm;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View holderLayout = inflater
                .inflate(R.layout.post_card_import_contacts_layout, null, false);
        mBackButton = (ImageButton) holderLayout.findViewById(R.id.import_action_bar_back);
        mRigthButton = (TextView) holderLayout.findViewById(R.id.import_action_bar_right);
        mImportTextView = (TextView) holderLayout.findViewById(R.id.tv_select_all);
        mCancelTextView = (TextView) holderLayout.findViewById(R.id.tv_cancel);
        mListView = (ListView) holderLayout.findViewById(R.id.contact_listview);
        mEmptyViewLayout = (RelativeLayout) holderLayout.findViewById(R.id.empty_view);

        mBackButton.setOnClickListener(this);
        mRigthButton.setOnClickListener(this);
        mImportTextView.setOnClickListener(this);
        mCancelTextView.setOnClickListener(this);
        initData();
        return holderLayout;
    }

    private void initData() {
        mListView.setEmptyView(mEmptyViewLayout);

        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                mData = new DataBaseUtil(getActivity()).queryContactFromDB(getActivity());
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
                    mAdapter = new ContactListAdapter(getActivity(), mData, mSelectedList,PostCardImportContactsFragment.this);
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
                            boolean isSelectedAll = true;
                            for (Boolean selected : mSelectedList) {
                                if (!selected) {
                                    isSelectedAll = false;
                                    break;
                                }
                            }
                            selectAll(isSelectedAll);
                        }
                    });
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == mRigthButton) {
            if(mRigthButton.getText().equals(getString(R.string.post_card_select_all))){
                mRigthButton.setText(R.string.post_card_dis_select_all);
                if (mSelectedList != null) {
                    for (int i = 0; i < mSelectedList.size(); i++) {
                        mSelectedList.set(i, true);
                    }
                }
            }else {
                Log.d("morning", "select all");
                mRigthButton.setText(R.string.post_card_select_all);
                if (mSelectedList != null) {
                    for (int i = 0; i < mSelectedList.size(); i++) {
                        mSelectedList.set(i, false);
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        } else if (v == mBackButton) {
            mFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
        } else if (v == mImportTextView) {
            ArrayList<PostCard> datas = new ArrayList<PostCard>();
            if (mSelectedList != null) {
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (mSelectedList.get(i)) {
                        datas.add(mData.get(i));
                    }
                }
            }
            boolean isInsertSuccess = new DataBaseUtil(getActivity()).insertPostCards(datas);
            Log.d("morning", "mImportTextView click 导入系统联系人 选中的个数为===" + datas.size()+"insert is success=="+isInsertSuccess);
            // 导入联系人成功 跳到holderFrament
            if (isInsertSuccess) {
                Toast.makeText(getActivity(),R.string.post_card_insert_success, Toast.LENGTH_SHORT);
                mFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
            }else {
                Toast.makeText(getActivity(),R.string.post_card_insert_failure, Toast.LENGTH_SHORT);
            }
            
        } else if (v == mCancelTextView) {
            Log.d("morning", "mCancelTextView clic1k");
            // 取消
            if (mSelectedList != null) {
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (mSelectedList.get(i)) {
                        mSelectedList.set(i, false);
                    }
                }
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void selectAll(boolean isSelectAll) {
        if(isSelectAll){
            mRigthButton.setText(R.string.post_card_dis_select_all);
        }else {
            mRigthButton.setText(R.string.post_card_select_all);
        }
    }
}
