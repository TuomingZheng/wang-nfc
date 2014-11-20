
package com.yigao.nfc.postcard.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.adapter.ContactCompanyAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactEmailAdapter;
import com.yigao.nfc.postcard.ui.adapter.ContactMobileAdapter;
import com.yigao.nfc.postcard.ui.view.DropDownLayout;
import com.yigao.nfc.postcard.ui.view.DropDownLayout.OnDropDownListener;

public class PostCardDetailsFragment extends Fragment implements OnClickListener {

    private ImageButton mBackButton;

    private TextView mNameView;

    private ImageView mMobileStateView;

    private ImageView mEmailStateView;

    private ImageView mCompanyStateView;

    private ImageView mEdtiableView;

    private ListView mMobileListView;

    private ListView mEmailListView;

    private ListView mCompanyListView;

    private PostCard mPostCard;

    private OnPostCardDetailsEventListener mEventListener;

    public PostCardDetailsFragment() {
        super();
    }

    public PostCardDetailsFragment(PostCard postCard) {
        super();

        mPostCard = postCard;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnPostCardDetailsEventListener) {
            mEventListener = (OnPostCardDetailsEventListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View detailsLayout = inflater.inflate(R.layout.post_card_details_layout, null, false);

        initTitleBar(detailsLayout, inflater);

        mNameView = (TextView) detailsLayout.findViewById(R.id.name_text);
        mNameView.setText(mPostCard.getContactName());

        initContactMobileLayout(detailsLayout);
        initContactEmailLayout(detailsLayout);
        initContactCompanyLayout(detailsLayout);

        return detailsLayout;
    }

    private void initTitleBar(View detailsLayout, LayoutInflater inflater) {
        mBackButton = (ImageButton) detailsLayout.findViewById(R.id.action_bar_back);
        mBackButton.setOnClickListener(this);

        TextView titleView = (TextView) detailsLayout.findViewById(R.id.action_bar_title);
        titleView.setText(R.string.post_card_details_title);

        FrameLayout rightActionContainer = (FrameLayout) detailsLayout
                .findViewById(R.id.action_bar_right_container);
        inflater.inflate(R.layout.post_card_details_right_action, rightActionContainer, true);
        mEdtiableView = (ImageView) rightActionContainer.findViewById(R.id.post_card_edtiable);
        mEdtiableView.setOnClickListener(this);
    }

    private void initContactMobileLayout(View detailsLayout) {
        DropDownLayout mobileLayout = (DropDownLayout) detailsLayout.findViewById(R.id.mobile_item);
        View topLayout = mobileLayout.setTopLayout(R.layout.contact_mobile_item_header);
        mMobileStateView = (ImageView) topLayout.findViewById(R.id.contact_mobile_state);

        View bottomLayout = mobileLayout.setBottomLayout(R.layout.contact_mobile_bottom_layout);
        mobileLayout.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onBottomLayoutOpened() {
                mMobileStateView.setImageResource(R.drawable.ic_hide);
            }

            @Override
            public void onBottomLayoutClosed() {
                mMobileStateView.setImageResource(R.drawable.ic_show);
            }

        });

        TextView mobileEmptyView = (TextView) bottomLayout.findViewById(R.id.mobile_empty);
        mMobileListView = (ListView) bottomLayout.findViewById(R.id.mobile_list);
        mMobileListView.setEmptyView(mobileEmptyView);

        ContactMobileAdapter mobileAdapter = new ContactMobileAdapter(getActivity(),
                mPostCard.getContactMobile());
        mMobileListView.setAdapter(mobileAdapter);
    }

    private void initContactEmailLayout(View detailsLayout) {
        DropDownLayout emailLayout = (DropDownLayout) detailsLayout.findViewById(R.id.email_item);
        View topLayout = emailLayout.setTopLayout(R.layout.contact_email_top_layout);
        mEmailStateView = (ImageView) topLayout.findViewById(R.id.contact_email_state);

        View bottomLayout = emailLayout.setBottomLayout(R.layout.contact_email_bottom_layout);
        emailLayout.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onBottomLayoutOpened() {
                mEmailStateView.setImageResource(R.drawable.ic_hide);
            }

            @Override
            public void onBottomLayoutClosed() {
                mEmailStateView.setImageResource(R.drawable.ic_show);
            }

        });

        TextView emailEmptyView = (TextView) bottomLayout.findViewById(R.id.email_empty);
        mEmailListView = (ListView) bottomLayout.findViewById(R.id.email_list);
        mEmailListView.setEmptyView(emailEmptyView);

        ContactEmailAdapter emailAdapter = new ContactEmailAdapter(getActivity(),
                mPostCard.getContactEmails());
        mEmailListView.setAdapter(emailAdapter);
    }

    private void initContactCompanyLayout(View detailsLayout) {
        DropDownLayout companyLayout = (DropDownLayout) detailsLayout
                .findViewById(R.id.company_item);
        View topLayout = companyLayout.setTopLayout(R.layout.contact_company_top_layout);
        mCompanyStateView = (ImageView) topLayout.findViewById(R.id.contact_company_state);

        View bottomLayout = companyLayout.setBottomLayout(R.layout.contact_company_bottom_layout);
        companyLayout.setOnDropDownListener(new OnDropDownListener() {

            @Override
            public void onBottomLayoutOpened() {
                mCompanyStateView.setImageResource(R.drawable.ic_hide);
            }

            @Override
            public void onBottomLayoutClosed() {
                mCompanyStateView.setImageResource(R.drawable.ic_show);
            }

        });

        TextView companyEmptyView = (TextView) bottomLayout.findViewById(R.id.company_empty);
        mCompanyListView = (ListView) bottomLayout.findViewById(R.id.company_list);
        mCompanyListView.setEmptyView(companyEmptyView);

        ContactCompanyAdapter companyAdapter = new ContactCompanyAdapter(getActivity(),
                mPostCard.getContactCompany());
        mCompanyListView.setAdapter(companyAdapter);
    }

    @Override
    public void onClick(View v) {
        if (mBackButton == v) {
            if (mEventListener != null) {
                mEventListener.onPostCardDetailsBackAction();
            }
        } else if (mEdtiableView == v) {
            if (mEventListener != null) {
                mEventListener.onPostcardDetailsEditAction(mPostCard);
            }
        }
    }

    public interface OnPostCardDetailsEventListener {

        public void onPostCardDetailsBackAction();

        public void onPostcardDetailsEditAction(PostCard card);
    }

}
