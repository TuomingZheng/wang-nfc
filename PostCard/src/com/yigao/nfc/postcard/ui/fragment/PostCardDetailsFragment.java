
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
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.view.DropDownLayout;
import com.yigao.nfc.postcard.ui.view.DropDownLayout.OnDropDownListener;

public class PostCardDetailsFragment extends Fragment implements OnClickListener {

    private ImageButton mBackButton;

    private TextView mNameView;

    private ImageView mMobileStateView;

    private ImageView mEmailStateView;

    private ImageView mCompanyStateView;

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
        mBackButton = (ImageButton) detailsLayout.findViewById(R.id.action_bar_back);
        mBackButton.setOnClickListener(this);

        mNameView = (TextView) detailsLayout.findViewById(R.id.name_text);
        mNameView.setText(mPostCard.getContactName());

        initContactMobileLayout(detailsLayout);
        initContactEmailLayout(detailsLayout);
        initContactCompanyLayout(detailsLayout);

        return detailsLayout;
    }

    private void initContactMobileLayout(View detailsLayout) {
        DropDownLayout mobileLayout = (DropDownLayout) detailsLayout.findViewById(R.id.mobile_item);
        View layout = mobileLayout.setTopLayout(R.layout.contact_mobile_item_header);
        mMobileStateView = (ImageView) layout.findViewById(R.id.contact_mobile_state);

        mobileLayout.setBottomLayout(R.layout.contact_mobile_bottom_layout);
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
    }

    private void initContactEmailLayout(View detailsLayout) {
        DropDownLayout emailLayout = (DropDownLayout) detailsLayout.findViewById(R.id.email_item);
        View layout = emailLayout.setTopLayout(R.layout.contact_email_top_layout);
        mEmailStateView = (ImageView) layout.findViewById(R.id.contact_email_state);

        emailLayout.setBottomLayout(R.layout.contact_email_bottom_layout);
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
    }

    private void initContactCompanyLayout(View detailsLayout) {
        DropDownLayout companyLayout = (DropDownLayout) detailsLayout.findViewById(R.id.company_item);
        View layout = companyLayout.setTopLayout(R.layout.contact_company_top_layout);
        mCompanyStateView = (ImageView) layout.findViewById(R.id.contact_company_state);

        companyLayout.setBottomLayout(R.layout.contact_company_bottom_layout);
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
    }

    @Override
    public void onClick(View v) {
        if (mBackButton == v) {
            if (mEventListener != null) {
                mEventListener.onPostCardDetailsBackAction();
            }
        }
    }

    public interface OnPostCardDetailsEventListener {

        public void onPostCardDetailsBackAction();

    }

}
