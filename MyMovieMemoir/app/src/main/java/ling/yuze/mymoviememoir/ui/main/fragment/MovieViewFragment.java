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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterTweets;
import ling.yuze.mymoviememoir.data.Tweet;
import ling.yuze.mymoviememoir.network.SearchMovieDB;
import ling.yuze.mymoviememoir.network.SearchTwitter;
import ling.yuze.mymoviememoir.utility.SentimentAnalysis;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class MovieViewFragment extends Fragment implements View.OnClickListener {
    SentimentAnalysis analyst;

    private int movieId;

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

    private TextView tvTweetsHeading;
    private ListView listView;
    private ListAdapterTweets adapter;
    private List<Tweet> tweets;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view, container, false);

        // initialize the bag of positive and negative words
        String[] negativeWords = getWordsList(readFile(R.raw.negative_words));
        String[] positiveWords = getWordsList(readFile(R.raw.positive_words));
        analyst = new SentimentAnalysis(positiveWords, negativeWords);

        // Get movie information from movie search fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);

        movieId = shared.getInt("id", -1);
        String name = shared.getString("name", "Unknown");
        String releaseDate = shared.getString("releaseDate", "Unknown");
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

        buttonWatchlist.setOnClickListener(this);
        buttonMemoir.setOnClickListener(this);

        tvTitle.setText(name);
        tvRelease.setText(releaseDate);

        tvOverview.setText(overview);
        setImage(image, imagePath);
        ratingBar.setRating(rating);

        new TaskGetDetails().execute(movieId);

        // Heading for tweets display
        tvTweetsHeading = v.findViewById(R.id.tv_tweets_heading);
        tvTweetsHeading.setVisibility(View.INVISIBLE);

        listView = v.findViewById(R.id.view_tweets_list);
        tweets = Tweet.createTweetList();
        adapter = new ListAdapterTweets(getContext(), R.layout.list_view_tweets, tweets);
        listView.setAdapter(adapter);

        new TaskGetTweets().execute(name);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btAddMemoir:
                replaceFragment(new AddMemoirFragment());
                break;
            case R.id.btAddWatchlist:
                replaceFragment(new WatchlistFragment());
                break;
        }
    }

    private void replaceFragment(Fragment next) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, next);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String[] getWordsList(String content) {
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

    private class TaskGetTweets extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            String keywords = strings[0] + " movie";
            List<String> tweetsList = SearchTwitter.search(keywords);
            return tweetsList;
        }

        @Override
        protected void onPostExecute(List<String> tweetsResult) {
            for (String tweetContent : tweetsResult) {
                Tweet tweet = new Tweet(tweetContent, analyst.analyze(tweetContent));
                tweets.add(tweet);
            }

            tvTweetsHeading.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
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
