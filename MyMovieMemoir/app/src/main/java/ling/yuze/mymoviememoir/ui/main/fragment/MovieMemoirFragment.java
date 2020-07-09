package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.PathDashPathEffect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterMemoir;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.MemoirItem;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.network.SearchMovieDB;
import ling.yuze.mymoviememoir.utility.FileIO;

import static ling.yuze.mymoviememoir.utility.DateFormat.compareDate;

public class MovieMemoirFragment extends Fragment {
    private List<MemoirItem> memoirs;
    private List<MemoirItem> memoirsAll;
    private ListView listView;
    private ListAdapterMemoir adapter;
    private Spinner filter;
    private ArrayAdapter<String> genreAdapter;
    private HashSet<String> genreSet = new HashSet<>();
    private Spinner sort;
    private int personId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_memoir, container, false);

        SharedPreferences shared = getContext().getSharedPreferences("Info", Context.MODE_PRIVATE);
        personId = shared.getInt("id", 0);

        filter = v.findViewById(R.id.spinner_filter);

        filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = filter.getSelectedItem().toString();

                new TaskFilterGenre().execute(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        sort = v.findViewById(R.id.spinner_sort);

        sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = sort.getSelectedItem().toString();
                // sort the list based on different options and notify the adapter to refresh
                switch (selected) {
                    case "my rating ↓":
                        sortMyRatingDescending();
                        adapter.notifyDataSetChanged();
                        break;
                    case "my rating ↑":
                        sortMyRatingAscending();
                        adapter.notifyDataSetChanged();
                        break;
                    case "public rating ↓":
                        sortPublicRatingDescending();
                        adapter.notifyDataSetChanged();
                        break;
                    case "public rating ↑":
                        sortPublicRatingAscending();
                        adapter.notifyDataSetChanged();
                        break;
                    case "watching date ↓":
                        sortWatchingDateDescending();
                        adapter.notifyDataSetChanged();
                        break;
                    case "watching date ↑":
                        sortWatchingDateAscending();
                        adapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        listView = v.findViewById(R.id.list_view_memoir);
        memoirs = MemoirItem.createList(); // store memoir items for display in the list
        memoirsAll = MemoirItem.createList(); // store all memoir items
        adapter = new ListAdapterMemoir(getContext(), R.layout.list_view_memoir, memoirs);

        listView.setAdapter(adapter);

        //new TaskGetAllMemoirs().execute(); // Some problems here

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // redirect to view screen when memoir item is clicked
                MemoirItem memoir = memoirs.get(position);
                SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("id", memoir.getMovie().getId());
                editor.putString("name", memoir.getMovie().getName());
                editor.putString("releaseDate", memoir.getMovie().getReleaseDate());
                editor.putString("overview", memoir.getMovie().getOverview());
                editor.putString("imagePath", memoir.getMovie().getImagePath());
                editor.putFloat("rating", memoir.getMovie().getRating());
                editor.apply();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.content_frame, new MovieViewFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;
    }

    private void sortMyRatingDescending() {
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
                return Float.compare(o1.getMovie().getRating(), o2.getMovie().getRating()) * (-1);
            }
        });
    }

    private void sortPublicRatingAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMovie().getRating(), o2.getMovie().getRating());
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
            adapter = new ListAdapterMemoir(getContext(), R.layout.list_view_memoir, memoirs);
            listView.setAdapter(adapter);
        }

    }

    private class TaskGetAllMemoirs extends AsyncTask<Void, Void, List<Object[]>> {
        @Override
        protected List<Object[]> doInBackground(Void... voids) {
            RestService rs = new RestService();
            return rs.getAllMemoirsByPerson(personId);
        }

        @Override
        protected void onPostExecute(List<Object[]> memoirsList) {
            for (Object[] memoir : memoirsList) {
                String name = (String) memoir[0];
                String release = (String) memoir[1];
                String watching = (String) memoir[2];
                String comment = (String) memoir[3];
                String suburb = (String) memoir[4];
                float rating = (float) ((double) memoir[5]);

                MemoirItem item = new MemoirItem(name, release, watching, suburb, comment, rating);

                new TaskGetMovieDetails().execute(name, release, item);

                memoirs.add(item);
                memoirsAll.add(item);
            }

            adapter.notifyDataSetChanged();
        }
    }

    private class TaskGetMovieDetails extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            SearchMovieDB search = new SearchMovieDB();
            search.setAPIKey(FileIO.readFile(getContext(), R.raw.moviedb_api_key));

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
}
