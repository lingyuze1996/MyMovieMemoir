package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.viewModel.MovieToWatchViewModel;
import ling.yuze.mymoviememoir.data.viewModel.MovieViewModel;
import ling.yuze.mymoviememoir.network.SearchMovieDB;
import ling.yuze.mymoviememoir.utility.DateFormat;
import ling.yuze.mymoviememoir.utility.ListParser;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class MovieViewFragment extends Fragment implements View.OnClickListener {

    private MovieToWatchViewModel viewModel;
    private MovieViewModel movieViewModel;
    private SearchMovieDB search;

    private Movie movie;

    private TextView tvDirector;
    private TextView tvGenre;
    private TextView tvCountry;
    private TextView tvCast;
    private Button buttonWatchlist;
    private Button buttonMemoir;

    private Handler handler = new Handler();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Get movie information from movie view model
        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        movie = movieViewModel.getMovie().getValue();

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view, container, false);

        // initialize widgets
        TextView tvTitle = v.findViewById(R.id.tv_view_movie_name);
        ImageView image = v.findViewById(R.id.image_view_poster);
        RatingBar ratingBar = v.findViewById(R.id.view_ratingBar);
        TextView tvRelease = v.findViewById(R.id.tv_view_date);
        TextView tvOverview = v.findViewById(R.id.tv_view_overview);
        tvDirector = v.findViewById(R.id.tv_view_director);
        tvGenre = v.findViewById(R.id.tv_view_genre);
        tvCountry = v.findViewById(R.id.tv_view_country);
        tvCast = v.findViewById(R.id.tv_view_cast);
        buttonWatchlist = v.findViewById(R.id.btAddWatchlist);
        buttonMemoir = v.findViewById(R.id.btAddMemoir);

        TextView twitterLink = v.findViewById(R.id.tv_here);

        buttonWatchlist.setOnClickListener(this);
        buttonMemoir.setOnClickListener(this);
        twitterLink.setOnClickListener(this);

        tvTitle.setText(movie.getName());
        tvRelease.setText(movie.getReleaseDate());

        tvOverview.setText(movie.getOverview());
        setImage(image, movie.getImagePath());
        ratingBar.setRating(Math.round(movie.getPublicRating()) / 2.0f);

        // retrieve detailed movie information
        getMovieInformation();

        return v;
    }

    @Override
    public void onStart() {
        if (movie.isInWatchlist()) buttonWatchlist.setVisibility(View.INVISIBLE);
        super.onStart();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btAddMemoir:
                movieViewModel.setMovie(movie);
                replaceFragment(new AddMemoirFragment());
                break;

            case R.id.btAddWatchlist:
                viewModel = new ViewModelProvider(getActivity()).get(MovieToWatchViewModel.class);
                viewModel.insert(new MovieToWatch(movie.getName(), movie.getReleaseDate(), DateFormat.getCurrentDatetime()));
                Toast.makeText(getContext(), R.string.success_add_watchlist, Toast.LENGTH_LONG).show();
                replaceFragment(new WatchlistFragment());

                break;

            case R.id.tv_here:
                replaceFragment(new TweetsFragment());
        }
    }

    private void replaceFragment(Fragment next) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, next);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void getMovieInformation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                search = new SearchMovieDB();
                search.setAPIKey(getString(R.string.movie_db_api_key));
                search.getDetails(movie);
                search.getCredits(movie);

                handler.post(new DisplayMovieInformation());
            }
        }).start();
    }

    private class DisplayMovieInformation implements Runnable {
        @Override
        public void run() {
            // Display directors
            tvDirector.setText(ListParser.listToString(movie.getDirectors()));

            // Display countries
            tvCountry.setText(ListParser.listToString(movie.getCountries()));

            // Display genres
            tvGenre.setText(ListParser.listToString(movie.getGenres()));

            // Display cast
            tvCast.setText(ListParser.listToString(movie.getCast()));
        }
    }
}
