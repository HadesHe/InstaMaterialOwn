package com.example.hades.myapplication.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.DecorContentParent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.example.hades.myapplication.R;
import com.example.hades.myapplication.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Hades on 2017/4/19.
 */
public class FeedItemAnimator extends DefaultItemAnimator {

    private static final DecelerateInterpolator DECELERATE_INTERPOLATOR=new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR=new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR=new OvershootInterpolator(4);

    Map<RecyclerView.ViewHolder,AnimatorSet> likeAnimationMap=new HashMap<>();
    Map<RecyclerView.ViewHolder,AnimatorSet> heartAnimationMap=new HashMap<>();

    private int lastAddAnimatedItem=-2;

    @Override
    public boolean canReuseUpdatedViewHolder(@NonNull RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state, @NonNull RecyclerView.ViewHolder viewHolder, int changeFlags, @NonNull List<Object> payloads) {
        if(changeFlags==FLAG_CHANGED){
            for(Object payLoad:payloads){
                if(payLoad instanceof String){
                    return new FeedItemHolderInfo((String)payLoad);
                }
            }
        }
        return super.recordPreLayoutInformation(state,viewHolder,changeFlags,payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        if(holder.getItemViewType()==FeedAdapter.VIEW_TYPE_DEFAULT){
            if(holder.getLayoutPosition()>lastAddAnimatedItem){
                lastAddAnimatedItem++;
                runEnterAnimation((FeedAdapter.CellFeedViewHolder)holder);
                return false;
            }
        }

        dispatchAddFinished(holder);
        return false;
    }

    private void runEnterAnimation(final FeedAdapter.CellFeedViewHolder holder){
        final int screenHeight= Utils.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder, @NonNull RecyclerView.ViewHolder newHolder, @NonNull ItemHolderInfo preInfo, @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);
        if(preInfo instanceof FeedItemHolderInfo){
            FeedItemHolderInfo feedItemHolderInfo= (FeedItemHolderInfo) preInfo;
            FeedAdapter.CellFeedViewHolder holder= (FeedAdapter.CellFeedViewHolder) newHolder;

            animateHeartButton(holder);
            updateLikesCounter(holder,holder.getFeedItem().likesCount);
            if(FeedAdapter.ACTION_LIKE_IMAGE_CLICKED.equals(feedItemHolderInfo.updateAction)){
                animatePhotoLike(holder);
            }
        }
        return false;
    }

    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item){
        if(likeAnimationMap.containsKey(item)){
            likeAnimationMap.get(item).cancel();
        }

        if(heartAnimationMap.containsKey(item)){
            heartAnimationMap.get(item).cancel();
        }
    }

    private void animateHeartButton(final FeedAdapter.CellFeedViewHolder holder){
        AnimatorSet animatorSet=new AnimatorSet();
        ObjectAnimator rotationAnim=ObjectAnimator.ofFloat(holder.btnLike,"rotation",0f,360f);
        rotationAnim.setDuration(300);
        rotationAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        ObjectAnimator bounceAnimX=ObjectAnimator.ofFloat(holder.btnLike,"scaleX",0.2f,1f);
        bounceAnimX.setDuration(300);
        bounceAnimX.setInterpolator(OVERSHOOT_INTERPOLATOR);

        ObjectAnimator bounceAnimY=ObjectAnimator.ofFloat(holder.btnLike,"scaleY",0.2f,1f);
        bounceAnimY.setDuration(300);
        bounceAnimY.setInterpolator(OVERSHOOT_INTERPOLATOR);

        bounceAnimY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                holder.btnLike.setImageResource(R.drawable.ic_heart_red);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                heartAnimationMap.remove(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });

        animatorSet.play(bounceAnimX).with(bounceAnimY).after(rotationAnim);
        animatorSet.start();
        heartAnimationMap.put(holder,animatorSet);
    }

    private void updateLikesCounter(FeedAdapter.CellFeedViewHolder holder,int toValue){
        String likesCountTextFrom=holder.tsLikesCounter.getResources().getQuantityString(
                R.plurals.likes_count,toValue-1,toValue-1
        );
        holder.tsLikesCounter.setCurrentText(likesCountTextFrom);

        String likesCountTextTo=holder.tsLikesCounter.getResources().getQuantityString(
                R.plurals.likes_count,toValue,toValue
        );
        holder.tsLikesCounter.setText(likesCountTextTo);

    }

    private void animatePhotoLike(final FeedAdapter.CellFeedViewHolder holder){
        holder.vBgLike.setVisibility(View.VISIBLE);
        holder.ivLike.setVisibility(View.VISIBLE);

        holder.vBgLike.setScaleY(0.1f);
        holder.vBgLike.setScaleX(0.1f);
        holder.vBgLike.setAlpha(1f);
        holder.ivLike.setScaleX(0.1f);
        holder.ivLike.setScaleY(0.1f);

        AnimatorSet animatorSet=new AnimatorSet();

        ObjectAnimator bgScaleYAnim=ObjectAnimator.ofFloat(holder.vBgLike,"scaleY",0.1f,1f);
        bgScaleYAnim.setDuration(200);
        bgScaleYAnim.setInterpolator(DECELERATE_INTERPOLATOR);
        ObjectAnimator bgScaleXAnim=ObjectAnimator.ofFloat(holder.vBgLike,"scaleX",0.1f,1f);
        bgScaleXAnim.setDuration(200);
        bgScaleXAnim.setInterpolator(DECELERATE_INTERPOLATOR);
        ObjectAnimator bgAlphaAnim=ObjectAnimator.ofFloat(holder.vBgLike,"alpha",1f,0f);
        bgAlphaAnim.setDuration(200);
        bgAlphaAnim.setStartDelay(150);
        bgAlphaAnim.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleUpYAnim=ObjectAnimator.ofFloat(holder.ivLike,"scaleY",0.1f,1f);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(DECELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim=ObjectAnimator.ofFloat(holder.ivLike,"scaleX",0.1f,1f);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(DECELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleDownYAnim=ObjectAnimator.ofFloat(holder.ivLike,"scaleY",1f,0f);
        imgScaleDownYAnim.setDuration(300);
        imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleDownXAnim=ObjectAnimator.ofFloat(holder.ivLike,"scaleX",1f,0f);
        imgScaleDownXAnim.setDuration(300);
        imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        animatorSet.playTogether(bgScaleYAnim,bgScaleXAnim,bgAlphaAnim,imgScaleUpYAnim,imgScaleUpXAnim);
        animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                likeAnimationMap.remove(holder);
                resetLikeAnimationState(holder);
                dispatchChangeFinishedIfAllAnimationsEnded(holder);
            }
        });
        animatorSet.start();

        likeAnimationMap.put(holder,animatorSet);
    }

    private void dispatchChangeFinishedIfAllAnimationsEnded(FeedAdapter.CellFeedViewHolder holder){
        if(likeAnimationMap.containsKey(holder)||heartAnimationMap.containsKey(holder)){
            return;
        }

        dispatchAnimationFinished(holder);
    }

    private void resetLikeAnimationState(FeedAdapter.CellFeedViewHolder holder){
        holder.vBgLike.setVisibility(View.INVISIBLE);
        holder.ivLike.setVisibility(View.INVISIBLE);
    }

    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        for(AnimatorSet animatorSet:likeAnimationMap.values()){
            animatorSet.cancel();
        }
    }

    public static class FeedItemHolderInfo extends ItemHolderInfo{
        public String updateAction;

        public FeedItemHolderInfo(String updateAction){
            this.updateAction=updateAction;
        }
    }
}
