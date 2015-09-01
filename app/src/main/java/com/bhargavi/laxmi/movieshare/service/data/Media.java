package com.bhargavi.laxmi.movieshare.service.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.bhargavi.laxmi.movieshare.service.ServiceManager;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by laxmi on 7/7/15.
 */
public class Media implements Parcelable {

    public static final String TYPE_TV = "tv";
    public static final String TYPE_MOVIE = "movie";

    @SerializedName("backdrop_path")
    private String backdropImage;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("genre_ids")
    private int[] genreIds;

    private GeneralData[] genres;

    @SerializedName("spoken_languages")
    private GeneralData[] spokenLanguages;

    @SerializedName("production_companies")
    private GeneralData[] productionCompanies;

    @SerializedName("created_by")
    private GeneralData[] createdBy;

    private GeneralData[] networks;

    private long id;

    @SerializedName("original_language")
    private String originialLang;

    @SerializedName("original_name")
    private String originalName;

    private String overview;

    @SerializedName("origin_country")
    private String[] originCountry;

    @SerializedName("poster_path")
    private String posterPath;

    private double popularity;

    private String name;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("vote_count")
    private int voteCount;


    @SerializedName("vote_average")
    private double voteAverage;

    private boolean adult;

    @SerializedName("original_title")
    private String originalTitle;

    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    private boolean video;

    private int runtime;

    private String tagline;

    private String status;

    private int[] episodeRuntime;

    @SerializedName("last_air_date")
    private String lastairDate;

    @SerializedName("number_of_episodes")
    private int numOfEpisodes;

    @SerializedName("number_of_seasons")
    private int numOfSeasons;

    private Season[] seasons;

    @SerializedName("homepage")
    private String homePage;

    private boolean isFavorite;


    //setters


    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setGenres(GeneralData[] genres) {
        this.genres = genres;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }


//    getterMethods


    public boolean isFavorite() {
        return isFavorite;
    }

    public String getHomePage() {
        return homePage;
    }

    public String getBackdropImage() {
        return backdropImage;
    }


    public int[] getGenreIds() {
        return genreIds;
    }

    public GeneralData[] getGenres() {
        return genres;
    }

    public String getGenreStringforDb(){
        String genreString = "";
        if (genres != null) {
            for (int i = 0; i < genres.length; i++) {
                if (i != genres.length - 1) {
                    genreString += genres[i].getName() + ";";
                } else {
                    genreString += genres[i].getName();
                }
            }
        }

        return genreString;
    }

    public String getGenreString() {
        String genreString = "";
        if (genres != null) {
            for (int i = 0; i < genres.length; i++) {
                if (i != genres.length - 1) {
                    genreString += genres[i].getName() + " | ";
                } else {
                    genreString += genres[i].getName();
                }
            }
        } else if (genreIds != null) {
            for (int i = 0; i < genreIds.length; i++) {
                if (i != genreIds.length - 1) {
                    genreString += ServiceManager.sGenres.get(genreIds[i]).getName() + " | ";
                } else {
                    genreString += ServiceManager.sGenres.get(genreIds[i]).getName();
                }
            }

        }

        return genreString;
    }

    public GeneralData[] getSpokenLanguages() {
        return spokenLanguages;
    }

    public GeneralData[] getProductionCompanies() {
        return productionCompanies;
    }

    public GeneralData[] getCreatedBy() {
        return createdBy;
    }

    public GeneralData[] getNetworks() {
        return networks;
    }

    public long getId() {
        return id;
    }

