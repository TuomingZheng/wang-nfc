
package com.yigao.nfc.postcard.ui.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.widget.Toast;

public class PostCardBaseFragment extends Fragment {

    public PostCardBaseFragment() {
        super();
    }

    public void showToastForShort(int resId) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToastForShort(String text) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToastForLong(int resId) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, resId, Toast.LENGTH_LONG).show();
        }
    }

    public void showToastForLong(String text) {
        Activity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
        }
    }
}
