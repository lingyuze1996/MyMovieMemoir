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
import ling.yuze.mymoviememoir.adapter.ListAdapterTweets;
import ling.yuze.mymoviememoir.data.Tweet;
import ling.yuze.mymoviememoir.network.SearchTwitter;
import ling.yuze.mymoviememoir.utility.SentimentAnalysis;

public class TweetsFragment extends Fragment {
    SentimentAnalysis analyst;

    private TextView tvTweetsHeading;
    private ListView listView;
    private ListAdapterTweets adapter;
    private List<Tweet> tweets;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets, container, false);

        // Get movie information from movie search fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);
        String name = shared.getString("name", "Unknown");

        // initialize the bag of positive and negative words
        String[] negativeWords = getWordsList(readFile(R.raw.negative_words));
        String[] positiveWords = getWordsList(readFile(R.raw.positive_words));
        analyst = new SentimentAnalysis(positiveWords, negativeWords);

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
}
