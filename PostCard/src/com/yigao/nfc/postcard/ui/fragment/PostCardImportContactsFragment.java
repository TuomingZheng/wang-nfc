
package com.yigao.nfc.postcard.ui.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.Data;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.DataBaseUtil;
import com.yigao.nfc.postcard.database.model.ContactCompany;
import com.yigao.nfc.postcard.database.model.ContactEmail;
import com.yigao.nfc.postcard.database.model.ContactMobile;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.adapter.ContactListAdapter;

public class PostCardImportContactsFragment extends Fragment implements OnClickListener {

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
        View holderLayout = inflater.inflate(R.layout.post_card_import_contacts_layout, null, false);
        mBackButton = (ImageButton) holderLayout.findViewById(R.id.import_action_bar_back);
        mRigthButton = (TextView) holderLayout.findViewById(R.id.import_action_bar_right);
        mImportTextView = (TextView) holderLayout.findViewById(R.id.tv_select_all);
        mCancelTextView = (TextView) holderLayout.findViewById(R.id.tv_cancel);
        mListView = (ListView) holderLayout.findViewById(R.id.contact_listview);
        mEmptyViewLayout =(RelativeLayout) holderLayout.findViewById(R.id.empty_view);
        
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
                // 获得所有的联系人
                Cursor cur = getActivity().getContentResolver().query(
                        ContactsContract.Contacts.CONTENT_URI,
                        null,
                        null,
                        null,
                        ContactsContract.Contacts.DISPLAY_NAME
                                + " COLLATE LOCALIZED ASC");
                // 循环遍历
                if (cur.moveToFirst()) {
                    int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
                    int displayNameColumn = cur
                            .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                     do {
                        PostCard postCard = new PostCard();
//                        postCard.set
                        // 获得联系人的ID号
                        String contactId = cur.getString(idColumn);
                        postCard.setContactIdentification(contactId);
                        // 获得联系人姓名
                        String disPlayName = cur.getString(displayNameColumn);
                        ArrayList<ContactEmail> contactEmails = new ArrayList<ContactEmail>();
                        ArrayList<ContactMobile> contactMobiles = new ArrayList<ContactMobile>();
                        ArrayList<ContactCompany> contactCompanies = new ArrayList<ContactCompany>();
                        postCard.setContactName(disPlayName);

                        // 查看该联系人有多少个电话号码。如果没有这返回值为0
                        int phoneCount = cur
                                .getInt(cur
                                        .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        Log.i("morning", disPlayName);
                        if (phoneCount > 0) {
                            // 获得联系人的电话号码
                            Cursor phones = getActivity().getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                            + " = " + contactId, null, null);
                            if (phones.moveToFirst()) {
                                do {
                                    ContactMobile contactMobile = new ContactMobile();
                                    // 遍历所有的电话号码
                                    String phoneNumber = phones
                                            .getString(phones
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                    String phoneType = phones
                                            .getString(phones
                                                    .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                                    Log.i("morning", phoneNumber);
                                    Log.i("morning", phoneType);
                                    contactMobile.setMobileOwnerId(contactId);
                                    contactMobile.setMobileType(phoneType);
                                    contactMobile.setMobileNumber(phoneNumber);
                                    contactMobiles.add(contactMobile);
                                } while (phones.moveToNext());
                            }
                        }

                        // 获取该联系人邮箱
                        Cursor emails = getActivity().getContentResolver().query(
                                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                        + " = " + contactId, null, null);
                        if (emails.moveToFirst()) {
                            do {
                                ContactEmail contactEmail = new ContactEmail();
                                // 遍历所有的电话号码
                                String emailType = emails
                                        .getString(emails
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                                String emailValue = emails
                                        .getString(emails
                                                .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                                Log.i("morning", emailType);
                                Log.i("morning", emailValue);
                                contactEmail.setEmailOwnerId(contactId);
                                contactEmail.setEmailAddress(emailValue);
//                                contactEmail.setEmailType(emailType);
                                contactEmails.add(contactEmail);
                            } while (emails.moveToNext());
                        }

                        // 获取该联系人IM
//                        Cursor IMs = getContentResolver().query(
//                                Data.CONTENT_URI,
//                                new String[] {
//                                        Data._ID, Im.PROTOCOL, Im.DATA
//                                },
//                                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
//                                        + Im.CONTENT_ITEM_TYPE + "'",
//                                new String[] {
//                                    contactId
//                                }, null);
//                        if (IMs.moveToFirst()) {
//                            do {
//                                String protocol = IMs.getString(IMs
//                                        .getColumnIndex(Im.PROTOCOL));
//                                String date = IMs
//                                        .getString(IMs.getColumnIndex(Im.DATA));
//                                Log.i("protocol", protocol);
//                                Log.i("date", date);
//                            } while (IMs.moveToNext());
//                        }

                        // 获取该联系人地址
//                        Cursor address = getActivity().getContentResolver()
//                                .query(
//                                        ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
//                                        null,
//                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
//                                                + " = " + contactId, null, null);
//                        if (address.moveToFirst()) {
//                            do {
//                                // 遍历所有的地址
//                                String street = address
//                                        .getString(address
//                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
//                                String city = address
//                                        .getString(address
//                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
//                                String region = address
//                                        .getString(address
//                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
//                                String postCode = address
//                                        .getString(address
//                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
//                                String formatAddress = address
//                                        .getString(address
//                                                .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
//                                Log.i("morning", street);
//                                Log.i("morning", city);
//                                Log.i("morning", region);
//                                Log.i("morning", postCode);
//                                Log.i("morning", formatAddress);
//                            } while (address.moveToNext());
//                        }
                        
                        // 获取该联系人公司
                        Cursor organizations = getActivity().getContentResolver().query(
                                Data.CONTENT_URI,
                                new String[] {
                                        Data._ID, Organization.COMPANY,
                                        Organization.TITLE
                                },
                                Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                        + Organization.CONTENT_ITEM_TYPE + "'",
                                new String[] {
                                    contactId
                                }, null);
                        if (organizations.moveToFirst()) {
                            do {
                                ContactCompany contactCompany = new ContactCompany();
                                String company = organizations.getString(organizations
                                        .getColumnIndex(Organization.COMPANY));
                                String title = organizations.getString(organizations
                                        .getColumnIndex(Organization.TITLE));
                                contactCompany.setCompanyName(company);
                                Log.i("morning", company);
                                Log.i("morning", title);
                                contactCompanies.add(contactCompany);
                            } while (organizations.moveToNext());
                        }
                        mData.add(postCard);
                    } while (cur.moveToNext());
                }
                if(mData.size() > 0){
                    return true;
                }else {
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
                    mAdapter = new ContactListAdapter(getActivity(), mData,mSelectedList);
                    mListView.setAdapter(mAdapter);
                } 
            }
        }.execute();
    }

    @Override
    public void onClick(View v) {
        if (v == mRigthButton) {
            
        } else if (v == mBackButton) {
            mFragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
        } else if (v == mImportTextView) {
            ArrayList<PostCard> datas = new ArrayList<PostCard>();
            if(mSelectedList!=null){
                for(int i=0;i<mSelectedList.size();i++){
                    if(mSelectedList.get(i)){
                        datas.add(mData.get(i));
                    }
                }
            }
            Log.d("morning", "mImportTextView click 导入系统联系人 选中的个数为==="+datas.size());
            new DataBaseUtil(getActivity()).insertPostCards(datas);
            new DataBaseUtil(getActivity()).queryPostCardsFromLocalDB();
            // 导入联系人
        } else if (v == mCancelTextView) {
            Log.d("morning", "mCancelTextView clic1k");
            // 取消
        }
    }
}
