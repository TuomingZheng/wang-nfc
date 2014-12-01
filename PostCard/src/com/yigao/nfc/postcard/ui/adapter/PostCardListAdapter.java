
package com.yigao.nfc.postcard.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.activity.PostCardMainActivity;
import com.yigao.nfc.postcard.ui.fragment.PostCardHolderFragment.OnPostCardInputActionListener;

public class PostCardListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<PostCard> mList;
    private ArrayList<Boolean> mSelectedList;
    private ViewHolder holder;
    private boolean mIsMutiMode;
    private PostCardMainActivity mActivity;
    private boolean mIsNfcEnable;
    private OnPostCardInputActionListener mInputActionListener;

    public PostCardListAdapter(Context context, ArrayList<PostCard> list,
            ArrayList<Boolean> selectedList, boolean isNfcEnable,
            OnPostCardInputActionListener listener) {
        mActivity = (PostCardMainActivity) context;
        this.context = context;
        this.mList = list;
        this.mSelectedList = selectedList;
        this.mIsNfcEnable = isNfcEnable;
        this.mInputActionListener = listener;
    }

    public void setMutiMode(boolean isMutiMode) {
        mIsMutiMode = isMutiMode;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public PostCard getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.post_card_item, null, false);

            holder = new ViewHolder();
            holder.postImg = (CheckBox) convertView.findViewById(R.id.contact_icon);
            holder.postName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.inputToNfc = (LinearLayout) convertView.findViewById(R.id.contact_input_to_nfc);
            holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.parent_layout);
            holder.syncView = (ImageButton) convertView.findViewById(R.id.nfc_sync_image);
            holder.avatarView = (ImageView) convertView.findViewById(R.id.contact_avatar);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PostCard postCard = getItem(position);
        holder.postName.setText(postCard.getContactName());

        holder.postImg.setChecked(mSelectedList.get(position));

        holder.postImg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean checked) {
                if (checked) {
                    mSelectedList.set(position, true);
                }
            }
        });

        if (mIsMutiMode) {
            holder.postImg.setVisibility(View.VISIBLE);
        } else {
            holder.postImg.setVisibility(View.INVISIBLE);
        }

        if (mIsNfcEnable) {
            holder.inputToNfc.setVisibility(View.VISIBLE);
        } else {
            holder.inputToNfc.setVisibility(View.GONE);
        }

        holder.inputToNfc.setTag(mList.get(position));
        holder.inputToNfc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                
                final String name =   ((TextView)((View)v.getParent()).findViewById(R.id.contact_name)).getText().toString();
                PostCard postCard = findPostCardByName(name);
                mActivity.setWriteMode(true, postCard);
                holder.syncView.performClick();
            }
        });

        holder.parentLayout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                final String name = ((TextView) v.findViewById(R.id.contact_name)).getText()
                        .toString();
                PostCard postCard = findPostCardByName(name);
                mInputActionListener.onReviewPostCardContact(postCard);
            }
        });
        return convertView;
    }

    private PostCard findPostCardByName(String name) {
        PostCard postCard = null;
        if (mList != null && !mList.isEmpty()) {
            for (PostCard card : mList) {
                if (card.getContactName().equals(name)) {
                    postCard = card;
                    break;
                }
            }
        }
        return postCard;
    }

    static class ViewHolder {
        TextView postName;
        CheckBox postImg;
        LinearLayout parentLayout;
        LinearLayout inputToNfc;
        ImageButton syncView;
        ImageView avatarView;
    }

    public interface ImportAdapterSelected {
        void selectAll(boolean isSelectAll);
    }

}
