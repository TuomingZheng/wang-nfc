
package com.yigao.nfc.postcard.ui.fragment;

import java.io.File;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.nfc.wang.postcard.R;
import com.yigao.nfc.postcard.app.PostCardApplication;
import com.yogao.nfc.postcard.orc.ORCImagePreProcess;

public class ORCDetectFragment extends Fragment implements View.OnClickListener {

    private final String IMAGE_PATH;

    private String mLanguageType;

    private static TextView tvResult;
    private static ImageView ivSelected;
    private static ImageView ivTreated;

    private static Button mTakePictureButton;

    private static Button mChoicePhototButton;

    private static CheckBox chPreTreat;
    private static String textResult;
    private static Bitmap bitmapSelected;
    private static Bitmap bitmapTreated;
    private static final int SHOWRESULT = 0x101;
    private static final int SHOWTREATEDIMG = 0x102;

    private static final int REQUEST_TAKE_PHOTOS = 0x01;

    private static final int REQUEST_CHOOSE_IMAGE = 0x02;

    private static final int REQUEST_CROP_IMAGE = 0x03;

    public ORCDetectFragment() {
        super();

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            IMAGE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "PostCard" + File.separator + "images";
        } else {
            IMAGE_PATH = PostCardApplication.getInstance().getCacheDir().getAbsolutePath()
                    + File.separator + "images";
        }

        Log.d("zheng", "IMAGE_PATH = " + IMAGE_PATH);
        File imageDir = new File(IMAGE_PATH);
        if (!imageDir.exists()) {
            imageDir.mkdir();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("zheng", "onActivityResult() method called!");
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkORCImageDirctory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.orc_detect_layout, null, false);

        tvResult = (TextView) rootLayout.findViewById(R.id.tv_result);
        ivSelected = (ImageView) rootLayout.findViewById(R.id.iv_selected);
        ivTreated = (ImageView) rootLayout.findViewById(R.id.iv_treated);

        chPreTreat = (CheckBox) rootLayout.findViewById(R.id.ch_pretreat);

        mTakePictureButton = (Button) rootLayout.findViewById(R.id.btn_camera);
        mTakePictureButton.setOnClickListener(this);

        mChoicePhototButton = (Button) rootLayout.findViewById(R.id.btn_select);
        mChoicePhototButton.setOnClickListener(this);

        mLanguageType = "chi_sim";

        return rootLayout;
    }

    @Override
    public void onClick(View v) {
        if (v == mTakePictureButton) {
            performCameraAction();
        } else if (v == mChoicePhototButton) {
            performChooseImageAction();
        }
    }

    private void checkORCImageDirctory() {
        File imageDir = new File(IMAGE_PATH);
        if (imageDir.exists() || !imageDir.isDirectory()) {
            imageDir.mkdirs();
        }
    }

    private void performCameraAction() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMAGE_PATH, "temp.jpg")));
        startActivityForResult(intent, REQUEST_TAKE_PHOTOS);
    }

    private void performChooseImageAction() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMAGE_PATH, "temp_cropped.jpg")));
        intent.putExtra("outputFormat",
                Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CHOOSE_IMAGE);
    }

    private void handleTakeCameraAction() {
        Uri imageUri = Uri.fromFile(new File(IMAGE_PATH, "temp.jpg"));
        performImageCropAction(imageUri);
    }

    private void handleChooseImageAction() {

        bitmapSelected = decodeUriAsBitmap(Uri.fromFile(new File(IMAGE_PATH,
                "temp_cropped.jpg")));
        if (bitmapSelected != null) {
            if (chPreTreat.isChecked()) {
                tvResult.setText("预处理中......");
            } else {
                tvResult.setText("识别中......");
            }
            ivSelected.setImageBitmap(bitmapSelected);

            if (chPreTreat.isChecked()) {
                bitmapTreated = ORCImagePreProcess
                        .doPretreatment(bitmapSelected);
                Message msg = new Message();
                msg.what = SHOWTREATEDIMG;
                mORDHandler.sendMessage(msg);
                textResult = detectWordsOnImage(bitmapTreated);
            } else {
                bitmapTreated = ORCImagePreProcess
                        .converyToGrayImg(bitmapSelected);
                Message msg = new Message();
                msg.what = SHOWTREATEDIMG;
                mORDHandler.sendMessage(msg);
                textResult = detectWordsOnImage(bitmapTreated);
            }
            Message msg2 = new Message();
            msg2.what = SHOWRESULT;
            mORDHandler.sendMessage(msg2);
        }
    }

    private void performImageCropAction(Uri imageUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(new File(IMAGE_PATH, "temp_cropped.jpg")));
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP_IMAGE);
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

        baseApi.init("mnt/sdcard/", mLanguageType);

        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        baseApi.setImage(bitmap);

        String text = baseApi.getUTF8Text();

        baseApi.clear();
        baseApi.end();
        return text;
    }

    private Handler mORDHandler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SHOWRESULT:
                    if (textResult.equals(""))
                        tvResult.setText("预处理中.....");
                    else
                        tvResult.setText(textResult);
                    break;
                case SHOWTREATEDIMG:
                    tvResult.setText("识别中......");
                    ivTreated.setImageBitmap(bitmapTreated);
                    break;
            }
        };
    };
}
