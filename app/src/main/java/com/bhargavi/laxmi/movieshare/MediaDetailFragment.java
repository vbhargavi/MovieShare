package com.bhargavi.laxmi.movieshare;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bhargavi.laxmi.movieshare.adapter.HorizontalAdapter;
import com.bhargavi.laxmi.movieshare.service.MySqlLiteHelper;
import com.bhargavi.laxmi.movieshare.service.ServiceManager;
import com.bhargavi.laxmi.movieshare.service.data.CastResponse;
import com.bhargavi.laxmi.movieshare.service.data.ImageResponse;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.bhargavi.laxmi.movieshare.service.data.MediaDetail;
import com.bhargavi.laxmi.movieshare.service.data.MediaResponse;
import com.bhargavi.laxmi.movieshare.service.data.VideoResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MediaDetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<MediaDetail> {
    public static final String EXTRA_ID = "mId";
    public static final String EXTRA_ISMOVIE = "IsMovie";
    public static final String EXTRA_TITLE = "mTitle";
    public static final String EXTRA_MEDIA = "mMedia";

    private boolean mIsMovie;
    private long mId;
    private String mTitle;
    private MediaDetail mMediaDetail;

    private ProgressBar progressBar;
    private View contentView;
    private TextView genreTextView;
    private TextView ratingTextView;
    private TextView descriptionTextView;
    private TextView releasedateTextView;
    private TextView runtimeTextView;
    private TextView taglineTextView;
    private RecyclerView castRecyclerView;
    private RecyclerView similarRecyclerView;
    private ImageView backdropImageView;
    private CollapsingToolbarLayout collapsingToolbar;
    private View runtimeLayout;
    private View taglineLayout;
    private View castCardView;
    private View similarCardView;
    private View seasonCardView;
    private RecyclerView seasonRecyclerView;

    private FloatingActionButton favBtn;


    private HorizontalAdapter castAdapter;
    private HorizontalAdapter similarAdapter;
    private HorizontalAdapter seasonAdapter;

    private OnMediaItemClicked mListener;

    public interface OnMediaItemClicked {
        void onSimilarClicked(long id, boolean isMovie, String title);
    }


    public static MediaDetailFragment newInstance(long id, boolean isMovie, String title) {
        MediaDetailFragment fragment = new MediaDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_ID, id);
        bundle.putBoolean(EXTRA_ISMOVIE, isMovie);
        bundle.putString(EXTRA_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }


    public MediaDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnMediaItemClicked) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(EXTRA_ID, mId);
        outState.putString(EXTRA_TITLE, mTitle);
        outState.putBoolean(EXTRA_ISMOVIE, mIsMovie);
        outState.putParcelable(EXTRA_MEDIA, mMediaDetail);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_media_detail, container, false);
        genreTextView = (TextView) view.findViewById(R.id.movie_genre);
        ratingTextView = (TextView) view.findViewById(R.id.rating_text);
        descriptionTextView = (TextView) view.findViewById(R.id.overview_text);
        releasedateTextView = (TextView) view.findViewById(R.id.movie_releasedate);
        runtimeTextView = (TextView) view.findViewById(R.id.movie_runtime);
        taglineTextView = (TextView) view.findViewById(R.id.tagline_textview);
        backdropImageView = (ImageView) view.findViewById(R.id.backdrop_image);
        runtimeLayout = view.findViewById(R.id.run_linearlayout);
        taglineLayout = view.findViewById(R.id.tagline_layout);
        castCardView = view.findViewById(R.id.cast_cardview);
        similarCardView = view.findViewById(R.id.similar_cardview);
        seasonCardView = view.findViewById(R.id.season_cardview);

        favBtn = (FloatingActionButton) view.findViewById(R.id.fav_btn);

        LinearLayoutManager seasonlinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        seasonRecyclerView = (RecyclerView) view.findViewById(R.id.season_list);
        seasonRecyclerView.setHasFixedSize(true);
        seasonRecyclerView.setLayoutManager(seasonlinearLayoutManager);

        LinearLayoutManager castlinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        castRecyclerView = (RecyclerView) view.findViewById(R.id.cast_list);
        castRecyclerView.setHasFixedSize(true);
        castRecyclerView.setLayoutManager(castlinearLayoutManager);

        LinearLayoutManager similarLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        similarRecyclerView = (RecyclerView) view.findViewById(R.id.similar_list);
        similarRecyclerView.setHasFixedSize(true);
        similarRecyclerView.setLayoutManager(similarLinearLayoutManager);

        Bundle bundle;
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
            mMediaDetail = bundle.getParcelable(EXTRA_MEDIA);
        } else {
            bundle = getArguments();
        }
        mIsMovie = bundle.getBoolean(EXTRA_ISMOVIE);
        mId = bundle.getLong(EXTRA_ID);
        mTitle = bundle.getString(EXTRA_TITLE);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        contentView = view.findViewById(R.id.main_content);

        MovieShareApplication.eventBus.register(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();

        if (mMediaDetail == null) {
            loadData();
        } else {
            populateView();
        }
    }

    private void loadData() {
        getLoaderManager().initLoader(0, null, this);
        progressBar.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) getView().findViewById(R.id.toolbar);
        collapsingToolbar = (CollapsingToolbarLayout) getView().findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mTitle);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        final ActionBar actionBar = appCompatActivity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_navigation_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public Loader<MediaDetail> onCreateLoader(int id, Bundle args) {
        return new MediaDetailsLoader(getActivity(), this.mId, mIsMovie);
    }

    @Override
    public void onLoadFinished(Loader<MediaDetail> loader, MediaDetail data) {
        mMediaDetail = data;
        populateView();
    }


    private void populateView() {
        final Media media = mMediaDetail.getMedia();

        if (!TextUtils.isEmpty(media.getGenreString())) {
            genreTextView.setText(media.getGenreString());
        } else {
            genreTextView.setVisibility(View.GONE);
        }
        ratingTextView.setText((String.valueOf(media.getVoteAverage() + "/10")));
        descriptionTextView.setText(media.getOverview());
        releasedateTextView.setText(media.getReleaseDateString());
        if (media.getRuntime() > 0) {
            runtimeTextView.setText(String.valueOf(media.getRuntime()));
        } else {
            runtimeLayout.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(media.getTagline())) {
            taglineTextView.setText(media.getTagline());
        } else {
            taglineLayout.setVisibility(View.GONE);
        }


        String imageUrl = ServiceManager.configuration.getImageConfiguaration().getBackDropImageUrl(780) + media.getBackdropImage();

        Picasso.with(getActivity())
                .load(imageUrl)
                .into(backdropImageView);

        if (mMediaDetail.getCast() != null && mMediaDetail.getCast().length > 0) {
            if (castAdapter == null) {
                castAdapter = new HorizontalAdapter(getActivity(), HorizontalAdapter.TYPE_CAST);
            }
            castAdapter.setCastData(mMediaDetail.getCast());
            castRecyclerView.setAdapter(castAdapter);
        } else {
            castCardView.setVisibility(View.GONE);
        }

        if (mMediaDetail.getSimilarMedia() != null && mMediaDetail.getSimilarMedia().length > 0) {
            if (similarAdapter == null) {
                similarAdapter = new HorizontalAdapter(getActivity(), HorizontalAdapter.TYPE_SIMILAR);
            }
            similarAdapter.setOnHorizontalItemListener(new HorizontalAdapter.OnHorizontalItemClicked() {
                @Override
                public void onItemClicked(int type, Object data) {
                    if (type == HorizontalAdapter.TYPE_SIMILAR) {
                        Media similarMedia = (Media) data;
                        mListener.onSimilarClicked(similarMedia.getId(), similarMedia.isMovie(), similarMedia.getMediaName());
                    }
                }
            });
            similarAdapter.setSimilarData(mMediaDetail.getSimilarMedia());
            similarRecyclerView.setAdapter(similarAdapter);
        } else {
            similarCardView.setVisibility(View.GONE);
        }

        if (!media.isMovie()) {
            if (seasonAdapter == null) {
                seasonAdapter = new HorizontalAdapter(getActivity(), HorizontalAdapter.TYPE_SEASONS);
            }
            seasonAdapter.setSeasonData(media.getSeasons());
            seasonRecyclerView.setAdapter(seasonAdapter);
        } else {
            seasonCardView.setVisibility(View.GONE);
        }

        favBtn.setImageResource(media.isFavorite() ? R.drawable.ic_action_favorite : R.drawable.ic_action_favorite_outline);

        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media.isFavorite()) {
                    favBtn.setImageResource(R.drawable.ic_action_favorite_outline);
                } else {
                    favBtn.setImageResource(R.drawable.ic_action_favorite);
                }
                media.setIsFavorite(!media.isFavorite());
                new FavoriteTask().execute(media.isFavorite());
                MovieShareApplication.eventBus.post(new FavoriteChangeEvent());
            }
        });

        progressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Media media = mMediaDetail.getMedia();
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_SUBJECT, media.getTitle());
            share.putExtra(Intent.EXTRA_TEXT, media.getHomePage());
            startActivity(Intent.createChooser(share, "Share link!"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class MediaDetailsLoader extends AsyncTaskLoader<MediaDetail> {

        private long mId;
        private boolean mIsMovie;


        public MediaDetailsLoader(Context context, long id, boolean isMovie) {
            super(context);
            mId = id;
            mIsMovie = isMovie;
        }

        @Override
        public MediaDetail loadInBackground() {
            try {
                Media media = ServiceManager.getMediaDetails(mId, mIsMovie);
                ImageResponse imageResponse = ServiceManager.getImage(mId, mIsMovie);
                CastResponse castResponse = ServiceManager.getCast(mId, mIsMovie);
                VideoResponse videoResponse = ServiceManager.getVideo(mId, mIsMovie);
                MediaResponse mediaResponse = ServiceManager.getSimilarMedia(mId, mIsMovie);

                MediaDetail mediaDetail = new MediaDetail();
                if (media != null) {
                    mediaDetail.setMedia(media);
                }
                if (imageResponse != null) {
                    mediaDetail.setBackdropImages(imageResponse.getBackdrops());
                    mediaDetail.setPosterImages(imageResponse.getPosters());
                }

                if (castResponse != null) {
                    mediaDetail.setCast(castResponse.getCast());
                }
                if (videoResponse != null) {
                    mediaDetail.setVideos(videoResponse.getResults());
                }
                if (mediaResponse != null) {
                    mediaDetail.setSimilarMedia(mediaResponse.getResults());
                }

                media.setIsFavorite(MySqlLiteHelper.getInstance(getContext()).isFavorite(media.getId()));

                return mediaDetail;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        MovieShareApplication.eventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class FavoriteTask extends AsyncTask<Boolean, Void, Void> {

        @Override
        protected Void doInBackground(Boolean... params) {
            Media media = mMediaDetail.getMedia();
            if (params[0]) {
                MySqlLiteHelper.getInstance(getContext()).addMedia(media);
            } else {
                MySqlLiteHelper.getInstance(getContext()).removeMedia(media.getId());
            }
            return null;
        }
    }

    public static class FavoriteChangeEvent {

    }
}
