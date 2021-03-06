
package com.yigao.nfc.postcard.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.Data;
import android.util.Log;

import com.yigao.nfc.postcard.database.columns.ContactCompanyColumns;
import com.yigao.nfc.postcard.database.columns.ContactEmailColumns;
import com.yigao.nfc.postcard.database.columns.ContactMobileColumns;
import com.yigao.nfc.postcard.database.columns.PostCardColumns;
import com.yigao.nfc.postcard.database.model.ContactCompany;
import com.yigao.nfc.postcard.database.model.ContactEmail;
import com.yigao.nfc.postcard.database.model.ContactMobile;
import com.yigao.nfc.postcard.database.model.PostCard;

public class DataBaseUtil extends DatabaseHelper {

    public DataBaseUtil(Context context) {
        super(context);
    }

    public ArrayList<PostCard> queryContactFromDB(Context context) {
        ArrayList<PostCard> mData = new ArrayList<PostCard>();
        // 获得所有的联系人

        ContentResolver cr = context.getContentResolver();

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC");
        // 循环遍历
        if (cur.moveToFirst()) {
            int idColumn = cur.getColumnIndex(ContactsContract.Contacts._ID);
            int displayNameColumn = cur
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            do {
                PostCard postCard = new PostCard();
                // postCard.set
                // 获得联系人的ID号
                String contactId = cur.getString(idColumn);
                // 获得联系人姓名
                String disPlayName = cur.getString(displayNameColumn);
                ArrayList<ContactEmail> contactEmails = new ArrayList<ContactEmail>();
                ArrayList<ContactMobile> contactMobiles = new ArrayList<ContactMobile>();
                ArrayList<ContactCompany> contactCompanies = new ArrayList<ContactCompany>();
                postCard.setContactName(disPlayName);
                postCard.setContactEmails(contactEmails);
                postCard.setContactMobile(contactMobiles);
                postCard.setContactCompany(contactCompanies);

                // 查看该联系人有多少个电话号码。如果没有这返回值为0
                int phoneCount = cur
                        .getInt(cur
                                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                Log.i("morning", disPlayName);
                if (phoneCount > 0) {
                    // 获得联系人的电话号码
                    Cursor phones = context.getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                    + " = " + contactId, null, null);
                    if (phones.moveToFirst()) {
                        do {
                            ContactMobile contactMobile = new ContactMobile();
                            // 遍历所有的电话号码
                            String phoneNumber = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            String phoneType = String.valueOf(0);
                            Log.i("morning", phoneNumber);
                            Log.i("morning", phoneType);
                            contactMobile.setMobileOwnerId(contactId);
                            contactMobile.setMobileType(phoneType);
                            contactMobile.setMobileNumber(phoneNumber);
                            contactMobiles.add(contactMobile);
                        } while (phones.moveToNext());
                    }
                    closeCursor(phones);
                }

                // 获取该联系人邮箱
                Cursor emails = context.getContentResolver().query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                + " = " + contactId, null, null);
                if (emails.moveToFirst()) {
                    do {
                        ContactEmail contactEmail = new ContactEmail();
                        // 遍历所有的电话号码
                        String emailType = emails
                                .getString(emails
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                        String emailValue = emails
                                .getString(emails
                                        .getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                        Log.i("morning", emailType);
                        Log.i("morning", emailValue);
                        contactEmail.setEmailOwnerId(contactId);
                        contactEmail.setEmailAddress(emailValue);
                        // contactEmail.setEmailType(emailType);
                        contactEmails.add(contactEmail);
                    } while (emails.moveToNext());
                }
                closeCursor(emails);

                // 获取该联系人IM
                // Cursor IMs = getContentResolver().query(
                // Data.CONTENT_URI,
                // new String[] {
                // Data._ID, Im.PROTOCOL, Im.DATA
                // },
                // Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE +
                // "='"
                // + Im.CONTENT_ITEM_TYPE + "'",
                // new String[] {
                // contactId
                // }, null);
                // if (IMs.moveToFirst()) {
                // do {
                // String protocol = IMs.getString(IMs
                // .getColumnIndex(Im.PROTOCOL));
                // String date = IMs
                // .getString(IMs.getColumnIndex(Im.DATA));
                // Log.i("protocol", protocol);
                // Log.i("date", date);
                // } while (IMs.moveToNext());
                // }

                // 获取该联系人地址
                // Cursor address = getActivity().getContentResolver()
                // .query(
                // ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
                // null,
                // ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                // + " = " + contactId, null, null);
                // if (address.moveToFirst()) {
                // do {
                // // 遍历所有的地址
                // String street = address
                // .getString(address
                // .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                // String city = address
                // .getString(address
                // .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                // String region = address
                // .getString(address
                // .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                // String postCode = address
                // .getString(address
                // .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                // String formatAddress = address
                // .getString(address
                // .getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
                // Log.i("morning", street);
                // Log.i("morning", city);
                // Log.i("morning", region);
                // Log.i("morning", postCode);
                // Log.i("morning", formatAddress);
                // } while (address.moveToNext());
                // }

                // 获取该联系人公司
                Cursor organizations = context.getContentResolver().query(
                        Data.CONTENT_URI,
                        new String[] {
                                Data._ID, Organization.COMPANY,
                                Organization.TITLE
                        },
                        Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
                                + Organization.CONTENT_ITEM_TYPE + "'",
                        new String[] {
                            contactId
                        }, null);
                if (organizations.moveToFirst()) {
                    do {
                        ContactCompany contactCompany = new ContactCompany();
                        String company = organizations.getString(organizations
                                .getColumnIndex(Organization.COMPANY));
                        String title = organizations.getString(organizations
                                .getColumnIndex(Organization.TITLE));
                        contactCompany.setCompanyName(company);
                        Log.i("morning", company);
                        Log.i("morning", title);
                        contactCompanies.add(contactCompany);
                    } while (organizations.moveToNext());
                }
                closeCursor(organizations);
                mData.add(postCard);
            } while (cur.moveToNext());
        }

        return mData;
    }

    /**
     * 获取本地数据库所有的postCard
     * 
     * @return
     */
    public ArrayList<PostCard> queryPostCardsFromLocalDB() {
        ArrayList<PostCard> datas = new ArrayList<PostCard>();
        Cursor postCursor = mDatabase.query(PostCardSQLiteOpenHelper.TABLE_POST_CARD, null, null,
                null,
                null, null, null);
        if (postCursor != null && postCursor.moveToFirst()) {
            do {
                PostCard postCard = new PostCard();
                String name = postCursor.getString(postCursor
                        .getColumnIndex(PostCardColumns.CONTACT_NAME));
                postCard.setContactName(name);

                // 查询电话号码
                ArrayList<ContactMobile> mobiles = new ArrayList<ContactMobile>();
                Cursor phoneCursor = mDatabase.query(PostCardSQLiteOpenHelper.TABLE_MOBILE, null,
                        ContactMobileColumns.MOBILE_OWNER + " = ? ", new String[] {
                            name
                        }, null, null, null);
                if (phoneCursor != null) {
                    while (phoneCursor.moveToNext()) {
                        mobiles.add(parseCursorToContactMobile(phoneCursor));
                    }
                    closeCursor(phoneCursor);
                }

                // 查询公司信息
                ArrayList<ContactCompany> companies = new ArrayList<ContactCompany>();
                Cursor companyCursor = mDatabase.query(PostCardSQLiteOpenHelper.TABLE_COMPANY,
                        null,
                        ContactCompanyColumns.COMPAY_RECORD_OWNER + " = ? ", new String[] {
                            name
                        }, null, null, null);
                if (companyCursor != null) {
                    while (companyCursor.moveToNext()) {
                        companies.add(parseCursorToContactCompany(companyCursor));
                    }
                    closeCursor(companyCursor);
                }

                // 查询Email
                ArrayList<ContactEmail> emails = new ArrayList<ContactEmail>();
                Cursor emailCursor = mDatabase.query(PostCardSQLiteOpenHelper.TABLE_EMIAL, null,
                        ContactEmailColumns.EMAIL_OWNER + " = ? ", new String[] {
                            name
                        }, null, null, null);
                if (emailCursor != null) {
                    while (emailCursor.moveToNext()) {
                        emails.add(parseCursorToContactEmail(emailCursor));
                    }

                    closeCursor(emailCursor);
                }

                postCard.setContactCompany(companies);
                postCard.setContactEmails(emails);
                postCard.setContactMobile(mobiles);
                datas.add(postCard);
            } while (postCursor.moveToNext());
        }
        closeCursor(postCursor);

        return datas;
    }

    /**
     * 删除本地数据库的postcard
     * 
     * @param postCards
     */
    public void deletePostCards(ArrayList<PostCard> postCards) {
        if (postCards != null && postCards.size() > 0) {
            for (PostCard postCard : postCards) {
                String contactName = postCard.getContactName();
                mDatabase.delete(PostCardSQLiteOpenHelper.TABLE_COMPANY,
                        ContactCompanyColumns.COMPAY_RECORD_OWNER + " = ? ", new String[] {
                            contactName
                        });
                mDatabase.delete(PostCardSQLiteOpenHelper.TABLE_EMIAL,
                        ContactEmailColumns.EMAIL_OWNER + " = ? ", new String[] {
                            contactName
                        });
                mDatabase.delete(PostCardSQLiteOpenHelper.TABLE_MOBILE,
                        ContactMobileColumns.MOBILE_OWNER + " = ? ", new String[] {
                            contactName
                        });
                mDatabase.delete(PostCardSQLiteOpenHelper.TABLE_POST_CARD,
                        PostCardColumns.CONTACT_IDENTIFICATION + " = ? ", new String[] {
                            contactName
                        });
            }
        }
    }

    /**
     * 将联系人插入到本地数据库
     * 
     * @param postCards
     */
    public boolean insertPostCards(ArrayList<PostCard> postCards) {
        boolean insertSuccess = false;
        if (postCards != null && postCards.size() > 0) {
            for (PostCard postCard : postCards) {
                String contactName = postCard.getContactName();
                List<ContactCompany> companies = postCard.getContactCompany();
                List<ContactEmail> emails = postCard.getContactEmails();
                List<ContactMobile> mobiles = postCard.getContactMobile();
                Log.e("morning", "mobiles size is ==" + mobiles.size());

                if (companies != null && companies.size() > 0) {
                    for (ContactCompany company : companies) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ContactCompanyColumns.COMPAY_RECORD_OWNER, contactName);
                        contentValues.put(ContactCompanyColumns.COMPANY_NAME,
                                company.getCompanyName());
                        mDatabase.insert(PostCardSQLiteOpenHelper.TABLE_COMPANY, null,
                                contentValues);
                    }
                }

                if (emails != null && emails.size() > 0) {
                    for (ContactEmail email : emails) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ContactEmailColumns.EMAIL_OWNER, contactName);
                        contentValues.put(ContactEmailColumns.EMAIL_ADDRESS,
                                email.getEmailAddress());
                        mDatabase.insert(PostCardSQLiteOpenHelper.TABLE_EMIAL, null, contentValues);
                    }
                }

