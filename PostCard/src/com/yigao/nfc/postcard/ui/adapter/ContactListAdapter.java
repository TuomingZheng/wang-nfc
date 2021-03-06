
package com.yigao.nfc.postcard.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.PostCard;

public class ContactListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<PostCard> mList;
    private ArrayList<Boolean> mSelectedList;
    private ViewHolder holder;
    private ImportAdapterSelected mSelectedCallBack;

    public ContactListAdapter(Context context, ArrayList<PostCard> list,ArrayList<Boolean> selectedList,ImportAdapterSelected callBack) {
        this.context = context;
        this.mList = list;
        this.mSelectedList = selectedList;
        mSelectedCallBack = callBack;
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
                    R.layout.contact_item, null);

            holder = new ViewHolder();
            holder.postImg = (ImageView) convertView
                    .findViewById(R.id.contact_icon);
            holder.postName = (TextView) convertView
                    .findViewById(R.id.contact_name);
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
        return convertView;
    }

    static class ViewHolder {
        TextView postName;
        ImageView postImg;
        RelativeLayout parentLayout;
    }
    
    public interface ImportAdapterSelected{
        void selectAll(boolean isSelectAll);
    }

}
