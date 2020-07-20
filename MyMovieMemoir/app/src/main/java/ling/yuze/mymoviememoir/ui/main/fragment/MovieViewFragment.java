package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.viewModel.MovieToWatchViewModel;
import ling.yuze.mymoviememoir.network.SearchMovieDB;
import ling.yuze.mymoviememoir.utility.DateFormat;
import ling.yuze.mymoviememoir.utility.FileIO;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class MovieViewFragment extends Fragment implements View.OnClickListener {

    private MovieToWatchViewModel viewModel;
    private SharedPreferences shared;
    private boolean inWatchlist;
    private int movieId;
    private String name;
    private String releaseDate;

    private TextView tvTitle;
    private ImageView image;
    private RatingBar ratingBar;
    private TextView tvDirector;
    private TextView tvGenre;
    private TextView tvCountry;
    private TextView tvRelease;
    private TextView tvOverview;
    private TextView tvCast;
    private Button buttonWatchlist;
    private Button buttonMemoir;
    private TextView link;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Get movie information from movie search fragment
        shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);
        inWatchlist = shared.getBoolean("added", false);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view, container, false);

        // get basic movie information passed from last screen
        movieId = shared.getInt("id", -1);
        name = shared.getString("name", "Unknown");
        releaseDate = shared.getString("releaseDate", "Unknown");
        String overview = shared.getString("overview", "Unknown");
        String imagePath = shared.getString("imagePath", "");
        float ratingOriginal = shared.getFloat("rating", 0);
        float rating = Math.round(ratingOriginal) / 2.0f;

        // initialize widgets
        tvTitle = v.findViewById(R.id.tv_view_movie_name);
        image = v.findViewById(R.id.image_view_poster);
        ratingBar = v.findViewById(R.id.view_ratingBar);
        tvDirector = v.findViewById(R.id.tv_view_director);
        tvGenre = v.findViewById(R.id.tv_view_genre);
        tvCountry = v.findViewById(R.id.tv_view_country);
        tvRelease = v.findViewById(R.id.tv_view_date);
        tvOverview = v.findViewById(R.id.tv_view_overview);
        tvCast = v.findViewById(R.id.tv_view_cast);
        buttonWatchlist = v.findViewById(R.id.btAddWatchlist);
        buttonMemoir = v.findViewById(R.id.btAddMemoir);

        link = v.findViewById(R.id.tv_here);

        buttonWatchlist.setOnClickListener(this);
        buttonMemoir.setOnClickListener(this);
        link.setOnClickListener(this);

        tvTitle.setText(name);
        tvRelease.setText(releaseDate);

        tvOverview.setText(overview);
        setImage(image, imagePath);
        ratingBar.setRating(rating);

        // retrieve detailed movie information
        new TaskGetDetails().execute(movieId);

        return v;
    }

    @Override
    public void onStart() {
        if (inWatchlist) buttonWatchlist.setVisibility(View.INVISIBLE);
        super.onStart();
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean("added", false);
        editor.apply();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btAddMemoir:
                replaceFragment(new AddMemoirFragment());
                break;

            case R.id.btAddWatchlist:
                viewModel = new ViewModelProvider(getActivity()).get(MovieToWatchViewModel.class);
                viewModel.insert(new MovieToWatch(name, releaseDate, DateFormat.getCurrentDatetime()));
                Toast.makeText(getContext(), R.string.success_add_watchlist, Toast.LENGTH_LONG).show();
                replaceFragment(new WatchlistFragment());

                break;

            case R.id.tv_here:
                replaceFragment(new TweetsFragment());
        }
    }

    private void replaceFragment(Fragment next) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, next);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class TaskGetDetails extends AsyncTask<Integer, Void, List<List<String>>> {
        @Override
        protected List<List<String>> doInBackground(Integer... integers) {
            ArrayList<List<String>> list = new ArrayList<>();

            SearchMovieDB search = new SearchMovieDB();
            search.setAPIKey(FileIO.readFile(getContext(), R.raw.moviedb_api_key));
            List<String> castList = search.searchCast(movieId);
            List<String> countryList = search.searchCountries(movieId);
            List<String> directorList = search.searchDirector(movieId);
            List<String> genreList = search.searchGenres(movieId);
            list.add(directorList);
            list.add(countryList);
            list.add(genreList);
            list.add(castList);

            return list;
        }

        @Override
        protected void onPostExecute(List<List<String>> lists) {
            List<String> directorList = lists.get(0);
            List<String> countryList = lists.get(1);
            List<String> genreList = lists.get(2);
            List<String> castList = lists.get(3);

            // update director information
            String directors = "";
            for (String director : directorList) {
                directors += director + ", ";
            }
            try {
                directors = directors.substring(0, directors.length() - 2);
            } catch (IndexOutOfBoundsException e) {e.printStackTrace();}
            tvDirector.setText(directors);

            // update country information
            String countries = "";
            for (String country : countryList) {
                countries += country + ", ";
            }

            try {
                countries = countries.substring(0, countries.length() - 2);
            } catch (IndexOutOfBoundsException e) {e.printStackTrace();}
            tvCountry.setText(countries);

            // update genre information
            String genres = "";
            for (String genre : genreList) {
                genres += genre + ", ";
            }

            try {
                genres = genres.substring(0, genres.length() - 2);
            } catch (IndexOutOfBoundsException e) {e.printStackTrace();}
            tvGenre.setText(genres);

            // update cast information
            String casts = "";
            for (String cast : castList) {
                casts += cast + ", ";
            }

            try {
                casts = casts.substring(0, casts.length() - 2);
            } catch (IndexOutOfBoundsException e) {e.printStackTrace();}
            tvCast.setText(casts + "...");
        }
    }

}
