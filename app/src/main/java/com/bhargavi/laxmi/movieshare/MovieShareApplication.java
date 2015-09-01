package com.bhargavi.laxmi.movieshare;

import android.app.Application;

import com.bhargavi.laxmi.movieshare.service.MySqlLiteHelper;
import com.squareup.otto.Bus;

/**
 * Created by laxmi on 7/12/15.
 */
public class MovieShareApplication extends Application {
    public static Bus eventBus = new Bus();

    @Override
    public void onCreate() {
        super.onCreate();
        MySqlLiteHelper.getInstance(this).open();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        MySqlLiteHelper.getInstance(this).close();
    }
}