    public String getOriginialLang() {
        return originialLang;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getOverview() {
        return overview;
    }

    public String[] getOriginCountry() {
        return originCountry;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getName() {
        return name;
    }

    public String getMediaType() {
        return mediaType;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDateApiString(){
        return releaseDate;
    }

    public String getFirstAirDateApiString(){
        return firstAirDate;
    }


    public boolean isVideo() {
        return video;
    }

    public int getRuntime() {
        return runtime;
    }

    public String getTagline() {
        return tagline;
    }

    public String getStatus() {
        return status;
    }

    public int[] getEpisodeRuntime() {
        return episodeRuntime;
    }

    public String getLastairDate() {
        return lastairDate;
    }

    public int getNumOfEpisodes() {
        return numOfEpisodes;
    }

    public int getNumOfSeasons() {
        return numOfSeasons;
    }

    public Season[] getSeasons() {
        return seasons;
    }

    public void setIsMovie(boolean isMovie) {
        mediaType = isMovie ? TYPE_MOVIE : TYPE_TV;
    }

    public boolean isMovie() {
        if (mediaType == null) {
            return true;
        }
        return mediaType.equalsIgnoreCase(TYPE_MOVIE);
    }

    public String getMediaName() {
        if (isMovie()) {
            return originalTitle;
        }
        return originalName;
    }

    public Date getReleaseDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (isMovie()) {
                if (releaseDate != null) {
                    return format.parse(releaseDate);
                } else {
                    new Date();
                }
            }

            if (firstAirDate != null) {
                return format.parse(firstAirDate);
            } else {
                new Date();
            }
        } catch (ParseException ex) {

        }

        return new Date();
    }

    public String getReleaseDateString() {
        Date date = getReleaseDate();
        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
        return format.format(date);
    }

    public int getReleaseYear() {
        Date dt = getReleaseDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int year = cal.get(Calendar.YEAR);
        return year;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.backdropImage);
        dest.writeString(this.firstAirDate);
        dest.writeIntArray(this.genreIds);
        dest.writeTypedArray(this.genres, flags);
        dest.writeTypedArray(this.spokenLanguages, flags);
        dest.writeTypedArray(this.productionCompanies, flags);
        dest.writeTypedArray(this.createdBy, flags);
        dest.writeTypedArray(this.networks, flags);
        dest.writeLong(this.id);
        dest.writeString(this.originialLang);
        dest.writeString(this.originalName);
        dest.writeString(this.overview);
        dest.writeStringArray(this.originCountry);
        dest.writeString(this.posterPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.name);
        dest.writeString(this.mediaType);
        dest.writeInt(this.voteCount);
        dest.writeDouble(this.voteAverage);
        dest.writeByte(adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.originalTitle);
        dest.writeString(this.title);
        dest.writeString(this.releaseDate);
        dest.writeByte(video ? (byte) 1 : (byte) 0);
        dest.writeInt(this.runtime);
        dest.writeString(this.tagline);
        dest.writeString(this.status);
        dest.writeIntArray(this.episodeRuntime);
        dest.writeString(this.lastairDate);
        dest.writeInt(this.numOfEpisodes);
        dest.writeInt(this.numOfSeasons);
        dest.writeTypedArray(this.seasons, flags);
        dest.writeByte(isFavorite ? (byte) 1 : (byte) 0);
    }

    public Media() {
    }

    protected Media(Parcel in) {
        this.backdropImage = in.readString();
        this.firstAirDate = in.readString();
        this.genreIds = in.createIntArray();
        this.genres = in.createTypedArray(GeneralData.CREATOR);
        this.spokenLanguages = in.createTypedArray(GeneralData.CREATOR);
        this.productionCompanies = in.createTypedArray(GeneralData.CREATOR);
        this.createdBy = in.createTypedArray(GeneralData.CREATOR);
        this.networks = in.createTypedArray(GeneralData.CREATOR);
        this.id = in.readLong();
        this.originialLang = in.readString();
        this.originalName = in.readString();
        this.overview = in.readString();
        this.originCountry = in.createStringArray();
        this.posterPath = in.readString();
        this.popularity = in.readDouble();
        this.name = in.readString();
        this.mediaType = in.readString();
        this.voteCount = in.readInt();
        this.voteAverage = in.readDouble();
        this.adult = in.readByte() != 0;
        this.originalTitle = in.readString();
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.video = in.readByte() != 0;
        this.runtime = in.readInt();
        this.tagline = in.readString();
        this.status = in.readString();
        this.episodeRuntime = in.createIntArray();
        this.lastairDate = in.readString();
        this.numOfEpisodes = in.readInt();
        this.numOfSeasons = in.readInt();
        this.seasons = in.createTypedArray(Season.CREATOR);
        this.isFavorite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Media> CREATOR = new Parcelable.Creator<Media>() {
        public Media createFromParcel(Parcel source) {
            return new Media(source);
        }

        public Media[] newArray(int size) {
            return new Media[size];
        }
    };
}

