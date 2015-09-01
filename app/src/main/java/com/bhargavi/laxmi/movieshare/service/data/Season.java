package com.bhargavi.laxmi.movieshare.service.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laxmi on 7/7/15.
 */
public class Season implements Parcelable {

    @SerializedName("air_date")
    private String airDate;

    @SerializedName("episode_count")
    private int episodeCount;

    private int id;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("season_number")
    private int seasonNum;

    public int getEpisodeCount() {
        return episodeCount;
    }

    public String getAirDate() {
        return airDate;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getSeasonNum() {
        return seasonNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.airDate);
        dest.writeInt(this.episodeCount);
        dest.writeInt(this.id);
        dest.writeString(this.posterPath);
        dest.writeInt(this.seasonNum);
    }

    public Season() {
    }

    protected Season(Parcel in) {
        this.airDate = in.readString();
        this.episodeCount = in.readInt();
        this.id = in.readInt();
        this.posterPath = in.readString();
        this.seasonNum = in.readInt();
    }

    public static final Parcelable.Creator<Season> CREATOR = new Parcelable.Creator<Season>() {
        public Season createFromParcel(Parcel source) {
            return new Season(source);
        }

        public Season[] newArray(int size) {
            return new Season[size];
        }
    };
}
