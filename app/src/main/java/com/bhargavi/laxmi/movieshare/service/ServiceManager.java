package com.bhargavi.laxmi.movieshare.service;

import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

import com.bhargavi.laxmi.movieshare.MediaListFragment;
import com.bhargavi.laxmi.movieshare.service.data.CastResponse;
import com.bhargavi.laxmi.movieshare.service.data.Configuration;
import com.bhargavi.laxmi.movieshare.service.data.GeneralData;
import com.bhargavi.laxmi.movieshare.service.data.GenreResponse;
import com.bhargavi.laxmi.movieshare.service.data.ImageResponse;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.bhargavi.laxmi.movieshare.service.data.MediaResponse;
import com.bhargavi.laxmi.movieshare.service.data.VideoResponse;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by laxmi on 7/7/15.
 */
public class ServiceManager {
    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String API_KEY = "bd33da5ccfead90bec4c3dfccd9588ec";

    private static OkHttpClient client = new OkHttpClient();

    private static Gson gson = new Gson();

    public static Configuration configuration;

    public static SparseArray<GeneralData> sGenres;

    public static String makeCall(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Log.d(ServiceManager.class.getSimpleName(), "Request url " + url);

        Response response = client.newCall(request).execute();
        String json = response.body().string();
        Log.d(ServiceManager.class.getSimpleName(), "Response " + json);
        return json;
    }

    public static MediaResponse searchMedia(String query, int page, boolean showAdult) throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
        builder.path("3/search/multi");
        builder.appendQueryParameter("api_key", API_KEY);
        builder.appendQueryParameter("query", query);
        builder.appendQueryParameter("page", String.valueOf(page));
        builder.appendQueryParameter("include_adult", String.valueOf(showAdult));

        String url = builder.build().toString();
        String response = makeCall(url);

        MediaResponse mediaResponse = gson.fromJson(response, MediaResponse.class);

        return mediaResponse;
    }

    public static Media getMediaDetails(long movieId, boolean isMovie) throws IOException {


        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        if (isMovie) {
            builder.path("3/movie/");
        } else {
            builder.path("3/tv/");
        }

        builder.appendPath(String.valueOf(movieId));
        builder.appendQueryParameter("api_key", API_KEY);

        String url = builder.build().toString();
        String response = makeCall(url);

        Media media = gson.fromJson(response, Media.class);
        media.setIsMovie(isMovie);
        return media;
    }

    public static void loadConfiguaration() throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();
        builder.path("3/configuration");
        builder.appendQueryParameter("api_key", API_KEY);

        String url = builder.build().toString();
        String response = makeCall(url);

        configuration = gson.fromJson(response, Configuration.class);

    }

    public static MediaResponse getMediaList(int type, int page) throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        boolean isMovie = true;

        if (type == MediaListFragment.TYPE_NOWPLAYING_MOVIES) {
            builder.path("3/movie/now_playing");
        } else if (type == MediaListFragment.TYPE_POPULAR_MOVIES) {
            builder.path("3/movie/popular");
        } else if (type == MediaListFragment.TYPE_TOPRATED_MOVIES) {
            builder.path("3/movie/top_rated");
        } else if (type == MediaListFragment.TYPE_NOWPLAYING_TVSHOWS) {
            isMovie = false;
            builder.path("3/tv/on_the_air");
        } else if (type == MediaListFragment.TYPE_POPULAR_TVSHOWS) {
            isMovie = false;
            builder.path("3/tv/popular");
        } else if (type == MediaListFragment.TYPE_TOPRATED_TVSHOWS) {
            isMovie = false;
            builder.path("3/tv/top_rated");
        }

        builder.appendQueryParameter("api_key", API_KEY);
        builder.appendQueryParameter("page", String.valueOf(page));

        String url = builder.build().toString();
        String response = makeCall(url);

        MediaResponse mediaResponse = gson.fromJson(response, MediaResponse.class);
        mediaResponse.setMediaType(isMovie);
        return mediaResponse;
    }

    public static CastResponse getCast(long id, boolean isMovie) throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        if (isMovie) {
            builder.path("3/movie/" + id + "/credits");
        } else {
            builder.path("3/tv/" + id + "/credits");
        }

        builder.appendQueryParameter("api_key", API_KEY);

        String url = builder.build().toString();
        String response = makeCall(url);

        CastResponse castResponse = gson.fromJson(response, CastResponse.class);
        return castResponse;
    }

    public static ImageResponse getImage(long id, boolean isMovie) throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        if (isMovie) {
            builder.path("3/movie/" + id + "/images");
        } else {
            builder.path("3/tv/" + id + "/images");
        }

        builder.appendQueryParameter("api_key", API_KEY);

        String url = builder.build().toString();
        String response = makeCall(url);

        ImageResponse imageResponse = gson.fromJson(response, ImageResponse.class);
        return imageResponse;

    }

    public static VideoResponse getVideo(long id, boolean isMovie) throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        if (isMovie) {
            builder.path("3/movie/" + id + "/videos");
        } else {
            builder.path("3/tv/" + id + "/videos");
        }

        builder.appendQueryParameter("api_key", API_KEY);

        String url = builder.build().toString();
        String response = makeCall(url);

        VideoResponse videoResponse = gson.fromJson(response, VideoResponse.class);
        return videoResponse;


    }

    public static MediaResponse getSimilarMedia(long id,boolean isMovie) throws IOException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        if (isMovie) {
            builder.path("3/movie/" + id + "/similar");
        } else {
            builder.path("3/tv/" + id + "/similar");
        }

        builder.appendQueryParameter("api_key", API_KEY);


        String url = builder.build().toString();
        String response = makeCall(url);

        MediaResponse mediaResponse = gson.fromJson(response, MediaResponse.class);
        mediaResponse.setMediaType(isMovie);
        return mediaResponse;

    }

    public static void getGenreList( boolean isMovie) throws IOException {



        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        if (isMovie) {
            builder.path("3/genre/movie/list");
        } else {
            builder.path("3/genre/tv/list");
        }

        builder.appendQueryParameter("api_key", API_KEY);


        String url = builder.build().toString();
        String response = makeCall(url);

        GenreResponse genreResponse = gson.fromJson(response, GenreResponse.class);
        if(sGenres == null){
            sGenres = new SparseArray<>();
        }

        if (genreResponse != null && genreResponse.getGenres() != null){

            for(GeneralData genre : genreResponse.getGenres()){
                sGenres.put(genre.getId(), genre);
            }
        }



    }


}
