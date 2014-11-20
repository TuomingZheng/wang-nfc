
package com.yigao.nfc.postcard.database.model;

import java.util.List;

public class PostCard {

    private String contactName;

    private String contactPinYin;

    private int contactGender;

    private long contactBirthday;

    private String ID;

    private String recordGenerateAddress;

    private long recordGenerateTimeStamp;

    private List<ContactMobile> contactMobile;

    private List<ContactEmail> contactEmails;

    private List<ContactCompany> contactCompany;

    public PostCard() {

    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPinYin() {
        return contactPinYin;
    }

    public void setContactPinYin(String contactPinYin) {
        this.contactPinYin = contactPinYin;
    }

    public int getContactGender() {
        return contactGender;
    }

    public void setContactGender(int contactGender) {
        this.contactGender = contactGender;
    }

    public long getContactBirthday() {
        return contactBirthday;
    }

    public void setContactBirthday(long contactBirthday) {
        this.contactBirthday = contactBirthday;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getRecordGenerateAddress() {
        return recordGenerateAddress;
    }

    public void setRecordGenerateAddress(String recordGenerateAddress) {
        this.recordGenerateAddress = recordGenerateAddress;
    }

    public long getRecordGenerateTimeStamp() {
        return recordGenerateTimeStamp;
    }

    public void setRecordGenerateTimeStamp(long recordGenerateTimeStamp) {
        this.recordGenerateTimeStamp = recordGenerateTimeStamp;
    }

    public List<ContactMobile> getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(List<ContactMobile> contactMobile) {
        this.contactMobile = contactMobile;
    }

    public List<ContactEmail> getContactEmails() {
        return contactEmails;
    }

    public void setContactEmails(List<ContactEmail> contactEmails) {
        this.contactEmails = contactEmails;
    }

    public List<ContactCompany> getContactCompany() {
        return contactCompany;
    }

    public void setContactCompany(List<ContactCompany> contactCompany) {
        this.contactCompany = contactCompany;
    }

}
