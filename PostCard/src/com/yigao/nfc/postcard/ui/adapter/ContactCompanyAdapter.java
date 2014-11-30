
package com.yigao.nfc.postcard.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.database.model.ContactCompany;

public class ContactCompanyAdapter extends BaseAdapter implements OnClickListener {

    private LayoutInflater mInflater;

    private List<ContactCompany> mCompanyList;

    public ContactCompanyAdapter(Context context, List<ContactCompany> companyList) {
        mInflater = LayoutInflater.from(context);
        mCompanyList = new ArrayList<ContactCompany>();
        if (companyList != null && !companyList.isEmpty()) {
            mCompanyList.addAll(companyList);
        }
    }

    @Override
    public int getCount() {
        return mCompanyList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCompanyList.get(position);
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
            convertView = mInflater.inflate(R.layout.contact_company_item_layout, null, false);
            convertView.setTag(holder);

            holder.companyAddressView = (TextView) convertView.findViewById(R.id.company_address);
            holder.companyDeleteView = (ImageView) convertView.findViewById(R.id.company_delete);
            holder.companyDepartView = (TextView) convertView.findViewById(R.id.company_depart);
            holder.companyNameView = (TextView) convertView.findViewById(R.id.company_name);
            holder.companyStaffView = (TextView) convertView.findViewById(R.id.company_staff);
            holder.companyDeleteView.setOnClickListener(this);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ContactCompany entry = (ContactCompany) getItem(position);
        holder.companyAddressView.setText(entry.getCompanyAddress());
        Log.d("zheng", "add = " + entry.getCompanyAddress());
        holder.companyDepartView.setText(entry.getDepartment());
        holder.companyNameView.setText(entry.getCompanyName());
        holder.companyStaffView.setText(entry.getStaff());
        holder.companyDeleteView.setTag(entry);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        ContactCompany entry = (ContactCompany) v.getTag();
        mCompanyList.remove(entry);
        notifyDataSetChanged();
    }

    public void addContactCompany(List<ContactCompany> company) {
        if (company != null && !company.isEmpty()) {
            mCompanyList.addAll(company);
            notifyDataSetChanged();
        }
    }

    public List<ContactCompany> getContactCompany() {
        return mCompanyList;
    }

    static class ViewHolder {
        TextView companyNameView;
        TextView companyAddressView;
        TextView companyStaffView;
        TextView companyDepartView;
        ImageView companyDeleteView;
    }
}
