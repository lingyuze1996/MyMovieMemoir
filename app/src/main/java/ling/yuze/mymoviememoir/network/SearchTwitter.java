package ling.yuze.mymoviememoir.network;

import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.ui.main.fragment.TweetsFragment;
import ling.yuze.mymoviememoir.utility.FileIO;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SearchTwitter {
    private static String CONSUMER_KEY;
    private static String CONSUMER_SECRET;
    private static String ACCESS_TOKEN;
    private static String ACCESS_SECRET;

    public static void setAPIKeys(String[] keys) {
        CONSUMER_KEY = keys[0];
        CONSUMER_SECRET = keys[1];
        ACCESS_TOKEN = keys[2];
        ACCESS_SECRET = keys[3];
    }

    public static List<String> search(String keyword) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey(CONSUMER_KEY)
                .setOAuthConsumerSecret(CONSUMER_SECRET)
                .setOAuthAccessToken(ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_SECRET);

        List<String> tweetList = new ArrayList<>();

        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        Twitter twitter = twitterFactory.getInstance();
        Query query = new Query(keyword);
        try {
            QueryResult result = twitter.search(query);
            for (Status status : result.getTweets()) {
                boolean duplicate = false;
                for (String tweet : tweetList) {
                    if (status.getText().equals(tweet)) {
                        duplicate = true;
                        break;
                    }
                }

                // avoid duplicate tweets
                if (!duplicate)
                    tweetList.add(status.getText());
                if (tweetList.size() >= 10) break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return tweetList;
        }

    }


}
