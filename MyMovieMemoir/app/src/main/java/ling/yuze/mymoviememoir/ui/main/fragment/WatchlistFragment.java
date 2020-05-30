package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterWatchlist;
import ling.yuze.mymoviememoir.data.MemoirItem;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.data.room.database.MovieToWatchDatabase;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.viewmodel.MovieToWatchViewModel;
import ling.yuze.mymoviememoir.network.SearchMovieDB;

public class WatchlistFragment extends Fragment {
    private ListView listView;
    private MovieToWatchViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_watchlist, container, false);

        listView = v.findViewById(R.id.list_view_watchlist);

        viewModel = new ViewModelProvider(getActivity()).get(MovieToWatchViewModel.class);
        viewModel.initializeVars(requireActivity().getApplication());
        viewModel.getAllMoviesToWatch().observe(this.getActivity(), new Observer<List<MovieToWatch>>() {
            @Override
            public void onChanged(@Nullable final List<MovieToWatch> moviesToWatch)
            {
                listView.setAdapter(new ListAdapterWatchlist(getContext(), R.layout.list_view_watchlist, moviesToWatch));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieToWatch m = (MovieToWatch) listView.getAdapter().getItem(position);
                new TaskGetMovieBasics().execute(m.getMovieName(), m.getReleaseDate());
            }
        });


        return v;
    }

    private class TaskGetMovieBasics extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            SearchMovieDB search = new SearchMovieDB();

            // first find movies with corresponding movie name
            List<Object[]> movieList = search.searchBasics((String) params[0]);
            if (movieList == null) return null;

            // find the movie with corresponding release date
            Object[] theMovie = new Object[6];
            for (Object[] movie : movieList) {
                String date = (String) movie[2];
                if (date.equals((String) params[1])) {
                    theMovie = movie;
                    break;
                }
            }

            String path = (String) theMovie[3];
            String overview = (String) theMovie[4];
            float rating = (float) ((double) theMovie[5]);

            int id = (Integer) theMovie[0];

            SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = shared.edit();
            editor.putInt("id", id);
            editor.putString("name", (String) params[0]);
            editor.putString("releaseDate", (String) params[1]);
            editor.putString("overview", overview);
            editor.putString("imagePath", path);
            editor.putFloat("rating", rating);
            editor.putBoolean("added", true);
            editor.apply();

            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.content_frame, new MovieViewFragment());
            transaction.addToBackStack(null);
            transaction.commit();

            return null;
        }
    }
}
