package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Cinema;

public class CinemaChooseRecyclerAdapter extends RecyclerView.Adapter<CinemaChooseRecyclerAdapter.CinemaViewHolder> {

    public class CinemaViewHolder extends RecyclerView.ViewHolder {
        TextView tvCinemaName;
        TextView tvCinemaAddress;

        public CinemaViewHolder(View itemView) {
            super(itemView);

            tvCinemaName = itemView.findViewById(R.id.tv_rec_cinema_choose_name);
            tvCinemaAddress = itemView.findViewById(R.id.tv_rec_cinema_choose_address);
        }

        public ItemDetailsLookup.ItemDetails<Long> getItemDetails() {
            return new ItemDetailsLookup.ItemDetails<Long>() {
                @Override
                public int getPosition() {
                    return getAdapterPosition();
                }

                @Override
                public Long getSelectionKey() {
                    return (long) getAdapterPosition();
                }
            };
        }
    }

    private List<Cinema> cinemas;
    public CinemaChooseRecyclerAdapter(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cinemasView = inflater.inflate(R.layout.recycler_cinema_choose, parent, false);
        return new CinemaViewHolder(cinemasView);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, int position) {
        final Cinema cinema = cinemas.get(position);
        holder.tvCinemaName.setText(cinema.getName());
        holder.tvCinemaAddress.setText(cinema.getAddress());
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }
}
