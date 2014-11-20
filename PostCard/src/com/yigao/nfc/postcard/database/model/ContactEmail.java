
package com.yigao.nfc.postcard.database.model;

public class ContactEmail {

    private int emailType;

    private String emailAddress;

    private String emailOwnerId;

    public ContactEmail() {

    }

    public int getEmailType() {
        return emailType;
    }

    public void setEmailType(int emailType) {
        this.emailType = emailType;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getEmailOwnerId() {
        return emailOwnerId;
    }

    public void setEmailOwnerId(String emailOwnerId) {
        this.emailOwnerId = emailOwnerId;
    }

}
