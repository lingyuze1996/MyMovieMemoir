package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.network.ImageDownload;

public class MovieSearchRecyclerAdapter extends RecyclerView.Adapter<MovieSearchRecyclerAdapter.MovieViewHolder> {

    private OnItemClickListener itemClickListener;
    private List<Movie> movies;

    public MovieSearchRecyclerAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cinemasView = inflater.inflate(R.layout.recycler_item_search, parent, false);
        return new MovieViewHolder(cinemasView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, final int position) {
        final Movie movie = movies.get(position);
        holder.tvName.setText(movie.getName());
        holder.tvReleaseYear.setText(movie.getReleaseYear());
        ImageDownload.setImage(holder.imagePoster, movie.getImagePath());

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

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private TextView tvReleaseYear;
        private ImageView imagePoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_rec_movie_search_name);
            tvReleaseYear = itemView.findViewById(R.id.tv_rec_movie_search_release_year);
            imagePoster = itemView.findViewById(R.id.image_rec_movie_search_poster);
        }
    }
}
