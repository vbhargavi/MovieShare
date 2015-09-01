package com.bhargavi.laxmi.movieshare.service.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laxmi on 7/14/15.
 */
public class Image implements Parcelable {

    @SerializedName("aspect_ratio")
    private double aspectRatio;

    @SerializedName("file_path")
    private String filePath;

    private int height;
    private int width;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    private double voteAvg;

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAvg() {
        return voteAvg;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.aspectRatio);
        dest.writeString(this.filePath);
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAvg);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.aspectRatio = in.readDouble();
        this.filePath = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
        this.voteCount = in.readInt();
        this.voteAvg = in.readDouble();
    }

    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
