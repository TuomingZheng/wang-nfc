
package com.yigao.nfc.postcard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yigao.nfc.postcard.database.columns.ContactCompanyColumns;
import com.yigao.nfc.postcard.database.columns.ContactEmailColumns;
import com.yigao.nfc.postcard.database.columns.ContactMobileColumns;
import com.yigao.nfc.postcard.database.columns.PostCardColumns;

public class PostCardSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final int VERSION_CODE = 1;

    private static final String DATABASE_NAME = "post_card";

    public static final String TABLE_POST_CARD = "post_card";

    public static final String TABLE_MOBILE = "mobile";

    public static final String TABLE_EMIAL = "email";

    public static final String TABLE_COMPANY = "company";
    private static PostCardSQLiteOpenHelper instance;

    private PostCardSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_CODE);
    }
    
    public static PostCardSQLiteOpenHelper getInstance(Context context){
        if(instance == null){
            instance = new PostCardSQLiteOpenHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createPostCardTable(db);
        createMobileTable(db);
        createEmailTable(db);
        createCompayTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        createPostCardTable(db);
        createMobileTable(db);
        createEmailTable(db);
        createCompayTable(db);
    }

    private void createPostCardTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table ");
        sql.append(TABLE_POST_CARD);
        sql.append(" ( ");
        sql.append(PostCardColumns._ID).append(" integer primary key autoincrement,");
        sql.append(PostCardColumns.CONTACT_NAME).append(" text not null,");
        sql.append(PostCardColumns.CONTACT_PINYIN).append(" text,");
        sql.append(PostCardColumns.CONTACT_GENDER).append(" integer,");
        sql.append(PostCardColumns.CONTACT_BIRTHDAY).append(" integer,");
        sql.append(PostCardColumns.CONTACT_IDENTIFICATION).append(" text unique not null,");
        sql.append(PostCardColumns.CONTACT_GENERATE_ADDRESS).append(" text,");
        sql.append(PostCardColumns.CONTACT_GENERATE_TIMESTAMP).append(" integer");
        sql.append(");");

        db.execSQL(sql.toString());
    }

    private void createMobileTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table ");
        sql.append(TABLE_MOBILE);
        sql.append("(");
        sql.append(ContactMobileColumns._ID).append(" integer  primary key autoincrement,");
        sql.append(ContactMobileColumns.MOBILE_TYPE).append(" integer not null,");
        sql.append(ContactMobileColumns.MOBILE_MCC).append(" integer,");
        sql.append(ContactMobileColumns.MOBILE_NUMBER).append(" text not null,");
        sql.append(ContactMobileColumns.MOBILE_OWNER).append(" text not null");
        sql.append(");");

        db.execSQL(sql.toString());
    }

    private void createEmailTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table ");
        sql.append(TABLE_EMIAL);
        sql.append(" ( ");
        sql.append(ContactEmailColumns._ID).append(" integer  primary key autoincrement,");
        sql.append(ContactEmailColumns.EMAIL_TYPE).append(" integer not null,");
        sql.append(ContactEmailColumns.EMAIL_ADDRESS).append(" text not null unique,");
        sql.append(ContactEmailColumns.EMAIL_OWNER).append(" text not null");
        sql.append(");");

        db.execSQL(sql.toString());
    }

    private void createCompayTable(SQLiteDatabase db) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table ");
        sql.append(TABLE_COMPANY);
        sql.append("(");
        sql.append(ContactCompanyColumns._ID).append(" integer  primary key autoincrement,");
        sql.append(ContactCompanyColumns.COMPANY_ADDRESS).append(" text,");
        sql.append(ContactCompanyColumns.COMPANY_NAME).append(" text not null,");
        sql.append(ContactCompanyColumns.COMPANY_STAFF).append(" text,");
        sql.append(ContactCompanyColumns.COMPAY_DEPAT).append(" text,");
        sql.append(ContactCompanyColumns.COMPAY_RECORD_OWNER).append(" text not null");
        sql.append(");");

        db.execSQL(sql.toString());
    }

    private void dropAllTables(SQLiteDatabase db) {
        StringBuffer dropPostCardTable = new StringBuffer();
        dropPostCardTable.append("drop table ");
        dropPostCardTable.append(TABLE_POST_CARD);
        dropPostCardTable.append(";");
        db.execSQL(dropPostCardTable.toString());

        StringBuffer dropMobileTable = new StringBuffer();
        dropMobileTable.append("drop table ");
        dropMobileTable.append(TABLE_MOBILE);
        dropMobileTable.append(";");
        db.execSQL(dropMobileTable.toString());

        StringBuffer dropEmailTable = new StringBuffer();
        dropEmailTable.append("drop table ");
        dropEmailTable.append(TABLE_EMIAL);
        dropEmailTable.append(";");
        db.execSQL(dropEmailTable.toString());

        StringBuffer dropCompayTable = new StringBuffer();
        dropCompayTable.append("drop table ");
        dropCompayTable.append(TABLE_COMPANY);
        dropCompayTable.append(";");
        db.execSQL(dropCompayTable.toString());
    }
    
}
