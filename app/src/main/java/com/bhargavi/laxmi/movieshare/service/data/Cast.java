package com.bhargavi.laxmi.movieshare.service.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by laxmi on 7/13/15.
 */
public class Cast implements Parcelable {

   @SerializedName("cast_id")
    private int castId ;
    private String character;

    @SerializedName("credit_id")
    private String creditId;
    private String name;
    private long id;

    @SerializedName("profile_path")
    private String profilePath;
    private int order;

    public int getCastId() {
        return castId;
    }

    public String getCharacter() {
        return character;
    }

    public String getCreditId() {
        return creditId;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public int getOrder() {
        return order;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.castId);
        dest.writeString(this.character);
        dest.writeString(this.creditId);
        dest.writeString(this.name);
        dest.writeLong(this.id);
        dest.writeString(this.profilePath);
        dest.writeInt(this.order);
    }

    public Cast() {
    }

    protected Cast(Parcel in) {
        this.castId = in.readInt();
        this.character = in.readString();
        this.creditId = in.readString();
        this.name = in.readString();
        this.id = in.readLong();
        this.profilePath = in.readString();
        this.order = in.readInt();
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        public Cast createFromParcel(Parcel source) {
            return new Cast(source);
        }

        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };
}
