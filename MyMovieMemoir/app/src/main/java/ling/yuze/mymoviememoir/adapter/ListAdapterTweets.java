package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Tweet;

public class ListAdapterTweets extends ArrayAdapter<Tweet> {
    private int resourceId;


    public ListAdapterTweets(Context context, int resource, List<Tweet> tweets) {
        super(context, resource, tweets);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Tweet tweet = getItem(position);
        View v = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvContent = v.findViewById(R.id.tv_tweet);
        ImageView imageSentiment = v.findViewById(R.id.image_tweet_sentiment);

        tvContent.setText(tweet.getContent());

        imageSentiment.setImageResource(
                getResourceIdFromSentiment(
                        tweet.getSentiment()));

        return v;
    }

    private int getResourceIdFromSentiment(int sentiment) {
        int resourceId = R.drawable.ic_sentiment_neutral;
        switch (sentiment) {
            case 1:
                resourceId = R.drawable.ic_sentiment_positive;
                break;
            case -1:
                resourceId = R.drawable.ic_sentiment_negative;
                break;
        }
        return resourceId;
    }

}
