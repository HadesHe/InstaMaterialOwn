package com.example.hades.myapplication.activity;

import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.hades.myapplication.R;
import com.example.hades.myapplication.utils.CircleTransfomation;
import com.squareup.picasso.Picasso;

import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.BindView;

/**
 * Created by Hades on 2017/4/4.
 */

public class BaseDrawerActivity extends BaseActivity {

    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;
    @BindView(R.id.vNavigation)
    NavigationView vNavigation;

    @BindDimen(R.dimen.global_menu_avatar_size)
    int avatarSize;
    @BindString(R.string.user_profile_photo)
    String profilePhoto;

    private ImageView ivMenuUserProfilePhoto;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentViewWithoutInject(R.layout.activity_drawer);
        ViewGroup viewGroup= (ViewGroup) findViewById(R.id.flContentRoot);
        LayoutInflater.from(this).inflate(layoutResID,viewGroup,true);
        bindViews();
        setupHeader();
    }

    private void setupHeader() {
        View headerView=vNavigation.getHeaderView(0);
        ivMenuUserProfilePhoto=(ImageView)headerView.findViewById(R.id.ivMenuUserProfilePhoto);
        headerView.findViewById(R.id.vGlobalMenuHeader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalMenuHeaderClick(v);
            }
        });
        Picasso.with(this)
                .load(profilePhoto)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize,avatarSize)
                .centerCrop()
                .transform(new CircleTransfomation())
                .into(ivMenuUserProfilePhoto);

    }

    private void onGlobalMenuHeaderClick(final View v) {

        drawerLayout.closeDrawers();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               int[] startingLocation=new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0]+=v.getWidth()/2;
                UserProfileActivity.startUserProfileFromLocation(startingLocation,BaseDrawerActivity.this);
                overridePendingTransition(0,0);
            }
        },200);

    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if(getToolbar()!=null){
            getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });
        }
    }
}
