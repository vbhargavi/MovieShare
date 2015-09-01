package com.bhargavi.laxmi.movieshare;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.bhargavi.laxmi.movieshare.MediaDetailFragment.OnMediaItemClicked;

public class MediaDetailActivity extends AppCompatActivity implements OnMediaItemClicked {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_ISMOVIE = "IsMovie";
    public static final String EXTRA_TITLE = "title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();

        if (savedInstanceState == null) {
            onSimilarClicked(bundle.getLong(EXTRA_ID), bundle.getBoolean(EXTRA_ISMOVIE), bundle.getString(EXTRA_TITLE));
        }
    }

    @Override
    public void onSimilarClicked(long id, boolean isMovie, String title) {
        String tag = MediaDetailFragment.class.getSimpleName() + id;
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MediaDetailFragment fragment = MediaDetailFragment.newInstance(id, isMovie, title);

        if (getSupportFragmentManager().findFragmentById(R.id.container_fragment) == null) {
            fragmentTransaction.add(R.id.container_fragment, fragment, tag);
        } else {
            fragmentTransaction.replace(R.id.container_fragment, fragment, tag);
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
