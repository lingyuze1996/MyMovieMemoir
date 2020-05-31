package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterSearch;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.network.SearchMovieDB;

public class MovieSearchFragment extends Fragment {
    private LinearLayout resultHeading;
    private ListView listView;
    private ListAdapterSearch adapter;
    private List<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        listView = v.findViewById(R.id.list_view_search);

        movies = Movie.createMovieList();
        adapter = new ListAdapterSearch(getContext(), R.layout.list_view_search, movies);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when the movie item is clicked, share the information and redirect to view screen
                Movie movie = movies.get(position);
                SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("id", movie.getId());
                editor.putString("name", movie.getName());
                editor.putString("releaseDate", movie.getReleaseDate());
                editor.putString("overview", movie.getOverview());
                editor.putString("imagePath", movie.getImagePath());
                editor.putFloat("rating", movie.getRating());
                editor.apply();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.content_frame, new MovieViewFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        //Heading for search results
        resultHeading = v.findViewById(R.id.search_outcome_header);
        resultHeading.setVisibility(View.INVISIBLE);

        //Search Bar
        SearchView searchView = v.findViewById(R.id.movie_search_view);
        searchView.setSubmitButtonEnabled(true);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new TaskSearchMovie().execute(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return v;
    }

    private class TaskSearchMovie extends AsyncTask<String, Void, List<Object[]>> {
        @Override
        protected List<Object[]> doInBackground(String... strings) {
            SearchMovieDB search = new SearchMovieDB();
            List<Object[]> basics = search.searchBasics(strings[0]);
            return basics;
        }

        @Override
        protected void onPostExecute(List<Object[]> list) {
            if (list == null) return;

            for (Object[] object : list) {
                int id = (Integer) object[0];
                String name = (String) object[1];
                String date = (String) object[2];
                String path = (String) object[3];
                String overview = (String) object[4];
                double rating = (Double) object[5];
                Movie newMovie = new Movie(id, name, date, path, overview, (float) rating);
                movies.add(newMovie);
            }

            resultHeading.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}
