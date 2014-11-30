
package com.yigao.nfc.postcard.ui.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.app.FileManager;
import com.yigao.nfc.postcard.app.PostCardApplication;
import com.yigao.nfc.postcard.database.DataBaseUtil;
import com.yigao.nfc.postcard.database.model.ContactCompany;
import com.yigao.nfc.postcard.database.model.ContactEmail;
import com.yigao.nfc.postcard.database.model.ContactMobile;
import com.yigao.nfc.postcard.database.model.PostCard;
import com.yogao.nfc.postcard.orc.ORCImagePreProcess;

public class ORCDetectFragment extends Fragment implements View.OnClickListener {

    private TextView mTakePictureButton;

    private TextView mChoicePhototButton;

    private Button mMoreOptionsButton;

    private ImageView mBackButton;

    private ImageView mPostCardImageView;

    private LinearLayout mMenuLayout;

    private Button mSaveButton;

    private EditText mNameEditText;

    private EditText mMobileEditText;

    private EditText mEmailEditText;

    private EditText mCompanyEditText;

    private EditText mCompantAddressEditText;

    private EditText mStaffEditText;

    private EditText mDepartEditText;

    private static Bitmap bitmapTreated;

    private OnORCDetectEventListener mORCEventListener;

    private static final int REQUEST_TAKE_PHOTOS = 0x01;

    private static final int REQUEST_CHOOSE_IMAGE = 0x02;

    private static final int REQUEST_CROP_IMAGE = 0x03;

    public ORCDetectFragment() {
        super();
    }

    private void initTitleBar(View rootLayout) {
        mBackButton = (ImageView) rootLayout.findViewById(R.id.action_bar_back);
        mBackButton.setOnClickListener(this);

        TextView titleView = (TextView) rootLayout.findViewById(R.id.action_bar_title);
        titleView.setText(R.string.post_card_orc_title);

        mMoreOptionsButton = new Button(getActivity());
        mMoreOptionsButton.setBackgroundResource(R.drawable.more_options_selector);
        mMoreOptionsButton.setOnClickListener(this);

        FrameLayout rightContainer = (FrameLayout) rootLayout
                .findViewById(R.id.action_bar_right_container);
        rightContainer.addView(mMoreOptionsButton, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT));

