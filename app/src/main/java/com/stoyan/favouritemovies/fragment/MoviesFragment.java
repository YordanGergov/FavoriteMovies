package com.stoyan.favouritemovies.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.stoyan.favouritemovies.R;
import com.stoyan.favouritemovies.Utils;
import com.stoyan.favouritemovies.adapter.MoviesAdapter;
import com.stoyan.favouritemovies.task.FetchMoviesTask;


/**
 *
 */
public class MoviesFragment extends Fragment {

    private static final String MOVIES_PARCELABLE_KEY = "movies";

    public static final String POPULARITY_DESC = "popularity.desc";
    public static final String VOTE_AVERAGE_DESC = "vote_average.desc";
    public static final String SORT = "sort";

    private MoviesAdapter mMoviesAdapter;
    private GridView mGridView;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMoviesAdapter = new MoviesAdapter(getActivity(),null);

        if(savedInstanceState == null || !savedInstanceState.containsKey(MOVIES_PARCELABLE_KEY)){

            Bundle bundle = this.getArguments();
            String sortBy = bundle.getString(SORT);

            fetchMovies(sortBy);
        }
        else {
            mMoviesAdapter.mData = savedInstanceState.getParcelableArrayList(MOVIES_PARCELABLE_KEY);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(MOVIES_PARCELABLE_KEY, mMoviesAdapter.mData);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        mGridView = (GridView) rootView.findViewById(R.id.grid_view_movies);

        mGridView.setAdapter(mMoviesAdapter);

        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String extra = mMoviesAdapter.mData.get(position).dataString;

                ((Callback) getActivity())
                        .onItemLongClick(extra);
                return true;
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String extra = mMoviesAdapter.mData.get(position).dataString;

                ((Callback) getActivity())
                        .onItemSelected(extra);
            }
        });


        return rootView;
    }

    public void onSortChanged(String sortBy) {

        Log.v("FRAG", "SORT CHANGED " + sortBy);

        if(fetchMovies(sortBy)){
            mMoviesAdapter.notifyDataSetChanged();
            mGridView.smoothScrollToPosition(0);
        }

    }

    private boolean fetchMovies(String sortBy) {

        if(getActivity() == null) return false;

        if(!Utils.isNetworkAvailable(getActivity())){

            String text = "No Internet Connectivity";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(getActivity(),text, duration);
            toast.show();
            return false;
        }

        // Network is present
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask(getActivity(), mMoviesAdapter);
        fetchMoviesTask.execute(sortBy);
        return true;
    }

    public interface Callback {
        void handleShakeEvent(int count);


        public void onItemSelected(String movieData);

        public void onItemLongClick(String movieData);
    }
}
