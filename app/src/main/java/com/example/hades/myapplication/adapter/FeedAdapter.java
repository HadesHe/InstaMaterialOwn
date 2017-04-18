package com.example.hades.myapplication.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;

import com.example.hades.myapplication.MainActivity;
import com.example.hades.myapplication.R;
import com.example.hades.myapplication.view.LoadingFeedItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hades on 2017/4/12.
 */
public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final String ACTION_LIKE_BUTTON_CLICKED = "action_like_button_clicked";
    public static final String ACTION_LIKE_IMAGE_CLICKED = "action_like_image_clicked";

    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;

    private final List<FeedItem> feedItems = new ArrayList<>();

    private boolean showLoadingView = false;
    private final Context context;

    public FeedAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_DEFAULT) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
            CellFeedViewHolder cellFeedViewHolder=new CellFeedViewHolder(view);
            setupClickableViews(view,cellFeedViewHolder);
            return cellFeedViewHolder;

        } else if (viewType == VIEW_TYPE_LOADER) {
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class FeedItem {
        public int likesCount;
        public boolean isLiked;

        public FeedItem(int likesCount, boolean isLiked) {
            this.likesCount = likesCount;
            this.isLiked = isLiked;
        }
    }

    public static class CellFeedViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivFeedCenter)
        ImageView ivFeedCenter;
        @BindView(R.id.ivFeedBottom)
        ImageView ivFeedBottom;
        @BindView(R.id.btnComments)
        ImageButton btnComments;
        @BindView(R.id.btnLike)
        ImageButton btnLike;
        @BindView(R.id.btnMore)
        ImageButton btnMore;
        @BindView(R.id.vBgLike)
        View vBgLike;
        @BindView(R.id.ivLike)
        Image ivLike;
        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
        @BindView(R.id.ivUserProfile)
        ImageView ivUserProfile;
        @BindView(R.id.vImageRoot)
        ImageView vImageRoot;

        FeedItem feedItem;

        public CellFeedViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        public void bindView(FeedItem feedItem) {
            this.feedItem = feedItem;
            int adapterPosition = getAdapterPosition();
            ivFeedCenter.setImageResource(adapterPosition % 2 == 0 ? R.drawable.img_feed_center_1 : R.drawable.img_feed_center_2);
            ivFeedBottom.setImageResource(adapterPosition % 2 == 0 ? R.drawable.img_feed_bottom_1 : R.drawable.img_feed_bottom_2);
            btnLike.setImageResource(feedItem.isLiked ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
            tsLikesCounter.setCurrentText(vImageRoot.getResources().getQuantityString(R.plurals.likes_count, feedItem.likesCount, feedItem.likesCount));
        }

        public FeedItem getFeedItem() {
            return feedItem;
        }
    }

    public  static class LoadingCellFeedViewHolder extends CellFeedViewHolder{

        LoadingFeedItemView loadingFeedItemView;
        public LoadingCellFeedViewHolder(View itemView) {
            super(itemView);
            this.loadingFeedItemView=itemView;
        }
    }
}
