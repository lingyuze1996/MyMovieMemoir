package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.network.ImageDownload;
import ling.yuze.mymoviememoir.utility.DateFormat;

public class MemoirRecyclerAdapter extends RecyclerView.Adapter<MemoirRecyclerAdapter.MemoirViewHolder> {

    private OnItemClickListener itemClickListener;
    private List<Memoir> memoirs;

    public MemoirRecyclerAdapter(List<Memoir> memoirs) {
        this.memoirs = memoirs;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MemoirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View memoirView = inflater.inflate(R.layout.recycler_item_memoir, parent, false);
        return new MemoirViewHolder(memoirView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoirViewHolder holder, final int position) {
        final Memoir memoir = memoirs.get(position);

        holder.tvMovieName.setText(memoir.getMovie().getName());

        String movieReleaseDate = "(" + memoir.getMovie().getReleaseDate() + ")";
        holder.tvMovieRelease.setText(movieReleaseDate);

        ImageDownload.setImage(holder.imagePoster, memoir.getMovie().getImagePath());

        String watchDate = "Watched on: " + DateFormat.timestampToDate(memoir.getMemoirTime());
        holder.tvMemoirDate.setText(watchDate);

        String watchCinema = "At: " + memoir.getCinema().getName();
        holder.tvMemoirCinema.setText(watchCinema);

        holder.tvComment.setText(memoir.getComment());

        holder.ratingMy.setRating(memoir.getMemoirRating());

        holder.ratingPublic.setRating(Math.round(memoir.getMovie().getPublicRating()) / 2.0f);

        final int currentPosition = holder.getAdapterPosition();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && currentPosition != RecyclerView.NO_POSITION) {
                    // Perform event defined by the fragment/activity
                    itemClickListener.onItemClick(memoirs.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return memoirs.size();
    }

    class MemoirViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMovieName;
        private TextView tvMovieRelease;
        private TextView tvMemoirDate;
        private TextView tvMemoirCinema;
        private TextView tvComment;
        private RatingBar ratingPublic;
        private RatingBar ratingMy;
        private ImageView imagePoster;

        public MemoirViewHolder(View itemView) {
            super(itemView);
            tvMovieName = itemView.findViewById(R.id.tv_rec_memoir_movie_name);
            tvMovieRelease = itemView.findViewById(R.id.tv_rec_memoir_movie_release);
            imagePoster = itemView.findViewById(R.id.rec_memoir_image);
            tvMemoirDate = itemView.findViewById(R.id.tv_rec_memoir_date);
            tvMemoirCinema = itemView.findViewById(R.id.tv_rec_memoir_cinema);
            tvComment = itemView.findViewById(R.id.tv_rec_memoir_comment);
            ratingMy = itemView.findViewById(R.id.ratingBar_memoir_my);
            ratingPublic = itemView.findViewById(R.id.ratingBar_memoir_public);
        }
    }
}
