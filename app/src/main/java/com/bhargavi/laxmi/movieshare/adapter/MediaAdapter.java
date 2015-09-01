package com.bhargavi.laxmi.movieshare.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.movieshare.R;
import com.bhargavi.laxmi.movieshare.service.ServiceManager;
import com.bhargavi.laxmi.movieshare.MediaDetailActivity;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by laxmi on 7/12/15.
 */
public class MediaAdapter extends RecyclerView.Adapter {
    private ArrayList<Media> mMedia;
    private Context mContext;
    private int mCurrentPage;
    private int mTotalPages;
    private int mLoadingIndex;
    private MediaAdapterInterface mListener;
    private boolean mIsDataLoading;

    private static final int VIEW_MEDIA = 0;
    private static final int VIEW_LOADING = 1;

    public interface MediaAdapterInterface {
        void loadNextPage(int page);
    }

    public void setListner(MediaAdapterInterface mediaAdapterInterface) {
        mListener = mediaAdapterInterface;
    }

    public MediaAdapter(ArrayList<Media> media, Context context, int currentPage, int totalPages) {
        mContext = context;
        mMedia = media;
        mCurrentPage = currentPage;
        mTotalPages = totalPages;
    }

    public void notifyPageChange(int currentPage) {
        mIsDataLoading = false;
        mCurrentPage = currentPage;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mLoadingIndex) {
            return VIEW_LOADING;
        }
        return VIEW_MEDIA;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_MEDIA) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.media_list_item, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view, mContext);
            return viewHolder;
        }
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loading_more_layout, viewGroup, false);
        LoadingViewHolder viewHolder = new LoadingViewHolder(view, mContext);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(i == mLoadingIndex){
            if (mListener != null && !mIsDataLoading) {
                mIsDataLoading = true;
                mListener.loadNextPage(mCurrentPage + 1);
            }
        }
        if (viewHolder instanceof ViewHolder) {
            ViewHolder holder = (ViewHolder) viewHolder;
            final Media media = mMedia.get(i);
            holder.update(media);
        } else {
            LoadingViewHolder holder = (LoadingViewHolder) viewHolder;
            holder.update();
        }

    }


    @Override
    public int getItemCount() {
        if (mMedia != null) {
            if (mCurrentPage == mTotalPages) {
                mLoadingIndex = -1;
                return mMedia.size();
            }
            mLoadingIndex = mMedia.size();
            return mLoadingIndex + 1;
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView movieTitleTextView;
        private TextView movieRatingTextView;
        private TextView movieGenreTextView;
        private Context mContext;


        public ViewHolder(View itemView, Context context) {
            super(itemView);
            mContext = context;
            imageView = (ImageView) itemView.findViewById(R.id.movieImage);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.movie_title);
            movieRatingTextView = (TextView) itemView.findViewById(R.id.rating_text);
            movieGenreTextView = (TextView) itemView.findViewById(R.id.movie_genres);

        }

        public void update(final Media media) {
            movieRatingTextView.setText(String.valueOf(media.getVoteAverage() + "/10"));
            movieTitleTextView.setText(media.getMediaName() + " (" + media.getReleaseYear() + ")");
            movieGenreTextView.setText(media.getGenreString());

            int width = imageView.getLayoutParams().width;
            int height = imageView.getLayoutParams().height;
            String imageUrl = ServiceManager.configuration.getImageConfiguaration().getPosterImageUrl(width) + media.getPosterPath();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MediaDetailActivity.class);
                    intent.putExtra(MediaDetailActivity.EXTRA_ID, media.getId());
                    intent.putExtra(MediaDetailActivity.EXTRA_ISMOVIE, media.isMovie());
                    intent.putExtra(MediaDetailActivity.EXTRA_TITLE, media.getMediaName());
                    mContext.startActivity(intent);
                }
            });

            Picasso.with(mContext)
                    .load(imageUrl)
                    .resize(width, height)
                    .centerCrop()
                    .into(imageView);
        }

    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private TextView loadingTextView;
        private Context mContext;

        public LoadingViewHolder(View view, Context context) {
            super(view);
            mContext = context;

            loadingTextView = (TextView) view.findViewById(R.id.loading_textview);
        }

        public void update() {

        }

    }


}
