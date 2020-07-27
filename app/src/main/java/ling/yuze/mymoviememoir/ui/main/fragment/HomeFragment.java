package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.HomeRecyclerAdapter;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.network.SearchMovieDB;

public class HomeFragment extends Fragment {

    private int id;
    private TextView tvDate;
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter adapter;
    private List<Movie> movieList;
    private Handler handler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        tvDate = v.findViewById(R.id.tv_home_date);
        Calendar c = Calendar.getInstance();
        String currentDate =
                c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(currentDate); // display current date

        recyclerView = v.findViewById(R.id.home_recycler);
        movieList = Movie.createMovieList();
        adapter = new HomeRecyclerAdapter(movieList);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // retrieve trending movies
        retrieveTrendingMovies();

        return v;
    }

    private void retrieveTrendingMovies() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SearchMovieDB search = new SearchMovieDB();
                search.setAPIKey(getString(R.string.movie_db_api_key));
                final List<Movie> movies = search.searchPopular();
                if (movies != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            movieList.addAll(movies);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

}
