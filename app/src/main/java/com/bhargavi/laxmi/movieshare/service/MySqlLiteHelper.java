package com.bhargavi.laxmi.movieshare.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.bhargavi.laxmi.movieshare.service.data.GeneralData;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.bhargavi.laxmi.movieshare.service.data.MediaResponse;

import java.util.ArrayList;

/**
 * Created by stadiko on 8/28/15.
 */
public class MySqlLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "media_table";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_VOTE_AVG = "vote_average";
    public static final String COLUMN_ORIGINAL_TITLE = "original_title";
    public static final String COLUMN_ORIGINAL_NAME = "original_name";
    public static final String COLUMN_RELEASE_DATE = "release_date";
    public static final String COLUMN_FIRST_AIR_DATE = "first_air_date";
    public static final String COLUMN_GENRES = "genres";
    public static final String COLUMN_POSTER_PATH = "poster_path";
    public static final String COLUMN_MEDIA_TYPE = "media_type";

    private static final String DATABASE_NAME = "favorites.db";
    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_TABLE_SQL = "CREATE TABLE " + TABLE_NAME +
            " (" + COLUMN_ID + " NUMERIC NOT NULL PRIMARY KEY, "
            + COLUMN_VOTE_AVG + " REAL, "
            + COLUMN_ORIGINAL_TITLE + " TEXT, "
            + COLUMN_ORIGINAL_NAME + " TEXT, "
            + COLUMN_RELEASE_DATE + " TEXT, "
            + COLUMN_FIRST_AIR_DATE + " TEXT, "
            + COLUMN_GENRES + " TEXT, "
            + COLUMN_POSTER_PATH + " TEXT, "
            + COLUMN_MEDIA_TYPE + " TEXT)";

    private static MySqlLiteHelper sInstance;

    private SQLiteDatabase database;

    public static synchronized MySqlLiteHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new MySqlLiteHelper(context);
        }

        return sInstance;
    }

    private MySqlLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void open() throws SQLException {
        database = getWritableDatabase();
    }

    public void addMedia(Media media) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_ID + " = " + media.getId(), null, null, null, null);
        int cnt = cursor.getCount();
        cursor.close();

        if (cnt == 0) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_ID, media.getId());
            values.put(COLUMN_VOTE_AVG, media.getVoteAverage());
            values.put(COLUMN_ORIGINAL_TITLE, media.getOriginalTitle());
            values.put(COLUMN_ORIGINAL_NAME, media.getOriginalName());
            values.put(COLUMN_RELEASE_DATE, media.getReleaseDateApiString());
            values.put(COLUMN_FIRST_AIR_DATE, media.getFirstAirDateApiString());
            values.put(COLUMN_GENRES, media.getGenreStringforDb());
            values.put(COLUMN_POSTER_PATH, media.getPosterPath());
            values.put(COLUMN_MEDIA_TYPE, media.getMediaType());
            long insertId = database.insert(TABLE_NAME, null, values);
        }
    }

    public void removeMedia(long id) {
        database.delete(TABLE_NAME, COLUMN_ID + " = " + id, null);
    }

    public boolean isFavorite(long id) {
        Cursor cursor = database.query(TABLE_NAME, new String[]{COLUMN_ID}, COLUMN_ID + " = " + id, null, null, null, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt > 0;
    }

    public ArrayList<Media> getMedia(boolean isMovie) {
        ArrayList<Media> medias = new ArrayList<>();

        Cursor cursor = database.query(TABLE_NAME, null, COLUMN_MEDIA_TYPE + " = '" + (isMovie ? Media.TYPE_MOVIE : Media.TYPE_TV) + "'", null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Media media = cursorToMedia(cursor);
            medias.add(media);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return medias;
    }

    public MediaResponse getMediaResponse(boolean isMovie) {
        ArrayList<Media> mediaArrayList = getMedia(isMovie);
        int cnt = mediaArrayList.size();
        MediaResponse response = new MediaResponse();
        response.setMediaType(isMovie);
        response.setPage(1);
        response.setTotalPages(1);
        response.setTotalResults(cnt);
        Media[] medias = new Media[cnt];
        response.setResults(mediaArrayList.toArray(medias));
        return response;
    }

    private Media cursorToMedia(Cursor cursor) {
        Media media = new Media();
        media.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
        media.setVoteAverage(cursor.getDouble(cursor.getColumnIndex(COLUMN_VOTE_AVG)));
        media.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_TITLE)));
        media.setOriginalName(cursor.getString(cursor.getColumnIndex(COLUMN_ORIGINAL_NAME)));
        media.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_RELEASE_DATE)));
        media.setFirstAirDate(cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_AIR_DATE)));
        media.setPosterPath(cursor.getString(cursor.getColumnIndex(COLUMN_POSTER_PATH)));
        media.setMediaType(cursor.getString(cursor.getColumnIndex(COLUMN_MEDIA_TYPE)));

        String genreString = cursor.getString(cursor.getColumnIndex(COLUMN_GENRES));

        if (!TextUtils.isEmpty(genreString)) {
            String[] genreStrings = genreString.split(";");

            GeneralData[] genres = new GeneralData[genreStrings.length];

            int i = 0;

            for (String str : genreStrings) {
                GeneralData generalData = new GeneralData();
                generalData.setName(str);
                genres[i] = generalData;
                i++;
            }

            media.setGenres(genres);
        }
        return media;
    }
}
