package ling.yuze.mymoviememoir.data;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    private String content;
    private int sentiment;

    public Tweet(String newContent, int newSentiment) {
        content = newContent;
        sentiment = newSentiment;
    }

    public int getSentiment() { return sentiment; }
    public String getContent() { return content; }

    public static List<Tweet> createTweetList() {
        List<Tweet> tweetList = new ArrayList<>();
        return tweetList;
    }
}
