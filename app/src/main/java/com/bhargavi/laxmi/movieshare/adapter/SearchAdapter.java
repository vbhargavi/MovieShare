package com.bhargavi.laxmi.movieshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.movieshare.R;
import com.bhargavi.laxmi.movieshare.service.ServiceManager;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.bhargavi.laxmi.movieshare.service.data.MediaResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import static com.bhargavi.laxmi.movieshare.R.id.moviename_text;

/**
 * Created by laxmi on 7/7/15.
 */
public class SearchAdapter extends ArrayAdapter<Media> implements Filterable {

    private LayoutInflater inflater;
    private Context mContext;

    private Media[] mediaArray;

    public SearchAdapter(Context context) {
        super(context, 0);
        mContext = context;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public Media getItem(int position) {
        if (mediaArray != null) {
            return mediaArray[position];
        }
        return null;
    }

    @Override
    public int getCount() {

        if (mediaArray == null) {
            return 0;
        }
        return mediaArray.length;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    try {
                        MediaResponse response = ServiceManager.searchMedia(constraint.toString(), 1, true);
                        mediaArray = response.getResults();

                        filterResults.values = mediaArray;
                        filterResults.count = mediaArray.length;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }

            }
        };
        return filter;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_autocomplete_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Media media = getItem(position);
        if (media != null) {
            viewHolder.update(media);
        }
        return convertView;
    }

    private class ViewHolder {

        ImageView movieImageView;
        TextView movieTextView;
        TextView yearTextView;

        public ViewHolder(View view) {

            movieImageView = (ImageView) view.findViewById(R.id.movie_image);
            movieTextView = (TextView) view.findViewById(R.id.moviename_text);
            yearTextView = (TextView) view.findViewById(R.id.year_text);
        }

        public void update(Media media) {

            movieTextView.setText(media.getMediaName());
            yearTextView.setText(String.valueOf(media.getReleaseYear()));
            int width = movieImageView.getLayoutParams().width;
            int height = movieImageView.getLayoutParams().height;
            String imageUrl = ServiceManager.configuration.getImageConfiguaration().getPosterImageUrl(width) + media.getPosterPath();

            Picasso.with(mContext)
                    .load(imageUrl)
                    .resize(width, height)
                    .centerCrop()
                    .into(movieImageView);
        }

    }


}
