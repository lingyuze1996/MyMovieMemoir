package ling.yuze.mymoviememoir.network;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class SearchTwitter {
    private static final String CONSUMER_KEY = "ms8pi1My78bvSWpwtLs75mdaU";
    private static final String CONSUMER_SECRET = "SMx93bFTVN6xXIFNwir0zbRsTq19zjnvS7IehBI5lyze4G9nnS";
    private static final String ACCESS_TOKEN = "953300632152178688-yDyukO5Mc6FWUneypTekLmM1D3qeGeM";
    private static final String ACCESS_SECRET = "X5mXiAVmAbgGzFXrLvQXYpTHAQpv5UcpMsYsfTxVa92Fx";

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
