
package com.yigao.nfc.postcard.database.columns;

import android.provider.BaseColumns;

public interface ContactMobileColumns extends BaseColumns {

    public static final String MOBILE_TYPE = "mobile_type";

    public static final String MOBILE_NUMBER = "mobile_number";

    public static final String MOBILE_MCC = "mobile_mcc";

    public static final String MOBILE_OWNER = "mobile_contact_id";
}
