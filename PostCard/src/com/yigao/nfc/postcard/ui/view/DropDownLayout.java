
package com.yigao.nfc.postcard.ui.view;

import android.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;

public class DropDownLayout extends FrameLayout implements View.OnClickListener {

    private int mBottomHeight;

    private RelativeLayout mTopItemLayout;

    private FrameLayout mBottomLayout;

    private OnDropDownListener mDropDownListener;

    public DropDownLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DropDownLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownLayout(Context context) {
        this(context, null, 0);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
//        View container = inflater.inflate(R.layout.drop_down_layout, this, true);
//        mTopItemLayout = (RelativeLayout) container.findViewById(R.id.drop_down_top_layout);
//        mTopItemLayout.setOnClickListener(this);
//
//        mBottomLayout = (FrameLayout) container.findViewById(R.id.drop_down_bottom_layout);
        mBottomLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mTopItemLayout == v) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    mBottomLayout.getLayoutParams();
            if (params.height <= 0) {
                performBottomLayoutShow();
            } else {
                performBottomLayoutHide();
            }
        }
    }

    protected void performBottomLayoutShow() {
        enableDisableViewGroup(mBottomLayout, false);
        final ViewGroup.LayoutParams lp = mBottomLayout.getLayoutParams();
        mBottomLayout.setVisibility(View.VISIBLE);

        final long duration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        ValueAnimator animator = ValueAnimator.ofInt(0, mBottomHeight)
                .setDuration(duration);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                enableDisableViewGroup(mBottomLayout, true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mDropDownListener != null) {
                    mDropDownListener.onBottomLayoutOpened();
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                mBottomLayout.setLayoutParams(lp);
            }
        });

        animator.start();
    }

    protected void performBottomLayoutHide() {
        enableDisableViewGroup(mBottomLayout, false);
        final ViewGroup.LayoutParams lp = mBottomLayout.getLayoutParams();
        mBottomLayout.setVisibility(View.VISIBLE);

        final long duration = getResources().getInteger(android.R.integer.config_shortAnimTime);
        ValueAnimator animator = ValueAnimator.ofInt(mBottomHeight, 0)
                .setDuration(duration);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                enableDisableViewGroup(mBottomLayout, true);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (mDropDownListener != null) {
                    mDropDownListener.onBottomLayoutClosed();
                }
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                lp.height = (Integer) valueAnimator.getAnimatedValue();
                mBottomLayout.setLayoutParams(lp);
            }
        });

        animator.start();
    }

    public static void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = viewGroup.getChildAt(i);
            view.setEnabled(enabled);
            if (view instanceof ViewGroup) {
                enableDisableViewGroup((ViewGroup) view, enabled);
            }
        }
    }

    public View setTopLayout(int topLayout) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(topLayout, mTopItemLayout, true);
        return mTopItemLayout;
    }

    public View setBottomLayout(int bottomLayout) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(bottomLayout, mBottomLayout, true);

        mBottomLayout.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                mBottomLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mBottomHeight = mBottomLayout.getHeight();

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                        mBottomLayout.getLayoutParams();
                params.height = 0;
                mBottomLayout.setLayoutParams(params);
            }
        });

        return mBottomLayout;
    }

    public void setOnDropDownListener(OnDropDownListener listener) {
        mDropDownListener = listener;
    }

    public interface OnDropDownListener {

        public void onBottomLayoutOpened();

        public void onBottomLayoutClosed();
    }
}
