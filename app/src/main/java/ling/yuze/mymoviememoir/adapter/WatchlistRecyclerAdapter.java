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
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;

public class WatchlistRecyclerAdapter extends RecyclerView.Adapter<WatchlistRecyclerAdapter.MovieToWatchViewHolder> {

    private OnItemClickListener itemClickListener;
    private List<MovieToWatch> movies;

    public WatchlistRecyclerAdapter(List<MovieToWatch> movies) {
        this.movies = movies;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MovieToWatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View moviesView = inflater.inflate(R.layout.recycler_item_watchlist, parent, false);
        return new MovieToWatchViewHolder(moviesView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieToWatchViewHolder holder, final int position) {
        final MovieToWatch movie = movies.get(position);

        holder.tvMovieName.setText(movie.getMovieName());

        holder.tvMovieRelease.setText(movie.getReleaseDate());

        holder.tvAddedTime.setText(movie.getTimeAdded());

        final int currentPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && currentPosition != RecyclerView.NO_POSITION) {
                    // Perform event defined by the fragment/activity
                    itemClickListener.onItemClick(movies.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieToWatchViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieName;
        private TextView tvMovieRelease;
        private TextView tvAddedTime;

        public MovieToWatchViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tv_rec_watchlist_movie_name);
            tvMovieRelease = itemView.findViewById(R.id.tv_rec_watchlist_release);
            tvAddedTime = itemView.findViewById(R.id.tv_rec_watchlist_added_time);
        }
    }
}
