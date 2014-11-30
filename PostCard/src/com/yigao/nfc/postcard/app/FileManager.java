
package com.yigao.nfc.postcard.app;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class FileManager {

    private File mORCImageDir;

    private File mORCLanguageDir;

    private Context mContext;

    private static final String POST_CARD_DIR_NAME = "PostCard";

    private static final String ORC_LAN_DIR_NAME = "language" + File.separator + "tessdata";

    private static final String IMAGE_NAME = "images";

    public FileManager(Context context) {
        mContext = context;

        String externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();

        StringBuilder orcLagPath = new StringBuilder(externalStoragePath);
        orcLagPath.append(File.separator).append(POST_CARD_DIR_NAME);
        orcLagPath.append(File.separator).append(ORC_LAN_DIR_NAME);
        mORCLanguageDir = new File(orcLagPath.toString());
        if (!mORCLanguageDir.exists()) {
            mORCLanguageDir.mkdir();
        }

        StringBuilder imagePath = new StringBuilder(externalStoragePath);
        imagePath.append(File.separator).append(POST_CARD_DIR_NAME);
        imagePath.append(File.separator).append(IMAGE_NAME);
        mORCImageDir = new File(imagePath.toString());
        if (!mORCImageDir.exists()) {
            mORCImageDir.mkdir();
        }
    }

    public File getORCLanguageDir() {
        return mORCLanguageDir;
    }

    public File getORCImageDir() {
        return mORCImageDir;
    }

    public void copyORCLanguageToExternalStorage() {
        File simFile = new File(mORCLanguageDir, "chi_sim.traineddata");
        if (!simFile.exists()) {
            copyORCLanguageFile("chi_sim.traineddata", simFile);
        }

        File engFile = new File(mORCLanguageDir, "eng.traineddata");
        if (!engFile.exists()) {
            copyORCLanguageFile("eng.traineddata", engFile);
        }
    }

    private void copyORCLanguageFile(String assetFileName, File lanFile) {
        AssetManager am = mContext.getAssets();

        if (!lanFile.exists()) {
            DataInputStream dis = null;
            FileOutputStream fos = null;
            try {
                if (lanFile.createNewFile()) {
                    dis = new DataInputStream(am.open(assetFileName));
                    fos = new FileOutputStream(lanFile);

                    byte[] buffer = new byte[1024];
                    while (dis.read(buffer) > 0) {
                        fos.write(buffer);
                    }

                    fos.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
