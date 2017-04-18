package com.example.hades.myapplication.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.example.hades.myapplication.R;

import butterknife.ButterKnife;


/**
 * Created by Hades on 2017/4/13.
 */
public class LoadingFeedItemView  extends FrameLayout{
    public LoadingFeedItemView(Context context) {
        super(context);
        init();
    }

    public LoadingFeedItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingFeedItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LoadingFeedItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_feed_loader,this,true);
        ButterKnife.bind(this);
    }
}
