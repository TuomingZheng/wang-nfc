
package com.yigao.nfc.postcard.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yigao.nfc.postcard.ui.activity.PostCardMainActivity;

public class PostCardListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<PostCard> mList;
    private ArrayList<Boolean> mSelectedList;
    private ViewHolder holder;
    private boolean mIsMutiMode;
    private PostCardMainActivity mActivity;
    private boolean mIsNfcEnable;

    public PostCardListAdapter(Context context, ArrayList<PostCard> list,ArrayList<Boolean> selectedList,boolean isNfcEnable) {
        mActivity = (PostCardMainActivity)context;
        this.context = context;
        this.mList = list;
        this.mSelectedList = selectedList;
        this.mIsNfcEnable = isNfcEnable;
    }
    
    public void setMutiMode(boolean isMutiMode){
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
                    R.layout.post_card_item, null);

            holder = new ViewHolder();
            holder.postImg = (ImageView) convertView
                    .findViewById(R.id.contact_icon);
            holder.postName = (TextView) convertView
                    .findViewById(R.id.contact_name);
            holder.inputToNfc = (TextView) convertView.findViewById(R.id.contact_input_to_nfc);
            holder.parentLayout = (RelativeLayout) convertView.findViewById(R.id.parent_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PostCard postCard = getItem(position);
        holder.postName.setText(postCard.getContactName());
        
        if(mSelectedList.get(position)){
            holder.postImg.setImageResource(R.drawable.btn_check_on);
        }else {
            holder.postImg.setImageResource(R.drawable.btn_check_off);
        }
        
        if(mIsMutiMode){
            holder.postImg.setVisibility(View.VISIBLE);
        }else {
            holder.postImg.setVisibility(View.INVISIBLE);
        }
        
        if(mIsNfcEnable){
            holder.inputToNfc.setVisibility(View.VISIBLE);
        }else {
            holder.inputToNfc.setVisibility(View.GONE);
        }
        
        holder.inputToNfc.setTag(mList.get(position));
        holder.inputToNfc.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Log.d("morning", "setOnClickListener insert into nfc");
                mActivity.setWriteMode(true, (PostCard)(holder.inputToNfc.getTag()));
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView postName;
        ImageView postImg;
        RelativeLayout parentLayout;
        TextView inputToNfc;
    }
    
    public interface ImportAdapterSelected{
        void selectAll(boolean isSelectAll);
    }

}
