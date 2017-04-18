package com.example.hades.myapplication.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * Created by Hades on 2017/4/6.
 */

public class RevealBackgroundView extends View {

    public static final int STATE_NOT_STARTED=0;
    public static final int STATE_FILL_STARTED=1;
    public static final int STATE_FINISH=2;
    private static final int FILL_TIME = 400;
    private static final Interpolator INTERPOLATOR = new AccelerateInterpolator();
    private Paint fillPaint;
    private int startLocationY;
    private int startLocationX;
    private int state=STATE_NOT_STARTED;
    private OnStateChangeListener onStateChangeListener;
    private ObjectAnimator revealAnimator;
    private float currentRadius;

    public RevealBackgroundView(Context context) {
        super(context);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RevealBackgroundView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        fillPaint=new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(Color.WHITE);
    }

    public void setFillPaintColor(int color){
        fillPaint.setColor(color);
    }

    public void startFromLocation(int[] tapLocationOnScreen){
        changeState(STATE_FILL_STARTED);
        startLocationX=tapLocationOnScreen[0];
        startLocationY=tapLocationOnScreen[1];
        revealAnimator= ObjectAnimator.ofInt(this,"currentRadius",0,getWidth()+getHeight()).setDuration(FILL_TIME);
        revealAnimator.setInterpolator(INTERPOLATOR);
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                changeState(STATE_FINISH);
            }
        });
        revealAnimator.start();
    }

    public void setToFinishedFrame(){
        changeState(STATE_FINISH);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(state==STATE_FINISH){
            canvas.drawRect(0,0,getWidth(),getHeight(),fillPaint);
        }else{
            canvas.drawCircle(startLocationX,startLocationY,currentRadius,fillPaint);
        }
    }

    private void changeState(int state) {
        if(this.state==state){
            return;
        }
        this.state=state;
        if(onStateChangeListener!=null){
            onStateChangeListener.onStateChange(state);
        }
    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public void setCurrentRadius(int radius){
        this.currentRadius=radius;
        invalidate();
    }

    public static interface OnStateChangeListener{
        void onStateChange(int state);
    }
}
