
package com.yigao.nfc.postcard.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.yigao.nfc.postcard.database.columns.ContactCompanyColumns;
import com.yigao.nfc.postcard.database.columns.ContactEmailColumns;
import com.yigao.nfc.postcard.database.columns.ContactMobileColumns;
import com.yigao.nfc.postcard.database.columns.PostCardColumns;
import com.yigao.nfc.postcard.database.model.ContactCompany;
import com.yigao.nfc.postcard.database.model.ContactEmail;
import com.yigao.nfc.postcard.database.model.ContactMobile;
import com.yigao.nfc.postcard.database.model.PostCard;

public class DatabaseHelper {

    private Context mContext;

    protected SQLiteDatabase mDatabase;

    public DatabaseHelper(Context context) {
        mContext = context;
        mDatabase = PostCardSQLiteOpenHelper.getInstance(mContext).getWritableDatabase();
    }

    protected long insert(String table, ContentValues values) {
        return mDatabase.insert(table, null, values);
    }

    protected int delete(String table, String whereClause, String[] whereArgs) {
        return mDatabase.delete(table, whereClause, whereArgs);
    }

    protected int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return mDatabase.update(table, values, whereClause, whereArgs);
    }

    public void destroyDatabaseHelper() {
        if (mDatabase != null && mDatabase.isOpen()) {
            mDatabase.close();
            mDatabase = null;
        }
    }

    protected ContactMobile parseCursorToContactMobile(Cursor cursor) {
        ContactMobile mobile = null;
        if (cursor != null) {
            mobile = new ContactMobile();

            final int mccIndex = cursor.getColumnIndex(ContactMobileColumns.MOBILE_MCC);
            mobile.setMobileMCC(cursor.getString(mccIndex));

            final int numberIndex = cursor.getColumnIndex(ContactMobileColumns.MOBILE_NUMBER);
            mobile.setMobileNumber(cursor.getString(numberIndex));

            final int ownerIndex = cursor.getColumnIndex(ContactMobileColumns.MOBILE_OWNER);
            mobile.setMobileOwnerId(cursor.getString(ownerIndex));

            final int mobType = cursor.getColumnIndex(ContactMobileColumns.MOBILE_TYPE);
            mobile.setMobileType(cursor.getString(mobType));
        }
        return mobile;
    }

    protected ContactEmail parseCursorToContactEmail(Cursor cursor) {
        ContactEmail email = null;
        if (cursor != null) {
            email = new ContactEmail();

            final int addressIndex = cursor.getColumnIndex(ContactEmailColumns.EMAIL_ADDRESS);
            email.setEmailAddress(cursor.getString(addressIndex));

            final int ownerIndex = cursor.getColumnIndex(ContactEmailColumns.EMAIL_OWNER);
            email.setEmailOwnerId(cursor.getString(ownerIndex));

            final int typeIndex = cursor.getColumnIndex(ContactEmailColumns.EMAIL_TYPE);
            email.setEmailType(cursor.getInt(typeIndex));
        }
        return email;
    }

    protected ContactCompany parseCursorToContactCompany(Cursor cursor) {
        ContactCompany company = null;
        if (cursor != null) {
            company = new ContactCompany();

            final int addressIndex = cursor.getColumnIndex(ContactCompanyColumns.COMPANY_ADDRESS);
            company.setCompanyAddress(cursor.getString(addressIndex));

            final int nameIndex = cursor.getColumnIndex(ContactCompanyColumns.COMPANY_NAME);
            company.setCompanyName(cursor.getString(nameIndex));

            final int departAddress = cursor.getColumnIndex(ContactCompanyColumns.COMPAY_DEPAT);
            company.setDepartment(cursor.getString(departAddress));

            final int ownerIndex = cursor.getColumnIndex(ContactCompanyColumns.COMPAY_RECORD_OWNER);
            company.setOwnerId(cursor.getString(ownerIndex));

            final int staffIndex = cursor.getColumnIndex(ContactCompanyColumns.COMPANY_STAFF);
            company.setStaff(cursor.getString(staffIndex));
        }
        return company;
    }

    protected PostCard parseCursorToPostCard(Cursor cursor) {
        PostCard card = null;
        if (cursor != null) {
            card = new PostCard();

            final int birthIndex = cursor.getColumnIndex(PostCardColumns.CONTACT_BIRTHDAY);
            card.setContactBirthday(cursor.getLong(birthIndex));

            final int genderIndex = cursor.getColumnIndex(PostCardColumns.CONTACT_GENDER);
            card.setContactGender(cursor.getInt(genderIndex));

            final int nameIndex = cursor.getColumnIndex(PostCardColumns.CONTACT_NAME);
            card.setContactName(cursor.getString(nameIndex));

            final int pinYinIndex = cursor.getColumnIndex(PostCardColumns.CONTACT_PINYIN);
            card.setContactPinYin(cursor.getString(pinYinIndex));

            final int IDIndex = cursor.getColumnIndex(PostCardColumns.CONTACT_IDENTIFICATION);
            card.setID(cursor.getString(IDIndex));

            final int genTimeIndex = cursor.getColumnIndex(
                    PostCardColumns.CONTACT_GENERATE_TIMESTAMP);
            card.setRecordGenerateTimeStamp(cursor.getLong(genTimeIndex));

            final int genAddressIndex = cursor
                    .getColumnIndex(PostCardColumns.CONTACT_GENERATE_ADDRESS);
            card.setRecordGenerateAddress(cursor.getString(genAddressIndex));
        }
        return card;
    }

    protected ContentValues getContentValues(ContactMobile mobile) {
        ContentValues values = null;
        if (mobile != null) {
            values = new ContentValues();
            values.put(ContactMobileColumns.MOBILE_MCC, mobile.getMobileMCC());
            values.put(ContactMobileColumns.MOBILE_NUMBER, mobile.getMobileNumber());
            values.put(ContactMobileColumns.MOBILE_OWNER, mobile.getMobileOwnerId());
            values.put(ContactMobileColumns.MOBILE_TYPE, mobile.getMobileType());
        }
        return values;
    }

    protected ContentValues getContantValues(ContactEmail email) {
        ContentValues values = null;
        if (email != null) {
            values = new ContentValues();
            values.put(ContactEmailColumns.EMAIL_ADDRESS, email.getEmailAddress());
            values.put(ContactEmailColumns.EMAIL_OWNER, email.getEmailOwnerId());
            values.put(ContactEmailColumns.EMAIL_TYPE, email.getEmailType());
        }
        return values;
    }

    protected ContentValues getContentValues(ContactCompany company) {
        ContentValues values = null;
        if (company != null) {
            values = new ContentValues();
            values.put(ContactCompanyColumns.COMPANY_ADDRESS, company.getCompanyAddress());
            values.put(ContactCompanyColumns.COMPANY_NAME, company.getCompanyName());
            values.put(ContactCompanyColumns.COMPAY_DEPAT, company.getDepartment());
            values.put(ContactCompanyColumns.COMPAY_RECORD_OWNER, company.getOwnerId());
            values.put(ContactCompanyColumns.COMPANY_STAFF, company.getStaff());
        }
        return values;
    }

    protected ContentValues getContentValues(PostCard card) {
        ContentValues values = null;
        if (card != null) {
            values = new ContentValues();
            values.put(PostCardColumns.CONTACT_BIRTHDAY, card.getContactBirthday());
            values.put(PostCardColumns.CONTACT_GENDER, card.getContactGender());
            values.put(PostCardColumns.CONTACT_NAME, card.getContactName());
            values.put(PostCardColumns.CONTACT_PINYIN, card.getContactPinYin());
            values.put(PostCardColumns.CONTACT_IDENTIFICATION, card.getID());
            values.put(PostCardColumns.CONTACT_GENERATE_TIMESTAMP,
                    card.getRecordGenerateTimeStamp());
            values.put(PostCardColumns.CONTACT_GENERATE_ADDRESS, card.getRecordGenerateAddress());
        }
        return values;
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }
    }
}
