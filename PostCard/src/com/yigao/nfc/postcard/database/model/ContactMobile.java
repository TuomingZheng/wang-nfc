
package com.yigao.nfc.postcard.database.model;

public class ContactMobile {

    private String mobileType;

    private String mobileNumber;

    private String mobileMCC;

    private String mobileOwnerId;

    public ContactMobile() {

    }

    public String getMobileType() {
        return mobileType;
    }

    public void setMobileType(String mobileType) {
        this.mobileType = mobileType;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMobileMCC() {
        return mobileMCC;
    }

    public void setMobileMCC(String mobileMCC) {
        this.mobileMCC = mobileMCC;
    }

    public String getMobileOwnerId() {
        return mobileOwnerId;
    }

    public void setMobileOwnerId(String mobileOwnerId) {
        this.mobileOwnerId = mobileOwnerId;
    }

}
