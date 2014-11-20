
package com.yigao.nfc.postcard.app;

import android.app.Application;

public class PostCardApplication extends Application {

    private static PostCardApplication sInstance;

    public PostCardApplication() {
        super();

        sInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public synchronized static PostCardApplication getInstance() {
        return sInstance;
    }
}
