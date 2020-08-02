package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.MovieSearchRecyclerAdapter;
import ling.yuze.mymoviememoir.adapter.OnItemClickListener;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.data.viewModel.MovieViewModel;
import ling.yuze.mymoviememoir.network.SearchMovieDB;

public class MovieSearchFragment extends Fragment {
    private LinearLayout resultHeading;
    private MovieSearchRecyclerAdapter adapter;
    private SearchMovieDB search;
    private Handler handler = new Handler();
    private List<Movie> movies;
    private MovieViewModel movieViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        // initialize movie view model
        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);

        RecyclerView recyclerView = v.findViewById(R.id.movies_search_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        movies = Movie.createMovieList();
        adapter = new MovieSearchRecyclerAdapter(movies);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                // when the movie item is clicked, pass the information to view model and redirect to view screen
                Movie movie = (Movie) item;
                movieViewModel.setMovie(movie);

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
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
                searchMoviesByQuery(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search = new SearchMovieDB();
        search.setAPIKey(getString(R.string.movie_db_api_key));
        return v;
    }

    private void searchMoviesByQuery(final String query) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                movies.clear();
                movies.addAll(search.searchByQuery(query));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        resultHeading.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }
}
