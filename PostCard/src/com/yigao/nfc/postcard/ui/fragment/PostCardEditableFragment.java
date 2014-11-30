
package com.yigao.nfc.postcard.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.DataBaseUtil;
import com.yigao.nfc.postcard.database.model.ContactCompany;
import com.yigao.nfc.postcard.database.model.ContactEmail;
import com.yigao.nfc.postcard.database.model.ContactMobile;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.adapter.ContactCompanyAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactEmailAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactMobileAdapter;
import com.yigao.nfc.postcard.ui.view.DropDownLayout;
import com.yigao.nfc.postcard.ui.view.DropDownLayout.OnDropDownListener;

public class PostCardEditableFragment extends Fragment implements OnClickListener {

    private ImageView mBackView;

    private ListView mMobileListView;

    private ListView mEmailListView;

    private ListView mCompanyListView;

    private ImageButton mMobileSlidingView;

    private ImageButton mEmailSlidingView;

    private ImageButton mCompanySlidingView;

    private ImageButton mMobileAddButton;

    private ImageButton mEmailAddButton;

    private ImageButton mCompanyAddButton;

    private EditText mMobileEditText;

    private EditText mEmailEditText;

    private EditText mCompanyNameEditText;

    private EditText mCompanyAddressEditText;

    private EditText mCompanyStaffEditText;

    private EditText mCompanyDepartEditText;

    private EditText mFamilyNameEditText;

    private EditText mLastNameEditText;

    private Spinner mMobileTypeSpinner;

    private Spinner mEmailTypeSpinner;

    private Button mSaveButton;

    private PostCard mPostCard;

    private ContactMobileAdapter mMobileAdapter;

    private ContactEmailAdapter mEmailAdapter;

    private ContactCompanyAdapter mCompanyAdapter;

    private OnPostCardEditEventListener mEventListener;

    public PostCardEditableFragment() {
        super();
    }

    public PostCardEditableFragment(PostCard card) {
        super();
        mPostCard = card;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnPostCardEditEventListener) {
            mEventListener = (OnPostCardEditEventListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout = inflater.inflate(R.layout.post_card_editable_layout, null, false);

        initTitleBar(detailsLayout);
        initNameLayout(detailsLayout);
        initMobileGroupLayout(detailsLayout);
        initEmailGroupLayout(detailsLayout);
        initCompanyGroupLayout(detailsLayout);

        return detailsLayout;
    }

    private void initTitleBar(View container) {
        mBackView = (ImageView) container.findViewById(R.id.action_bar_back);
        mBackView.setOnClickListener(this);

        TextView titleView = (TextView) container.findViewById(R.id.action_bar_title);
        titleView.setText(R.string.post_card_editable_title);

        mSaveButton = (Button) container.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this);
    }

