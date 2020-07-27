package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Movie;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMovieName;
        public TextView tvReleaseDate;
        public TextView tvRating;

        public ViewHolder(View item) {
            super(item);

            tvMovieName = item.findViewById(R.id.tv_rec_home_movie_name);
            tvReleaseDate = item.findViewById(R.id.tv_rec_home_release_date);
            tvRating = item.findViewById(R.id.tv_rec_home_rating);
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    private List<Movie> movies;
    public HomeRecyclerAdapter(List<Movie> mMovies) {
        movies = mMovies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View moviesView = inflater.inflate(R.layout.recycler_home, parent, false);
        ViewHolder viewHolder = new ViewHolder(moviesView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        holder.tvMovieName.setText(movie.getName());
        holder.tvReleaseDate.setText(movie.getReleaseDate());
        holder.tvRating.setText("" + movie.getRating());

    }
}
