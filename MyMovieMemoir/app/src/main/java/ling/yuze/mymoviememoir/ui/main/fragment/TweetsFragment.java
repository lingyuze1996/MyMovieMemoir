package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterTweet;
import ling.yuze.mymoviememoir.data.Tweet;
import ling.yuze.mymoviememoir.network.SearchTwitter;
import ling.yuze.mymoviememoir.utility.FileIO;
import ling.yuze.mymoviememoir.utility.SentimentAnalysis;

public class TweetsFragment extends Fragment {
    SentimentAnalysis analyst;

    private TextView tvTweetsHeading;
    private ListView listView;
    private ListAdapterTweet adapter;
    private List<Tweet> tweets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets, container, false);

        // Get movie information from movie search fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);
        String name = shared.getString("name", "Unknown");

        // initialize the bag of positive and negative words
        String[] negativeWords = FileIO.readFile(this.getContext(), R.raw.negative_words).split("\n");
        String[] positiveWords = FileIO.readFile(this.getContext(), R.raw.positive_words).split("\n");
        analyst = new SentimentAnalysis(positiveWords, negativeWords);

        // Heading for tweets display
        tvTweetsHeading = v.findViewById(R.id.tv_tweets_heading);
        tvTweetsHeading.setVisibility(View.INVISIBLE);

        listView = v.findViewById(R.id.view_tweets_list);
        tweets = Tweet.createTweetList();
        adapter = new ListAdapterTweet(getContext(), R.layout.list_view_tweets, tweets);
        listView.setAdapter(adapter);

        // retrieve tweets according to movie name
        new TaskGetTweets().execute(name);

        return v;
    }


    private class TaskGetTweets extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            String keywords = strings[0] + " movie";
//            CONSUMER_KEY = keys[0];
//            CONSUMER_SECRET = keys[1];
//            ACCESS_TOKEN = keys[2];
//            ACCESS_SECRET = keys[3];
            String key = getString(R.string.twitter_api_key);
            String apiSecret = getString(R.string.twitter_api_secret_key);
            String token = getString(R.string.twitter_access_token);
            String tokenSecret = getString(R.string.twitter_access_token_secret);
            String[] twitterKeys = {key, apiSecret, token, tokenSecret};
            SearchTwitter.setAPIKeys(twitterKeys);
            List<String> tweetsList = SearchTwitter.search(keywords);
            return tweetsList;
        }

        @Override
        protected void onPostExecute(List<String> tweetsResult) {
            for (String tweetContent : tweetsResult) {

                // perform sentiment analysis for each tweet
                Tweet tweet = new Tweet(tweetContent, analyst.analyze(tweetContent));
                tweets.add(tweet);
            }

            tvTweetsHeading.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
    }
}
