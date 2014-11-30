
package com.yigao.nfc.postcard.app;

import android.app.Application;

public class PostCardApplication extends Application {

    private FileManager mFileManager;

    private static PostCardApplication sInstance;

    public PostCardApplication() {
        super();

        sInstance = this;
        mFileManager = new FileManager(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mFileManager.copyORCLanguageToExternalStorage();
    }

    public synchronized static PostCardApplication getInstance() {
        return sInstance;
    }

    public FileManager getFileManager() {
        return mFileManager;
    }
}
