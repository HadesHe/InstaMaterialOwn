package com.example.hades.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.hades.myapplication.activity.BaseActivity;
import com.example.hades.myapplication.activity.BaseDrawerActivity;
import com.example.hades.myapplication.activity.CommentsActivity;
import com.example.hades.myapplication.activity.TakePhotoActivity;
import com.example.hades.myapplication.activity.UserProfileActivity;
import com.example.hades.myapplication.adapter.FeedAdapter;
import com.example.hades.myapplication.adapter.FeedItemAnimator;
import com.example.hades.myapplication.view.FeedContextMenu;
import com.example.hades.myapplication.view.FeedContextMenuManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity implements FeedAdapter.OnFeedItemClickListener, FeedContextMenu.OnFeedContextMenuItemClickListener {

    public static final String ACTION_SHOW_LOADING_ITEM = "action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR=300;
    private static final int ANIM_DURATION_FAB=400;

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
    @BindView(R.id.btnCreate)
    FloatingActionButton fabCreate;
    @BindView(R.id.content)
    CoordinatorLayout clContent;

    private FeedAdapter feedAdapter;

    private boolean pendingIntroAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFeed();
        if(savedInstanceState==null){
            pendingIntroAnimation=true;
        }else{
            feedAdapter.updateItems(false);

        }
    }

    private void setupFeed(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this){
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        rvFeed.setLayoutManager(linearLayoutManager);

        feedAdapter=new FeedAdapter(this);
        feedAdapter.setOnFeedItemClickListener(this);
        rvFeed.setAdapter(feedAdapter);
        rvFeed.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                FeedContextMenuManager.getInstance().onScrolled(recyclerView,dx,dy);
            }
        });
        rvFeed.setItemAnimator(new FeedItemAnimator());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(ACTION_SHOW_LOADING_ITEM.equals(intent.getAction())){
            showFeedLoadingItemDelayed();
        }
    }

    private void showFeedLoadingItemDelayed(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               rvFeed.smoothScrollToPosition(0);
                feedAdapter.showLoadingView();
            }
        },500);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        if(pendingIntroAnimation){
            pendingIntroAnimation=false;
            startIntroAnimation();
        }
        return true;
    }

    private void startIntroAnimation(){
        fabCreate.setTranslationY(2*getResources().getDimensionPixelOffset(R.dimen.btn_fab_size));

        int actionbarSize=Utils.dpToPx(56);
        getToolbar().setTranslationY(-actionbarSize);
        getIvLogo().setTranslationY(-actionbarSize);
        getInboxMenuItem().getActionView().setTranslationY(-actionbarSize);

        getToolbar().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(300);
        getIvLogo().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(400);
        getInboxMenuItem().getActionView().animate()
                .translationY(0)
                .setDuration(ANIM_DURATION_TOOLBAR)
                .setStartDelay(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startContentAnimation();
                    }
                })
        .start();
    }

    private void startContentAnimation(){
        fabCreate.animate()
                .translationY(0)
                .setInterpolator(new OvershootInterpolator(1.f))
                .setStartDelay(300)
                .setDuration(ANIM_DURATION_FAB)
                .start();
        feedAdapter.updateItems(true);
    }

    @Override
    public void onCommentsClick(View view, int position) {
        final Intent intent=new Intent(this,CommentsActivity.class);
        int[] startingLocation=new int[2];
        view.getLocationOnScreen(startingLocation);
        intent.putExtra(CommentsActivity.ARG_DRAWING_START_LOCATION,startingLocation[1]);
        startActivity(intent);
        overridePendingTransition(0,0);
    }

    @Override
    public void onMoreClick(View view, int position) {
        FeedContextMenuManager.getInstance().toggleContextMenuFromView(view,position,this);
    }

    @Override
    public void onProfileClick(View view) {
        int[] startingLocation=new int[2];
        view.getLocationOnScreen(startingLocation);
        startingLocation[0]+=view.getWidth()/2;
        UserProfileActivity.startUserProfileFromLocation(startingLocation,this);
        overridePendingTransition(0,0);
    }

    @Override
    public void onReportClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onSharePhotoClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCopyShareUrlClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }

    @Override
    public void onCancelClick(int feedItem) {
        FeedContextMenuManager.getInstance().hideContextMenu();
    }


    @OnClick(R.id.btnCreate)
    public void onTakePhotoClick(){
        int[] startingLocation=new int[2];
        fabCreate.getLocationOnScreen(startingLocation);
        startingLocation[0]+=fabCreate.getWidth()/2;
        TakePhotoActivity.startCameraFromLocation(startingLocation,this);
        overridePendingTransition(0,0);
    }

    public void showLikedSnackbar(){
        Snackbar.make(clContent,"Liked!",Snackbar.LENGTH_SHORT).show();
    }
}
