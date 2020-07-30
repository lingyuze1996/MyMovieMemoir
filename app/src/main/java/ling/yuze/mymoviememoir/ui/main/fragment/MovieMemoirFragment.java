package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterMemoir;
import ling.yuze.mymoviememoir.adapter.MemoirRecyclerAdapter;
import ling.yuze.mymoviememoir.adapter.OnItemClickListener;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.viewModel.MovieViewModel;

import static ling.yuze.mymoviememoir.utility.DateFormat.compareDate;

public class MovieMemoirFragment extends Fragment {
    private List<Memoir> memoirs;
    private List<Memoir> memoirsAll;
    private RecyclerView recyclerView;
    private MemoirRecyclerAdapter recyclerAdapter;
    private MovieViewModel movieViewModel;
    private Spinner spinnerGenreFilter;
    private ArrayAdapter<String> genreAdapter;
    private HashSet<String> genreSet = new HashSet<>();
    private Spinner spinnerSort;

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater.inflate(R.menu.nav_menu, menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_memoir, container, false);

        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);

        /*
        spinnerGenreFilter = v.findViewById(R.id.spinner_genre_filter);

        spinnerGenreFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerGenreFilter.getSelectedItem().toString();

                new TaskFilterGenre().execute(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });*/

        spinnerSort = v.findViewById(R.id.spinner_memoir_sort);

        /*
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerSort.getSelectedItem().toString();
                // sort the list based on different options and notify the adapter to refresh
                switch (selected) {
                    case "my rating ↓":
                        sortMyRatingDescending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "my rating ↑":
                        sortMyRatingAscending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "public rating ↓":
                        sortPublicRatingDescending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "public rating ↑":
                        sortPublicRatingAscending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "watching date ↓":
                        sortWatchingDateDescending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "watching date ↑":
                        sortWatchingDateAscending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

         */


        recyclerView = v.findViewById(R.id.memoirs_recycler);
        memoirs = new ArrayList<>(); // store memoir items for display
        memoirsAll = new ArrayList<>(); // store all memoir items
        recyclerAdapter = new MemoirRecyclerAdapter(memoirs);

        recyclerView.setAdapter(recyclerAdapter);

        //new TaskGetAllMemoirs().execute(); // Some problems here

        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                final Memoir memoir = (Memoir) item;
                movieViewModel.setMovie(memoir.getMovie());

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new MovieViewFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;
    }

    /*private void sortMyRatingDescending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMyRating(), o2.getMyRating()) * (-1);
            }
        });
    }

    private void sortMyRatingAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMyRating(), o2.getMyRating());
            }
        });
    }

    private void sortPublicRatingDescending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating()) * (-1);
            }
        });
    }

    private void sortPublicRatingAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating());
            }
        });
    }

    private void sortWatchingDateAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return compareDate(o1.getWatching(), o2.getWatching());
            }
        });
    }

    private void sortWatchingDateDescending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return compareDate(o1.getWatching(), o2.getWatching()) * (-1);
            }
        });
    }

    private class TaskFilterGenre extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String selected = strings[0];
            if (selected.equals("All")) {
                memoirs = memoirsAll;
                return null;
            }

            List<MemoirItem> filteredMemoirs = new ArrayList<>();
            for (MemoirItem memoir : memoirsAll) {
                if (memoir.getMovie().getGenres().contains(selected)) {
                    filteredMemoirs.add(memoir);
                }
            }
            memoirs = filteredMemoirs;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ListAdapterMemoir(getContext(), R.layout.recycler_item_memoir, memoirs);
            listView.setAdapter(adapter);
        }

    }



    private class TaskGetMovieDetails extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            SearchMovieDB search = new SearchMovieDB();
            search.setAPIKey(getString(R.string.movie_db_api_key));

            // first find movies with corresponding movie name
            List<Object[]> movieList = search.searchByQuery((String) params[0]);
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
            List<String> genres = search.searchGenres(id);
            genreSet.addAll(genres);

            MemoirItem memoir = (MemoirItem) params[2];

            Movie movie = memoir.getMovie();
            movie.setId(id);
            movie.setImagePath(path);
            movie.setGenres(genres);
            movie.setRating(rating);
            movie.setOverview(overview);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sortWatchingDateDescending();
            adapter.notifyDataSetChanged();

            List<String> genres = new ArrayList<>();
            genres.add("All");
            genres.addAll(genreSet);

            genreAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, genres);
            filter.setAdapter(genreAdapter);
        }
    }

     */
}
