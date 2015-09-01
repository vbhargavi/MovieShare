package com.bhargavi.laxmi.movieshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bhargavi.laxmi.movieshare.R;
import com.bhargavi.laxmi.movieshare.service.data.Media;

import java.util.ArrayList;

/**
 * Created by laxmi on 7/23/15.
 */
public class ShareAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context mContext;
    private ArrayList<SocialShare> mData;

    public ShareAdapter(Context context){
        mContext = context;
        inflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
        mData.add(new SocialShare("Facebook", R.drawable.ic_fb_logo));
        mData.add(new SocialShare("Twitter", R.drawable.ic_twitter_logo));
        mData.add(new SocialShare("GooglePlus", R.drawable.ic_googleplus));

    }
    @Override
    public int getCount() {
        if (mData != null){
           return mData.size();
        }
       return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mData != null){
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.share_item, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SocialShare socialShare = (SocialShare) getItem(position);
        if (socialShare != null) {
            viewHolder.update(socialShare);
        }
        return convertView;


    }

    private class SocialShare{
        String name;
        int iconId;

        public SocialShare(String name, int iconId) {
            this.name = name;
            this.iconId = iconId;
        }
    }

    private class ViewHolder{

        private ImageView shareImageView;
        private TextView shareTextView;

        public ViewHolder(View view){

            shareImageView = (ImageView) view.findViewById(R.id.facebook_image);
            shareTextView = (TextView) view.findViewById(R.id.facebook_text_view);

        }

        public void update(SocialShare socialShare){

            shareImageView.setImageResource(socialShare.iconId);
            shareTextView.setText(socialShare.name);
        }
    }


}
