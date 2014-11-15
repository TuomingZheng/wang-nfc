
package com.yigao.nfc.postcard.database.model;

import java.util.List;

public class PostCard {

    private String mContactName;

    private String mContactPinYin;

    private int mContactGender;

    private long mContactBirthday;

    private String mID;

    private String mRecordGenerateAddress;

    private long mRecordGenerateTimeStamp;

    private List<ContactMobile> mContactMobile;

    private List<ContactEmail> mContactEmails;

    private List<ContactCompany> mContactCompany;

    public PostCard() {

    }

    public void setContactName(String contactName) {
        mContactName = contactName;
    }

    public String getContactName() {
        return mContactName;
    }

    public void setContactPinYin(String contactPinYin) {
        mContactPinYin = contactPinYin;
    }

    public String getContactPinYin() {
        return mContactPinYin;
    }

    public void setContactGender(int gender) {
        mContactGender = gender;
    }

    public int getContactGender() {
        return mContactGender;
    }

    public void setContactBirthday(long birthday) {
        mContactBirthday = birthday;
    }

    public long getContactBirthday() {
        return mContactBirthday;
    }

    public void setContactIdentification(String id) {
        mID = id;
    }

    public String getContactIdentificationId() {
        return mID;
    }

    public void setContactGenerateTimestamp(long timeStamp) {
        mRecordGenerateTimeStamp = timeStamp;
    }

    public long getContactGenerateTimestamp() {
        return mRecordGenerateTimeStamp;
    }

    public void setContactGenerateAddress(String address) {
        mRecordGenerateAddress = address;
    }

    public String getContactGenerateAddress() {
        return mRecordGenerateAddress;
    }

    public void setContactMobiles(List<ContactMobile> mobiles) {
        mContactMobile = mobiles;
    }

    public List<ContactMobile> getContactMobiles() {
        return mContactMobile;
    }

    public void setContactEmails(List<ContactEmail> emails) {
        mContactEmails = emails;
    }

    public List<ContactEmail> getContactEmails() {
        return mContactEmails;
    }

    public void setContactCompanys(List<ContactCompany> companys) {
        mContactCompany = companys;
    }

    public List<ContactCompany> getContactCompanys() {
        return mContactCompany;
    }
}
