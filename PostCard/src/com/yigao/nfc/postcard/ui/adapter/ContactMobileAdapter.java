
package com.yigao.nfc.postcard.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.ContactMobile;

public class ContactMobileAdapter extends BaseAdapter implements OnClickListener {

    private LayoutInflater mInflater;

    private List<ContactMobile> mContactMobile;

    public ContactMobileAdapter(Context context, List<ContactMobile> mobiles) {
        super();

        mInflater = LayoutInflater.from(context);
        mContactMobile = new ArrayList<ContactMobile>();
        mContactMobile.addAll(mobiles);
    }

    @Override
    public int getCount() {
        return mContactMobile.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactMobile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.contact_mobile_item_layout, null, false);
            holder = new ViewHolder();
            holder.mobileNumberView = (TextView) convertView.findViewById(R.id.mobile_type);
            holder.mobileDeleteView = (ImageView) convertView.findViewById(R.id.mobile_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactMobile entry = (ContactMobile) getItem(position);
        holder.mobileNumberView.setText(entry.getMobileNumber());
        holder.mobileDeleteView.setOnClickListener(this);
        holder.mobileDeleteView.setTag(entry);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        ContactMobile entry = (ContactMobile) v.getTag();
        mContactMobile.remove(entry);
        notifyDataSetChanged();
    }

    public void addContactMobiles(List<ContactMobile> mobiles) {
        if (mobiles != null && !mobiles.isEmpty()) {
            mContactMobile.addAll(mobiles);
            notifyDataSetChanged();
        }
    }

    public List<ContactMobile> getContactMobiles() {
        return mContactMobile;
    }

    static class ViewHolder {
        TextView mobileNumberView;
        ImageView mobileDeleteView;
    }
}