                if (mobiles != null && mobiles.size() > 0) {
                    for (ContactMobile mobile : mobiles) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ContactMobileColumns.MOBILE_OWNER, contactName);
                        contentValues.put(ContactMobileColumns.MOBILE_NUMBER,
                                mobile.getMobileNumber());
                        contentValues.put(ContactMobileColumns.MOBILE_TYPE,
                                mobile.getMobileType());
                        long result = mDatabase
                                .insert(PostCardSQLiteOpenHelper.TABLE_MOBILE, null, contentValues);
                        Log.e("morning", "result is ===" + result);

                    }
                }

                ContentValues postcardContentValues = new ContentValues();
                postcardContentValues.put(PostCardColumns.CONTACT_IDENTIFICATION, contactName);
                postcardContentValues.put(PostCardColumns.CONTACT_NAME, postCard.getContactName());

                long insertValuse = mDatabase.insert(PostCardSQLiteOpenHelper.TABLE_POST_CARD,
                        null,
                        postcardContentValues);
                if (insertValuse > 0) {
                    insertSuccess = true;
                }
            }
        }
        return insertSuccess;
    }

    public synchronized void insertPostCard(PostCard postCard) {
        if (postCard != null) {
            List<ContactMobile> mobiles = postCard.getContactMobile();
            if (mobiles != null && !mobiles.isEmpty()) {
                for (ContactMobile mobile : mobiles) {
                    ContentValues mobValues = getContentValues(mobile);
                    super.insert(PostCardSQLiteOpenHelper.TABLE_MOBILE, mobValues);
                }
            }

            List<ContactEmail> emails = postCard.getContactEmails();
            if (emails != null && !emails.isEmpty()) {
                for (ContactEmail email : emails) {
                    ContentValues values = getContantValues(email);
                    super.insert(PostCardSQLiteOpenHelper.TABLE_EMIAL, values);
                }
            }

            List<ContactCompany> compaies = postCard.getContactCompany();
            if (compaies != null && !compaies.isEmpty()) {
                for (ContactCompany company : compaies) {
                    ContentValues companyValues = getContentValues(company);
                    super.insert(PostCardSQLiteOpenHelper.TABLE_COMPANY, companyValues);
                }
            }

            ContentValues cardvalues = getContentValues(postCard);
            super.insert(PostCardSQLiteOpenHelper.TABLE_POST_CARD, cardvalues);
        }
    }

    public synchronized void deletePostCard(PostCard card) {
        if (card != null) {
            StringBuilder deleteMobileSQL = new StringBuilder();
            deleteMobileSQL.append(ContactMobileColumns.MOBILE_OWNER);
            deleteMobileSQL.append("=?");

            super.delete(PostCardSQLiteOpenHelper.TABLE_MOBILE,
                    deleteMobileSQL.toString(),
                    new String[] {
                        card.getContactName()
                    });

            StringBuilder deleteEmailSQL = new StringBuilder();
            deleteEmailSQL.append(ContactEmailColumns.EMAIL_OWNER);
            deleteEmailSQL.append("=?");

            super.delete(PostCardSQLiteOpenHelper.TABLE_EMIAL, deleteEmailSQL.toString(),
                    new String[] {
                        card.getContactName()
                    });

            StringBuilder deleteCompanySQL = new StringBuilder();
            deleteCompanySQL.append(ContactCompanyColumns.COMPAY_RECORD_OWNER);
            deleteCompanySQL.append("=?");

            super.delete(PostCardSQLiteOpenHelper.TABLE_COMPANY, deleteCompanySQL.toString(),
                    new String[] {
                        card.getContactName()
                    });

            StringBuilder deletePostCardSQL = new StringBuilder();
            deletePostCardSQL.append(PostCardColumns.CONTACT_NAME);
            deletePostCardSQL.append("=?");

            super.delete(PostCardSQLiteOpenHelper.TABLE_POST_CARD, deletePostCardSQL.toString(),
                    new String[] {
                        card.getContactName()
                    });
        }
    }

    protected void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }
    }
}
