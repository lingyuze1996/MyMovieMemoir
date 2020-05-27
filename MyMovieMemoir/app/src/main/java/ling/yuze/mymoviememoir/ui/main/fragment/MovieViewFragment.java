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


import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.network.SearchMovieDB;
import ling.yuze.mymoviememoir.utility.SentimentAnalysis;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class MovieViewFragment extends Fragment implements View.OnClickListener {
    SentimentAnalysis analyst;

    private int movieId;

    private TextView tvTitle;
    private TextView tvYear;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view, container, false);

        // initialize the bag of positive and negative words
        String[] negativeWords = getWordsList(readFile(R.raw.negative_words));
        String[] positiveWords = getWordsList(readFile(R.raw.negative_words));
        analyst = new SentimentAnalysis(positiveWords, negativeWords);

        // Get movie information from movie search fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);

        movieId = shared.getInt("id", -1);
        String name = shared.getString("name", "Unknown");
        String releaseDate = shared.getString("releaseDate", "Unknown");
        String overview = shared.getString("overview", "Unknown");
        String imagePath = shared.getString("imagePath", "");
        float rating = shared.getFloat("rating", 0);

        // initialize widgets
        tvTitle = v.findViewById(R.id.tv_view_movie_name);
        tvYear = v.findViewById(R.id.tv_view_movie_year);
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

        buttonWatchlist.setOnClickListener(this);
        buttonMemoir.setOnClickListener(this);

        tvTitle.setText(name);
        tvRelease.setText(releaseDate);

        if (!releaseDate.equals("Unknown"))
            tvYear.setText("(" + releaseDate.substring(0, 4) + ")");

        tvOverview.setText(overview);
        setImage(image, imagePath);
        ratingBar.setRating(rating);

        new TaskGetDetails().execute(movieId);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //case
        }
    }

    public String[] getWordsList(String content) {
        String[] wordsList = content.split("\n");
        return wordsList;
    }

    public String readFile(int resourceId) {
        String content = "";
        StringBuffer buffer = new StringBuffer();
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            Scanner scanner = new Scanner(inputStream, "UTF-8");
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine() + "\n");
            }
            content = buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    private class TaskGetDetails extends AsyncTask<Integer, Void, List<List<String>>> {
        @Override
        protected List<List<String>> doInBackground(Integer... integers) {
            ArrayList<List<String>> list = new ArrayList<>();

            SearchMovieDB search = new SearchMovieDB();
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
                directors += director + ",";
            }

            directors = directors.substring(0, directors.length() - 1);
            tvDirector.setText(directors);

            // update country information
            String countries = "";
            for (String country : countryList) {
                countries += country + ",";
            }

            countries = countries.substring(0, countries.length() - 1);
            tvCountry.setText(countries);

            // update genre information
            String genres = "";
            for (String genre : genreList) {
                genres += genre + ",";
            }

            genres = genres.substring(0, genres.length() - 1);
            tvGenre.setText(genres);

            // update cast information
            String casts = "";
            for (String cast : castList) {
                casts += cast + ",";
            }

            casts = casts.substring(0, casts.length() - 1);
            tvCast.setText(casts);
        }
    }
}
