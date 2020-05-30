package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.MemoirItem;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class ListAdapterWatchlist extends ArrayAdapter<MovieToWatch> {
    private int resourceId;


    public ListAdapterWatchlist(Context context, int resource, List<MovieToWatch> movies) {
        super(context, resource, movies);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        MovieToWatch movie = getItem(position);
        View v = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvMovieName = v.findViewById(R.id.tv_ls_watchlist_movie_name);
        TextView tvRelease = v.findViewById(R.id.tv_ls_watchlist_release);
        TextView tvAdded = v.findViewById(R.id.tv_ls_watchlist_added);

        tvMovieName.setText(movie.getMovieName());
        tvRelease.setText(movie.getReleaseDate());
        tvAdded.setText(movie.getTimeAdded());

        return v;
    }


}
