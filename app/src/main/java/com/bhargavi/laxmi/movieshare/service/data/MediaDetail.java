package com.bhargavi.laxmi.movieshare.service.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by laxmi on 7/14/15.
 */
public class MediaDetail implements Parcelable {

    private Media media;
    private Image[] backdropImages;
    private Image[] posterImages;
    private Video[] videos;
    private Media[] similarMedia;
    private Cast[] cast;

    public MediaDetail() {
    }

    public Media getMedia() {
        return media;
    }

    public Cast[] getCast() {
        return cast;
    }

    public void setCast(Cast[] cast) {
        this.cast = cast;
    }

    public Video[] getVideos() {
        return videos;
    }

    public Media[] getSimilarMedia() {
        return similarMedia;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public Image[] getPosterImages() {
        return posterImages;
    }

    public void setPosterImages(Image[] posterImages) {
        this.posterImages = posterImages;
    }

    public Image[] getBackdropImages() {
        return backdropImages;
    }

    public void setBackdropImages(Image[] backdropImages) {
        this.backdropImages = backdropImages;
    }

    public void setVideos(Video[] videos) {
        this.videos = videos;
    }

    public void setSimilarMedia(Media[] similarMedia) {
        this.similarMedia = similarMedia;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.media, 0);
        dest.writeTypedArray(this.backdropImages, flags);
        dest.writeTypedArray(this.posterImages, flags);
        dest.writeTypedArray(this.videos, flags);
        dest.writeTypedArray(this.similarMedia, flags);
        dest.writeTypedArray(this.cast, flags);
    }

    protected MediaDetail(Parcel in) {
        this.media = in.readParcelable(Media.class.getClassLoader());
        this.backdropImages =  in.createTypedArray(Image.CREATOR);
        this.posterImages =  in.createTypedArray(Image.CREATOR);
        this.videos =  in.createTypedArray(Video.CREATOR);
        this.similarMedia =  in.createTypedArray(Media.CREATOR);
        this.cast =  in.createTypedArray(Cast.CREATOR);
    }

    public static final Parcelable.Creator<MediaDetail> CREATOR = new Parcelable.Creator<MediaDetail>() {
        public MediaDetail createFromParcel(Parcel source) {
            return new MediaDetail(source);
        }

        public MediaDetail[] newArray(int size) {
            return new MediaDetail[size];
        }
    };
}