        mMenuLayout = (LinearLayout) rootLayout.findViewById(R.id.menu_layout);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTOS) {
            if (Activity.RESULT_OK == resultCode) {
                handleTakeCameraAction();
            }
        } else if (requestCode == REQUEST_CHOOSE_IMAGE) {
            if (Activity.RESULT_OK == resultCode) {
                handleChooseImageAction();
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof OnORCDetectEventListener) {
            mORCEventListener = (OnORCDetectEventListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.orc_detect_layout, null, false);

        initTitleBar(rootLayout);

        initPostCardDetailsLayout(rootLayout);

        initMenuLayout(rootLayout);

        return rootLayout;
    }

    private void initPostCardDetailsLayout(View rootLayout) {
        mPostCardImageView = (ImageView) rootLayout.findViewById(R.id.post_card_image);

        mSaveButton = (Button) rootLayout.findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(this);

        mNameEditText = (EditText) rootLayout.findViewById(R.id.name_edit_text);
        mMobileEditText = (EditText) rootLayout.findViewById(R.id.phone_edit_text);
        mEmailEditText = (EditText) rootLayout.findViewById(R.id.email_edit_text);
        mCompanyEditText = (EditText) rootLayout.findViewById(R.id.company_edit_text);
        mStaffEditText = (EditText) rootLayout.findViewById(R.id.staff_edit_text);
        mDepartEditText = (EditText) rootLayout.findViewById(R.id.depart_edit_text);
        mCompantAddressEditText = (EditText) rootLayout
                .findViewById(R.id.company_address_edit_text);
    }

    private void initMenuLayout(View rootLayout) {
        mTakePictureButton = (TextView) rootLayout.findViewById(R.id.btn_camera);
        mTakePictureButton.setOnClickListener(this);

        mChoicePhototButton = (TextView) rootLayout.findViewById(R.id.btn_select);
        mChoicePhototButton.setOnClickListener(this);
    }

    private void changeMenuLayout() {
        int visibility = mMenuLayout.getVisibility();
        if (visibility == View.GONE) {
            mMenuLayout.setVisibility(View.VISIBLE);
        } else if (visibility == View.VISIBLE) {
            mMenuLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mTakePictureButton) {
            changeMenuLayout();
            performCameraAction();
        } else if (v == mChoicePhototButton) {
            changeMenuLayout();
            performChooseImageAction();
        } else if (v == mBackButton) {
            if (mORCEventListener != null) {
                mORCEventListener.onORCDetectCanceled();
            }
        } else if (mMoreOptionsButton == v) {
            changeMenuLayout();
        } else if (mSaveButton == v) {
            performSavePostCardAction();
        }
    }

    private void performCameraAction() {
        FileManager fm = PostCardApplication.getInstance().getFileManager();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(fm.getORCImageDir(), "temp.jpg")));
        startActivityForResult(intent, REQUEST_TAKE_PHOTOS);
    }

    private void performChooseImageAction() {
        FileManager fm = PostCardApplication.getInstance().getFileManager();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(fm.getORCImageDir(),
                        "temp_cropped.jpg")));
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
    }

    private void performSavePostCardAction() {
        if (mORCEventListener != null) {
            PostCard card = new PostCard();
            card.setContactName(mNameEditText.getText().toString());
            card.setRecordGenerateTimeStamp(System.currentTimeMillis());
            card.setID(card.getContactName());

            List<ContactMobile> mobiles = new ArrayList<ContactMobile>();
            ContactMobile mobile = new ContactMobile();
            mobile.setMobileNumber(mMobileEditText.getText().toString());
            mobile.setMobileOwnerId(card.getContactName());
            mobiles.add(mobile);
            card.setContactMobile(mobiles);

            List<ContactEmail> emails = new ArrayList<ContactEmail>();
            ContactEmail email = new ContactEmail();
            email.setEmailAddress(mEmailEditText.getText().toString());
            email.setEmailOwnerId(card.getContactName());
            emails.add(email);
            card.setContactEmails(emails);

            List<ContactCompany> companies = new ArrayList<ContactCompany>();
            ContactCompany company = new ContactCompany();
            company.setCompanyAddress(mCompantAddressEditText.getText().toString());
            company.setCompanyName(mCompanyEditText.getText().toString());
            company.setDepartment(mDepartEditText.getText().toString());
            company.setStaff(mStaffEditText.getText().toString());
            company.setOwnerId(card.getContactName());
            companies.add(company);
            card.setContactCompany(companies);

            DataBaseUtil database = new DataBaseUtil(getActivity());
            database.insertPostCard(card);
            if (mORCEventListener != null) {
                mORCEventListener.onORCDetectSuccess(card);
            }
        }
    }

    private void handleTakeCameraAction() {
        FileManager fm = PostCardApplication.getInstance().getFileManager();
        Uri imageUri = Uri.fromFile(new File(fm.getORCImageDir(), "temp.jpg"));
        performImageCropAction(imageUri);
    }

    private void handleChooseImageAction() {
        FileManager fm = PostCardApplication.getInstance().getFileManager();
        Uri fileUri = Uri.fromFile(new File(fm.getORCImageDir(), "temp_cropped.jpg"));
        new AsyncTask<Uri, Void, String>() {

            private ProgressDialog mProgressDialog;

            protected void onPreExecute() {
                Activity activity = getActivity();
                if (activity != null) {
                    mProgressDialog = new ProgressDialog(activity);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.setTitle(R.string.post_card_orc_dialog_title);
                    mProgressDialog.setMessage(activity
                            .getString(R.string.post_card_orc_dialog_message));
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                }
            };

            @Override
            protected String doInBackground(Uri... uri) {
                String text = null;

                final Bitmap image = decodeUriAsBitmap(uri[0]);
                if (image != null) {
                    mPostCardImageView.post(new Runnable() {

                        @Override
                        public void run() {
                            mPostCardImageView.setImageBitmap(image);
                        }
                    });
                    bitmapTreated = ORCImagePreProcess.converyToGrayImg(image);
                    text = detectWordsOnImage(bitmapTreated);
                }
                return text;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.hide();
                }

                if (!TextUtils.isEmpty(result)) {
                    result = result.replace(" ", "");
                    parsePostCardInfo(result);
                }
            }
        }.execute(fileUri);

    }

    private void performImageCropAction(Uri imageUri) {
        FileManager fm = PostCardApplication.getInstance().getFileManager();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(fm.getORCImageDir(), "temp_cropped.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
    }

    private void parsePostCardInfo(String text) {
        final String namePrefx = "姓名";
        parsePostCardItem(namePrefx, text, mNameEditText);

        final String mobilePrefx = "电话";
        parsePostCardItem(mobilePrefx, text, mMobileEditText);

        final String emailPrefix = "电子邮件";
        parsePostCardItem(emailPrefix, text, mEmailEditText);

        final String companyNamePrefx = "公司";
        parsePostCardItem(companyNamePrefx, text, mCompanyEditText);

        final String staffPrefx = "职务";
        parsePostCardItem(staffPrefx, text, mStaffEditText);

        final String departPrefx = "部门";
        parsePostCardItem(departPrefx, text, mDepartEditText);

        final String addressPrefx = "地址";
        parsePostCardItem(addressPrefx, text, mCompantAddressEditText);
    }

    private void parsePostCardItem(String prefx, String text, EditText editText) {
        int indexOfPrefx = text.indexOf(prefx);
        if (indexOfPrefx >= 0) {
            Log.d("zheng", "indexOfPrefx = " + indexOfPrefx);
            int indexOfNull = text.indexOf("\n", indexOfPrefx + prefx.length());
            Log.d("zheng", "indexOfNull = " + indexOfNull);
            if (indexOfNull >= 0) {
                String item = text.substring(indexOfPrefx + prefx.length(), indexOfNull);
                if (!TextUtils.isEmpty(item)) {
                    editText.setText(item);
                }
            }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri imageUri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(
                    PostCardApplication.getInstance().getContentResolver()
                            .openInputStream(imageUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String detectWordsOnImage(Bitmap bitmap) {
        TessBaseAPI baseApi = new TessBaseAPI();

        FileManager fm = PostCardApplication.getInstance().getFileManager();
        baseApi.init(fm.getORCLanguageDir().getParentFile().getPath(), "chi_sim");

        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);

        String text = baseApi.getUTF8Text();

        baseApi.clear();
        baseApi.end();
        return text;
    }

    public interface OnORCDetectEventListener {

        public void onORCDetectCanceled();

        public void onORCDetectSuccess(PostCard card);
    }
}
