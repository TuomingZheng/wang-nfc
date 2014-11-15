
package com.yigao.nfc.postcard.database.model;

public class ContactEmail {

    private int mEmailType;

    private String mEmailAddress;

    private String mEmailOwnerId;

    public ContactEmail() {

    }

    public void setEmailType(int emailType) {
        mEmailType = emailType;
    }

    public int getEmailType() {
        return mEmailType;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public String getEmialAddress() {
        return mEmailAddress;
    }

    public void setEmailOwnerId(String ownerId) {
        mEmailOwnerId = ownerId;
    }

    public String getEmailOwnerId() {
        return mEmailOwnerId;
    }
}
