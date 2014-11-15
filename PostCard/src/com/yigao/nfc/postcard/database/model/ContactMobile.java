
package com.yigao.nfc.postcard.database.model;

public class ContactMobile {

    private String mMobileType;

    private String mMobileNumber;

    private String mMobileMCC;

    private String mMobileOwnerId;

    public ContactMobile() {

    }

    public void setMobileType(String mobileType) {
        mMobileType = mobileType;
    }

    public String getMobileType() {
        return mMobileType;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileMCC(String mcc) {
        mMobileMCC = mcc;
    }

    public String getMobileMCC() {
        return mMobileMCC;
    }

    public void setMobileOwnerId(String ownerId) {
        mMobileOwnerId = ownerId;
    }

    public String getMobileOwnerId() {
        return mMobileOwnerId;
    }
}
