package com.science.carnetplus.widget.materialProgress;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author 幸运Science-陈土燊
 * @description material design加载动画
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/13
 */

public class LoadingView extends RelativeLayout {

    private CircleImageView mCircleView;
    private MaterialProgressDrawable mProgress;
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;
    private static final int CIRCLE_DIAMETER_Big = 40;
    private int mCircleWidth;
    private int mCircleHeight;

    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;

    public LoadingView(Context context) {
        super(context);
        initView(getContext());
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(getContext());
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(getContext());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(getContext());
    }

    private void initView(Context context) {
        createProgressView();
        final DisplayMetrics metrics = getResources().getDisplayMetrics();
        mCircleHeight = mCircleWidth = (int) (CIRCLE_DIAMETER_Big * metrics.density);
        setVisibility(VISIBLE);
    }

    private void createProgressView() {
        mCircleView = new CircleImageView(getContext(), CIRCLE_BG_LIGHT, CIRCLE_DIAMETER_Big / 2);
        if (true) {
            mProgress = new MaterialProgressDrawable(getContext(), this);
            mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(mProgress);
            mProgress.setAlpha(256);
            mProgress.setColorSchemeColors(0xff4285f4, 0xffea4335, 0xfffbbc06, 0xff34a853);
            addView(imageView);
        } else {
            mProgress = new MaterialProgressDrawable(getContext(), this);
            mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
            mCircleView.setImageDrawable(mProgress);
            mCircleView.setVisibility(View.VISIBLE);
            mProgress.setAlpha(256);
            mProgress.setColorSchemeColors(0xff4285f4, 0xffea4335, 0xfffbbc06, 0xff34a853);
            addView(mCircleView);
        }
    }

    public void startAnimation() {

        mProgress.start();
    }

    public void stopAnimation() {

        mProgress.stop();
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == GONE || visibility == INVISIBLE) {

            mProgress.stop();
        } else {
            mProgress.start();
            mProgress.showArrow(true);
        }
        super.setVisibility(visibility);
    }

    /**
     * @param progress
     */
    private void setAnimationProgress(float progress) {
        ViewCompat.setScaleX(mCircleView, progress);
        ViewCompat.setScaleY(mCircleView, progress);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final int width = getMeasuredWidth();
        int circleWidth = mCircleView.getMeasuredWidth();
        int circleHeight = mCircleView.getMeasuredHeight();
        mCircleView.layout((width / 2 - circleWidth / 2), 0,
                (width / 2 + circleWidth / 2), circleHeight);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCircleView.measure(MeasureSpec.makeMeasureSpec(mCircleWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mCircleHeight, MeasureSpec.EXACTLY));

    }
}

