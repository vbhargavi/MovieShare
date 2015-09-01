package com.bhargavi.laxmi.movieshare.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.movieshare.R;
import com.bhargavi.laxmi.movieshare.service.ServiceManager;
import com.bhargavi.laxmi.movieshare.service.data.Cast;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.bhargavi.laxmi.movieshare.service.data.Season;
import com.squareup.picasso.Picasso;

/**
 * Created by laxmi on 7/15/15.
 */
public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {

    public static final int TYPE_CAST = 0;
    public static final int TYPE_VIDEOS = 2;
    public static final int TYPE_SIMILAR = 3;
    public static final int TYPE_SEASONS = 4;

    private Context mContext;
    private int mType;
    private Cast[] mCasts;
    private Media[] mMedia;
    private Season[] mSeasons;

    private Drawable mPlaceHolder;

    private OnHorizontalItemClicked mListener;

    public interface OnHorizontalItemClicked {
        void onItemClicked(int type, Object data);
    }

    public void setOnHorizontalItemListener(OnHorizontalItemClicked listener) {
        mListener = listener;
    }

    public HorizontalAdapter(Context context, int type) {
        mContext = context;
        mType = type;

        if (mType == TYPE_CAST) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mPlaceHolder = context.getDrawable(R.drawable.person_placeholder);
            } else {
                mPlaceHolder = context.getResources().getDrawable(R.drawable.person_placeholder);
            }
        } else {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                mPlaceHolder = context.getDrawable(R.mipmap.ic_launcher);
            } else {
                mPlaceHolder = context.getResources().getDrawable(R.mipmap.ic_launcher);
            }
        }
    }

    public void setCastData(Cast[] casts) {
        mCasts = casts;
    }

    public void setSimilarData(Media[] medias) {
        mMedia = medias;

    }

    public void setSeasonData(Season[] seasons) {
        mSeasons = seasons;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }


    @Override
    public int getItemCount() {
        if (mType == TYPE_CAST && mCasts != null) {
            return mCasts.length;
        } else if (mType == TYPE_SIMILAR && mMedia != null) {
            return mMedia.length;
        } else if (mType == TYPE_SEASONS && mSeasons != null) {
            return mSeasons.length;
        }
        return 0;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private ImageView imageView;
        int width;
        int height;
        String baseUrl;

        public ViewHolder(View itemView) {

            super(itemView);
            width = mContext.getResources().getDimensionPixelOffset(R.dimen.horizontal_image_width);
            height = mContext.getResources().getDimensionPixelOffset(R.dimen.horizontal_image_height);

            baseUrl = ServiceManager.configuration.getImageConfiguaration().getPosterImageUrl(92);

            imageView = (ImageView) itemView.findViewById(R.id.image);
            nameTextView = (TextView) itemView.findViewById(R.id.cast_text_view);
        }

        public void update(int position) {
            String name;
            String imageUrl = baseUrl;
            final Object data;

            if (mType == TYPE_CAST) {
                Cast cast = mCasts[position];
                name = cast.getName();
                imageUrl += cast.getProfilePath();
                data = cast;
            } else if (mType == TYPE_SIMILAR) {
                Media media = mMedia[position];
                name = media.getMediaName();
                imageUrl += media.getPosterPath();
                data = media;
            } else {
                Season season = mSeasons[position];
                name = "Season " + (season.getSeasonNum());
                imageUrl += season.getPosterPath();
                data = season;
            }

            nameTextView.setText(name);
            Picasso.with(mContext)
                    .load(imageUrl)
                    .resize(width, height)
                    .placeholder(mPlaceHolder)
                    .centerCrop()
                    .into(imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClicked(mType, data);
                    }
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onItemClicked(mType, data);
                    }
                }
            });
        }
    }

}
