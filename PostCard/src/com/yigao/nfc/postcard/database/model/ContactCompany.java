
package com.yigao.nfc.postcard.database.model;

public class ContactCompany {

    private String mCompanyName;

    private String mCompanyAddress;

    private String mStaff;

    private String mDepartment;

    private String mOwnerId;

    public ContactCompany() {

    }

    public void setCompanyName(String companyName) {
        mCompanyName = companyName;
    }

    public String getCompanyName() {
        return mCompanyName;
    }

    public void setCompanyAddress(String companyAddress) {
        mCompanyAddress = companyAddress;
    }

    public String getCompanyAddress() {
        return mCompanyAddress;
    }

    public void setCompanyStaff(String staff) {
        mStaff = staff;
    }

    public String getCompayStaff() {
        return mStaff;
    }

    public void setCompanyDepartment(String department) {
        mDepartment = department;
    }

    public String getCompanyDepartment() {
        return mDepartment;
    }

    public void setRecordOwnerId(String ownerId) {
        mOwnerId = ownerId;
    }

    public String getRecordOwnerId() {
        return mOwnerId;
    }
}
