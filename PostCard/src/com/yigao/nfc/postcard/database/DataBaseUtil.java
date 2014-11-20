package com.yigao.nfc.postcard.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yigao.nfc.postcard.database.columns.ContactCompanyColumns;
import com.yigao.nfc.postcard.database.columns.ContactEmailColumns;
import com.yigao.nfc.postcard.database.columns.ContactMobileColumns;
import com.yigao.nfc.postcard.database.columns.PostCardColumns;
import com.yigao.nfc.postcard.database.model.ContactCompany;
import com.yigao.nfc.postcard.database.model.ContactEmail;
import com.yigao.nfc.postcard.database.model.ContactMobile;
import com.yigao.nfc.postcard.database.model.PostCard;

public class DataBaseUtil {
    
    private SQLiteDatabase db;
    
    public DataBaseUtil(Context context){
        db = PostCardSQLiteOpenHelper.getInstance(context).getWritableDatabase();
    }
    
    /**
     * 获取本地数据库所有的postCard
     * @return
     */
    public ArrayList<PostCard> queryPostCardsFromLocalDB(){
        ArrayList<PostCard> datas = new ArrayList<PostCard>();
        Cursor postCursor = db.query(PostCardSQLiteOpenHelper.TABLE_POST_CARD, null, null, null, null, null, null);
        if (postCursor != null && postCursor.moveToFirst()) {
            do {
                PostCard postCard = new PostCard();
                String contactId = postCursor.getString(postCursor.getColumnIndex(PostCardColumns.CONTACT_IDENTIFICATION));
                String name = postCursor.getString(postCursor.getColumnIndex(PostCardColumns.CONTACT_NAME));
                postCard.setContactIdentification(contactId);
                postCard.setContactName(name);
                Log.d("morning", "get data from db === name is =="+name);
                
                
                //查询电话号码
                ArrayList<ContactMobile> mobiles = new ArrayList<ContactMobile>();
                Cursor phoneCursor = db.query(PostCardSQLiteOpenHelper.TABLE_MOBILE, null,
                        ContactMobileColumns.MOBILE_OWNER + " = ? ", new String[] {
                            contactId
                        }, null, null, null);
                if (phoneCursor != null && phoneCursor.moveToFirst()) {
                    do {
                        ContactMobile contactMobile = new ContactMobile();
                        String mobileNumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactMobileColumns.MOBILE_NUMBER));
                        String mobileType = phoneCursor.getString(phoneCursor.getColumnIndex(ContactMobileColumns.MOBILE_TYPE));
                        Log.d("morning", "get data from db === mobileNumber is =="+mobileNumber);
                        contactMobile.setMobileNumber(mobileNumber);
                        contactMobile.setMobileType(mobileType);
                        mobiles.add(contactMobile);
                    } while (phoneCursor.moveToNext());
                }
                closeCursor(phoneCursor);
                
              //查询公司信息
                ArrayList<ContactCompany> companies = new ArrayList<ContactCompany>();
                Cursor companyCursor = db.query(PostCardSQLiteOpenHelper.TABLE_COMPANY, null,
                        ContactCompanyColumns.COMPAY_RECORD_OWNER + " = ? ", new String[] {
                            contactId
                        }, null, null, null);
                if (companyCursor != null && companyCursor.moveToFirst()) {
                    do {
                        ContactCompany contactCompany = new ContactCompany();
                        String companyName = companyCursor.getString(companyCursor.getColumnIndex(ContactCompanyColumns.COMPANY_NAME));
                        Log.d("morning", "get data from db === companyName is =="+companyName);
                        contactCompany.setCompanyName(companyName);
                        companies.add(contactCompany);
                    } while (companyCursor.moveToNext());
                }
                closeCursor(companyCursor);
                
              //查询Email
                ArrayList<ContactEmail> emails = new ArrayList<ContactEmail>();
                Cursor emailCursor = db.query(PostCardSQLiteOpenHelper.TABLE_EMIAL, null,
                        ContactEmailColumns.EMAIL_OWNER + " = ? ", new String[] {
                            contactId
                        }, null, null, null);
                if (emailCursor != null && emailCursor.moveToFirst()) {
                    do {
                        ContactEmail contactEmail = new ContactEmail();
                        String email = emailCursor.getString(emailCursor.getColumnIndex(ContactEmailColumns.EMAIL_ADDRESS));
                        Log.d("morning", "get data from db === email is =="+email);
                        contactEmail.setEmailAddress(email);
                        emails.add(contactEmail);
                    } while (emailCursor.moveToNext());
                }
                closeCursor(emailCursor);
                
                postCard.setContactCompanys(companies);
                postCard.setContactEmails(emails);
                postCard.setContactMobiles(mobiles);
                datas.add(postCard);
            } while (postCursor.moveToNext());
        }
        closeCursor(postCursor);
        
        return datas;
    }
    
    
    /**
     * 删除本地数据库的postcard
     * @param postCards
     */
    public void deletePostCards(ArrayList<PostCard> postCards) {
        if (postCards != null && postCards.size() > 0) {
            for (PostCard postCard : postCards) {
                String contactId = postCard.getContactIdentificationId();
                db.delete(PostCardSQLiteOpenHelper.TABLE_COMPANY,
                        ContactCompanyColumns.COMPAY_RECORD_OWNER + " = ? ", new String[] {
                            contactId
                        });
                db.delete(PostCardSQLiteOpenHelper.TABLE_EMIAL,
                        ContactEmailColumns.EMAIL_OWNER + " = ? ", new String[] {
                            contactId
                        });
                db.delete(PostCardSQLiteOpenHelper.TABLE_MOBILE,
                        ContactMobileColumns.MOBILE_OWNER + " = ? ", new String[] {
                            contactId
                        });
                db.delete(PostCardSQLiteOpenHelper.TABLE_POST_CARD,
                        PostCardColumns.CONTACT_IDENTIFICATION + " = ? ", new String[] {
                            contactId
                        });
            }
        }
    }
    
    /**
     * 将联系人插入到本地数据库
     * @param postCards
     */
    public void insertPostCards(ArrayList<PostCard> postCards){
        if (postCards != null && postCards.size() > 0) {
            for (PostCard postCard : postCards) {
                String contactId = postCard.getContactIdentificationId();
                List<ContactCompany> companies = postCard.getContactCompanys();
                List<ContactEmail> emails = postCard.getContactEmails();
                List<ContactMobile> mobiles = postCard.getContactMobiles();
                
                if (companies != null && companies.size() > 0) {
                    for (ContactCompany company : companies) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ContactCompanyColumns.COMPAY_RECORD_OWNER, contactId);
                        contentValues.put(ContactCompanyColumns.COMPANY_NAME,
                                company.getCompanyName());
                        db.insert(PostCardSQLiteOpenHelper.TABLE_COMPANY, null, contentValues);
                    } 
                } 
                
                if (emails != null && emails.size() > 0) {
                    for (ContactEmail email : emails) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ContactEmailColumns.EMAIL_OWNER, contactId);
                        contentValues.put(ContactEmailColumns.EMAIL_ADDRESS,
                                email.getEmialAddress());
                        db.insert(PostCardSQLiteOpenHelper.TABLE_EMIAL, null, contentValues);
                    } 
                }
                
                if (mobiles != null && mobiles.size() > 0) {
                    for (ContactMobile mobile : mobiles) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(ContactMobileColumns.MOBILE_OWNER, contactId);
                        contentValues.put(ContactMobileColumns.MOBILE_NUMBER,
                                mobile.getMobileNumber());
                        contentValues.put(ContactMobileColumns.MOBILE_TYPE,
                                mobile.getMobileType());
                        db.insert(PostCardSQLiteOpenHelper.TABLE_MOBILE, null, contentValues);
                    }
                }
                
                ContentValues postcardContentValues = new ContentValues();
                postcardContentValues.put(PostCardColumns.CONTACT_IDENTIFICATION, contactId);
                postcardContentValues.put(PostCardColumns.CONTACT_NAME, postCard.getContactName());
                db.insert(PostCardSQLiteOpenHelper.TABLE_POST_CARD, null, postcardContentValues);
            }
        }
    }
    
    
    
    protected void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
            cursor = null;
        }
    }
}
