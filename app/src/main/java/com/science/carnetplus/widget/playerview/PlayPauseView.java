package com.science.carnetplus.widget.playerview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.animation.DecelerateInterpolator;

/**
 * @author 幸运Science-陈土燊
 * @description 音乐开启关闭开关
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/30
 */
public class PlayPauseView extends FloatingActionButton implements PlayPauseDrawable.OnPlayPauseToggleListener {

    private CircleImageRotateView mCircleImageRotateView;
    private PlayPauseDrawable mPlayPauseDrawable;
    /**
     * Animator set for play pause toggle
     */
    private AnimatorSet mAnimatorSet;
    /**
     * play pause animation duration
     */
    private static final long PLAY_PAUSE_ANIMATION_DURATION = 200;
    private boolean mFirstDraw = true;

    public PlayPauseView(Context context) {
        super(context);
        init(context, null);
    }

    public PlayPauseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PlayPauseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        mPlayPauseDrawable = new PlayPauseDrawable(context);
        mPlayPauseDrawable.setCallback(callback);
        mPlayPauseDrawable.setToggleListener(this);
        if (mFirstDraw) {
            toggle();
            mFirstDraw = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int preferredSize = getResources().getDimensionPixelSize(android.support.design.R.dimen.design_fab_size_normal);

        final int w = resolveAdjustedSize(preferredSize, widthMeasureSpec);
        final int h = resolveAdjustedSize(preferredSize, heightMeasureSpec);

        this.setMeasuredDimension(w, h);

        //We resize play/pause drawable with button radius. button needs to be inside circle.
        mPlayPauseDrawable.resize((1.2f * w / 10.0f), (3.0f * w / 10.0f) + 10.0f, (w / 10.0f));

        mPlayPauseDrawable.setBounds(0, 0, w, h);
        setImageDrawable(mPlayPauseDrawable);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private int resolveAdjustedSize(int desiredSize, int measureSpec) {
        int result = desiredSize;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                // Parent says we can be as big as we want. Just don't be larger
                // than max size imposed on ourselves.
                result = desiredSize;
                break;
            case MeasureSpec.AT_MOST:
                // Parent says we can be as big as we want, up to specSize.
                // Don't be larger than specSize, and don't be larger than
                // the max size imposed on ourselves.
                result = Math.min(desiredSize, specSize);
                break;
            case MeasureSpec.EXACTLY:
                // No choice. Do what we are told.
                result = specSize;
                break;
        }
        return result;
    }

    public void initCircleImageRotateView(CircleImageRotateView circleImageRotateView) {
        mCircleImageRotateView = circleImageRotateView;
        mCircleImageRotateView.initPlayPauseDrawable(mPlayPauseDrawable);
    }

    /**
     * Play pause drawable callback
     */
    Drawable.Callback callback = new Drawable.Callback() {
        @Override
        public void invalidateDrawable(Drawable who) {
            postInvalidate();
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {

        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {

        }
    };

    /**
     * Animate play/pause image
     */
    @Override
    public void onToggled() {
        toggle();
    }

    /**
     * Animate play/pause image
     */
    public void toggle() {
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }

        mAnimatorSet = new AnimatorSet();
        final Animator pausePlayAnim = mPlayPauseDrawable.getPausePlayAnimator();
        mAnimatorSet.setInterpolator(new DecelerateInterpolator());
        mAnimatorSet.setDuration(PLAY_PAUSE_ANIMATION_DURATION);
        mAnimatorSet.playTogether(pausePlayAnim);
        mAnimatorSet.start();
    }

    public void playPauseStart() {
        mCircleImageRotateView.start();
    }

    public void playPauseStop() {
        mCircleImageRotateView.stop();
    }
}
