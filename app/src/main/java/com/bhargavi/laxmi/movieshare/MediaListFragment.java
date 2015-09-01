package com.bhargavi.laxmi.movieshare;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bhargavi.laxmi.movieshare.adapter.MediaAdapter;
import com.bhargavi.laxmi.movieshare.service.MySqlLiteHelper;
import com.bhargavi.laxmi.movieshare.service.ServiceManager;
import com.bhargavi.laxmi.movieshare.service.data.Media;
import com.bhargavi.laxmi.movieshare.service.data.MediaResponse;
import com.bhargavi.laxmi.movieshare.widget.DividerItemDecoration;
import com.bhargavi.laxmi.movieshare.widget.EmptyRecyclerView;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class MediaListFragment extends Fragment implements LoaderManager.LoaderCallbacks<MediaResponse> {
    public static final int TYPE_NOWPLAYING_MOVIES = 1;
    public static final int TYPE_POPULAR_MOVIES = 2;
    public static final int TYPE_TOPRATED_MOVIES = 3;
    public static final int TYPE_NOWPLAYING_TVSHOWS = 4;
    public static final int TYPE_POPULAR_TVSHOWS = 5;
    public static final int TYPE_TOPRATED_TVSHOWS = 6;
    public static final int TYPE_USERSHARED_MOVIES = 7;
    public static final int TYPE_USERSHARED_TV_SHOWS = 8;
    private static final String EXTRA_TYPE = "Type";
    private static final String EXTRA_POSITION = "position";
    private static final String EXTRA_QUERY = "Query";
    private static final String EXTRA_MEDIA = "media";
    private static final String EXTRA_PAGE = "page";
    private static final String EXTRA_TOTALPAGES = "Total Pages";
    private ProgressBar progressBar;
    private EmptyRecyclerView recyclerView;

    private int type;
    private String query;
    private ArrayList<Media> media;
    private int position;
    private int currentPage = 1;
    private int totalPages;

    private MediaAdapter mediaAdapter;

    public static MediaListFragment newInstance(int type, int position, String query) {
        MediaListFragment fragment = new MediaListFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE, type);
        bundle.putInt(EXTRA_POSITION, position);
        bundle.putString(EXTRA_QUERY, query);
        fragment.setArguments(bundle);

        return fragment;
    }


    public MediaListFragment() {
        // Required empty public constructor

    }

    @Subscribe
    public void onFavoriteChanged(MediaDetailFragment.FavoriteChangeEvent event) {
        if(this.type != TYPE_USERSHARED_MOVIES || this.type != TYPE_USERSHARED_TV_SHOWS){
            return;
        }
        this.media = null;
        mediaAdapter = null;
        recyclerView.setAdapter(null);
        loadData(currentPage);
    }

    @Subscribe
    public void update(HomeActivity.TabChangedEvent event) {
        if (event.position != position || type == event.type) {
            return;
        }
        this.type = event.type;
        //this.query = query;
        this.media = null;
        currentPage = 1;
        mediaAdapter = null;
        recyclerView.setAdapter(null);
        loadData(currentPage);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(EXTRA_TYPE, type);
        outState.putInt(EXTRA_POSITION, position);
        outState.putString(EXTRA_QUERY, query);
        outState.putParcelableArrayList(EXTRA_MEDIA, media);
        outState.putInt(EXTRA_PAGE, currentPage);
        outState.putInt(EXTRA_TOTALPAGES, totalPages);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_media_list, container, false);
        MovieShareApplication.eventBus.register(this);

        progressBar = (ProgressBar) view.findViewById(R.id.progrss_bar);
        recyclerView = (EmptyRecyclerView) view.findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        recyclerView.setEmptyView(view.findViewById(R.id.empty_view));


        Bundle bundle;
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
            media = bundle.getParcelableArrayList(EXTRA_MEDIA);
            currentPage = bundle.getInt(EXTRA_PAGE);
            totalPages = bundle.getInt(EXTRA_TOTALPAGES);
        } else {
            bundle = getArguments();
        }
        position = bundle.getInt(EXTRA_POSITION);
        type = bundle.getInt(EXTRA_TYPE);
        query = bundle.getString(EXTRA_QUERY);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (media == null) {
            loadData(currentPage);
        } else {
            populateView();
        }
    }

    private void loadData(int page) {
        if (!isAdded()) {
            return;
        }
        Bundle bundle = new Bundle(1);
        bundle.putInt(EXTRA_PAGE, page);
        getLoaderManager().restartLoader(this.type, bundle, this);
        if (page == 1) {
            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public Loader<MediaResponse> onCreateLoader(int id, Bundle args) {
        return new MediaLoader(getActivity(), type, query, args.getInt(EXTRA_PAGE));
    }

    @Override
    public void onLoadFinished(Loader<MediaResponse> loader, MediaResponse data) {
        getLoaderManager().destroyLoader(this.type);
        ArrayList<Media> results = null;
        if (data != null && data.getResults() != null) {
            results = new ArrayList<>(Arrays.asList(data.getResults()));
            if (media == null) {
                media = results;
            } else {
                media.addAll(results);
            }
            currentPage = data.getPage();
            totalPages = data.getTotalPages();
        }

        populateView();
    }

    private void populateView() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        if (mediaAdapter == null) {
            mediaAdapter = new MediaAdapter(media, getActivity(), currentPage, totalPages);
            recyclerView.setAdapter(mediaAdapter);
        } else {
            mediaAdapter.notifyPageChange(currentPage);
        }
        mediaAdapter.setListner(new MediaAdapter.MediaAdapterInterface() {
            @Override
            public void loadNextPage(int page) {
                loadData(page);
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<MediaResponse> loader) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MovieShareApplication.eventBus.unregister(this);
    }

    public static class MediaLoader extends AsyncTaskLoader<MediaResponse> {
        private int type;
        private String query;
        private int page;

        public MediaLoader(Context context, int type, String query, int page) {
            super(context);
            this.type = type;
            this.query = query;
            this.page = page;
        }

        @Override
        public MediaResponse loadInBackground() {
            try {
                if (type == TYPE_USERSHARED_MOVIES) {
                    return MySqlLiteHelper.getInstance(getContext()).getMediaResponse(true);
                } else if (type == TYPE_USERSHARED_TV_SHOWS) {
                    return MySqlLiteHelper.getInstance(getContext()).getMediaResponse(false);
                } else {
                    return ServiceManager.getMediaList(type, page);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }
    }


}
