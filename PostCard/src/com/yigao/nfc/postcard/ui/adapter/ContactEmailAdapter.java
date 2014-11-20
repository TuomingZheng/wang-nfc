
package com.yigao.nfc.postcard.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.ContactEmail;

public class ContactEmailAdapter extends BaseAdapter implements View.OnClickListener {

    private LayoutInflater mInflater;

    private List<ContactEmail> mEmailList;

    public ContactEmailAdapter(Context context, List<ContactEmail> emailList) {
        mInflater = LayoutInflater.from(context);
        mEmailList = new ArrayList<ContactEmail>();
        if (emailList != null && !emailList.isEmpty()) {
            mEmailList.addAll(emailList);
        }
    }

    @Override
    public int getCount() {
        return mEmailList.size();
    }

    @Override
    public Object getItem(int position) {
        return mEmailList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.contact_email_item_layout, null, false);
            convertView.setTag(holder);

            holder.emailTypeView = (TextView) convertView.findViewById(R.id.email_type);
            holder.emailAddressView = (TextView) convertView.findViewById(R.id.email_address);
            holder.emailDeleteView = (ImageView) convertView.findViewById(R.id.email_delete);
            holder.emailDeleteView.setOnClickListener(this);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactEmail entry = (ContactEmail) getItem(position);
        holder.emailDeleteView.setTag(entry);
        holder.emailTypeView.setText(entry.getEmailType());
        holder.emailAddressView.setText(entry.getEmailAddress());

        return convertView;
    }

    @Override
    public void onClick(View v) {
        ContactEmail entry = (ContactEmail) v.getTag();
        mEmailList.remove(entry);
        notifyDataSetChanged();
    }

    public void addContactEmails(List<ContactEmail> emails) {
        if (emails != null && !emails.isEmpty()) {
            mEmailList.addAll(emails);
            notifyDataSetChanged();
        }
    }

    public List<ContactEmail> getContactEmails() {
        return mEmailList;
    }

    static class ViewHolder {
        TextView emailTypeView;
        TextView emailAddressView;
        ImageView emailDeleteView;
    }
}
