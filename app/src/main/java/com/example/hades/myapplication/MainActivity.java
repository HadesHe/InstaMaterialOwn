package com.example.hades.myapplication;

import android.os.Bundle;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.hades.myapplication.activity.BaseDrawerActivity;
import com.example.hades.myapplication.adapter.FeedAdapter;

import java.util.List;

import butterknife.BindView;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseDrawerActivity {

    public static final String ACTION_SHOW_LOADING_ITEM="action_show_loading_item";

    private static final int ANIM_DURATION_TOOLBAR=300;
    private static final int ANIM_DURATION_FAB=400;

    @BindView(R.id.rvFeed)
    RecyclerView rvFeed;
    @BindView(R.id.btnCreate)
    FloatingActionButton fabCreate;
    @BindView(R.id.content)
    CoordinatorLayout clContent;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    }
}