    private void initMobileGroupLayout(View container) {
        DropDownLayout mobileGroupLayout = (DropDownLayout) container
                .findViewById(R.id.mobile_group);

        View topLayout = mobileGroupLayout.setTopLayout(R.layout.contact_mobile_edit_top_layout);
        mMobileSlidingView = (ImageButton) topLayout.findViewById(R.id.mobile_sliding_button);
        mMobileAddButton = (ImageButton) topLayout.findViewById(R.id.mobile_add_button);
        mMobileAddButton.setOnClickListener(this);
        mMobileEditText = (EditText) topLayout.findViewById(R.id.mobile_edit_text);
        mMobileTypeSpinner = (Spinner) topLayout.findViewById(R.id.mobile_spinner);

        View bottomLayout = mobileGroupLayout.setBottomLayout(
                R.layout.contact_mobile_bottom_layout);
        mobileGroupLayout.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onBottomLayoutOpened() {
                mMobileSlidingView.setImageResource(R.drawable.ic_hide);
            }

            @Override
            public void onBottomLayoutClosed() {
                mMobileSlidingView.setImageResource(R.drawable.ic_show);
            }

        });

        TextView mobileEmptyView = (TextView) bottomLayout.findViewById(R.id.mobile_empty);
        mMobileListView = (ListView) bottomLayout.findViewById(R.id.mobile_list);
        mMobileListView.setEmptyView(mobileEmptyView);

        mMobileAdapter = new ContactMobileAdapter(getActivity(), mPostCard.getContactMobile());
        mMobileListView.setAdapter(mMobileAdapter);
    }

    private void initEmailGroupLayout(View container) {
        DropDownLayout emailGroupLayout = (DropDownLayout) container.findViewById(R.id.email_group);
        View topLayout = emailGroupLayout.setTopLayout(R.layout.contact_email_edit_top_layout);

        mEmailSlidingView = (ImageButton) topLayout.findViewById(R.id.email_sliding_button);
        mEmailAddButton = (ImageButton) topLayout.findViewById(R.id.email_add_button);
        mEmailAddButton.setOnClickListener(this);
        mEmailEditText = (EditText) topLayout.findViewById(R.id.email_edit_text);
        mEmailTypeSpinner = (Spinner) topLayout.findViewById(R.id.email_spinner);

        View bottomLayout = emailGroupLayout.setBottomLayout(R.layout.contact_email_bottom_layout);
        emailGroupLayout.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onBottomLayoutOpened() {
                mEmailSlidingView.setImageResource(R.drawable.ic_hide);
            }

            @Override
            public void onBottomLayoutClosed() {
                mEmailSlidingView.setImageResource(R.drawable.ic_show);
            }

        });

        TextView emailEmptyView = (TextView) bottomLayout.findViewById(R.id.email_empty);
        mEmailListView = (ListView) bottomLayout.findViewById(R.id.email_list);
        mEmailListView.setEmptyView(emailEmptyView);

        mEmailAdapter = new ContactEmailAdapter(getActivity(), mPostCard.getContactEmails());
        mEmailListView.setAdapter(mEmailAdapter);
    }

    private void initCompanyGroupLayout(View container) {
        DropDownLayout companyGroupLayout = (DropDownLayout) container
                .findViewById(R.id.company_group);
        View topLayout = companyGroupLayout.setTopLayout(R.layout.contact_company_edit_top_layout);

        mCompanySlidingView = (ImageButton) topLayout.findViewById(R.id.company_sliding_button);
        mCompanyAddButton = (ImageButton) topLayout.findViewById(R.id.company_add_button);
        mCompanyAddButton.setOnClickListener(this);
        mCompanyNameEditText = (EditText) topLayout.findViewById(R.id.company_name_edit_text);
        mCompanyAddressEditText = (EditText) topLayout.findViewById(R.id.company_address_edit_text);
        mCompanyStaffEditText = (EditText) topLayout.findViewById(R.id.company_staff_edit_text);
        mCompanyDepartEditText = (EditText) topLayout.findViewById(R.id.company_depart_edit_text);

        View bottomLayout = companyGroupLayout
                .setBottomLayout(R.layout.contact_company_bottom_layout);
        companyGroupLayout.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onBottomLayoutOpened() {
                mCompanySlidingView.setImageResource(R.drawable.ic_hide);
            }

            @Override
            public void onBottomLayoutClosed() {
                mCompanySlidingView.setImageResource(R.drawable.ic_show);
            }

        });

        TextView companyEmptyView = (TextView) bottomLayout.findViewById(R.id.company_empty);
        mCompanyListView = (ListView) bottomLayout.findViewById(R.id.company_list);
        mCompanyListView.setEmptyView(companyEmptyView);

        mCompanyAdapter = new ContactCompanyAdapter(getActivity(), mPostCard.getContactCompany());
        mCompanyListView.setAdapter(mCompanyAdapter);
    }

    private void initNameLayout(View detailsLayout) {
        mFamilyNameEditText = (EditText) detailsLayout.findViewById(R.id.family_name_edit_text);
        mFamilyNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String familyName = mFamilyNameEditText.getText().toString();
                StringBuilder name = new StringBuilder(familyName);
                name.append(mLastNameEditText.getText().toString());
                mPostCard.setContactName(name.toString());
            }
        });

        mLastNameEditText = (EditText) detailsLayout.findViewById(R.id.first_name_edit_text);
        mLastNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String familyName = mFamilyNameEditText.getText().toString();
                StringBuilder name = new StringBuilder(familyName);
                name.append(mLastNameEditText.getText().toString());
                mPostCard.setContactName(name.toString());
            }
        });

        mFamilyNameEditText.setText(mPostCard.getContactName());
    }

    private void performContactMobileAction() {
        ContactMobile entry = new ContactMobile();
        entry.setMobileMCC("");
        entry.setMobileOwnerId(mPostCard.getContactName());
        entry.setMobileNumber(mMobileEditText.getText().toString());
        entry.setMobileType(String.valueOf(mMobileTypeSpinner.getSelectedItemPosition()));

        List<ContactMobile> mobiles = new ArrayList<ContactMobile>();
        mobiles.add(entry);
        mMobileAdapter.addContactMobiles(mobiles);
    }

    private void performContactEmailAction() {
        ContactEmail entry = new ContactEmail();
        entry.setEmailOwnerId(mPostCard.getContactName());
        entry.setEmailAddress(mEmailEditText.getText().toString());
        entry.setEmailType(mEmailTypeSpinner.getSelectedItemPosition());

        List<ContactEmail> emails = new ArrayList<ContactEmail>();
        emails.add(entry);
        mEmailAdapter.addContactEmails(emails);
    }

    private void performContactCompanyAction() {
        ContactCompany company = new ContactCompany();
        company.setCompanyAddress(mCompanyAddressEditText.getText().toString());
        company.setCompanyName(mCompanyNameEditText.getText().toString());
        company.setStaff(mCompanyStaffEditText.getText().toString());
        company.setDepartment(mCompanyDepartEditText.getText().toString());
        company.setOwnerId(mPostCard.getContactName());

        List<ContactCompany> companies = new ArrayList<ContactCompany>();
        companies.add(company);
        mCompanyAdapter.addContactCompany(companies);
    }

    private void performPostCardAction() {
        String family = mFamilyNameEditText.getText().toString();
        StringBuilder name = new StringBuilder(family);
        name.append(mLastNameEditText.getEditableText().toString());
        mPostCard.setContactName(name.toString());
        mPostCard.setID(name.toString());

        mPostCard.setContactMobile(mMobileAdapter.getContactMobiles());
        mPostCard.setContactEmails(mEmailAdapter.getContactEmails());
        mPostCard.setContactCompany(mCompanyAdapter.getContactCompany());

        mPostCard.setRecordGenerateAddress("");
        mPostCard.setRecordGenerateTimeStamp(System.currentTimeMillis());

        (new DataBaseUtil(getActivity())).deletePostCard(mPostCard);
        (new DataBaseUtil(getActivity())).insertPostCard(mPostCard);
    }

    @Override
    public void onClick(View v) {
        if (v == mBackView) {
            if (mEventListener != null) {
                mEventListener.onPostCardEditBackAction();
            }
        } else if (v == mMobileAddButton) {
            performContactMobileAction();
        } else if (v == mEmailAddButton) {
            performContactEmailAction();
        } else if (v == mCompanyAddButton) {
            performContactCompanyAction();
        } else if (mSaveButton == v) {
            performPostCardAction();
            if (mEventListener != null) {
                mEventListener.onPostcardEditSaveAction(mPostCard);
            }
        }
    }

    public interface OnPostCardEditEventListener {

        public void onPostCardEditBackAction();

        public void onPostcardEditSaveAction(PostCard card);
    }
}
