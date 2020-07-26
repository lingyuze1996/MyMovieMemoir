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
import ling.yuze.mymoviememoir.data.Movie;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class ListAdapterSearch extends ArrayAdapter<Movie> {
    private int resourceId;


    public ListAdapterSearch (Context context, int resource, List<Movie> movies) {
        super(context, resource, movies);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);
        View v = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView tvMovieName = v.findViewById(R.id.tv_ls_search_movie_name);
        TextView tvReleaseYear = v.findViewById(R.id.tv_ls_search_release_year);
        ImageView imageMovie = v.findViewById(R.id.ls_search_image);

        tvMovieName.setText(movie.getName());
        String date = movie.getReleaseDate();
        if (date != null && !date.equals(""))
            tvReleaseYear.setText(date.substring(0, 4));
        else
            tvReleaseYear.setText(R.string.unknown);

        setImage(imageMovie, movie.getImagePath());

        return v;
    }


}
