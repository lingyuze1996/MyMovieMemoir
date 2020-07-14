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
import ling.yuze.mymoviememoir.data.Cinema;

public class CinemaChooseRecyclerAdapter extends RecyclerView.Adapter<CinemaChooseRecyclerAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCinemaName;
        TextView tvCinemaAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            tvCinemaName = itemView.findViewById(R.id.tv_rec_cinema_choose_name);
            tvCinemaAddress = itemView.findViewById(R.id.tv_rec_cinema_choose_address);
        }
    }

    private List<Cinema> cinemas;
    public CinemaChooseRecyclerAdapter(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }
    public void addCinema(Cinema newCinema) {
        cinemas.add(newCinema);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cinemasView = inflater.inflate(R.layout.recycler_cinema_choose, parent, false);
        return new ViewHolder(cinemasView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Cinema cinema = cinemas.get(position);
        holder.tvCinemaName.setText(cinema.getName());
        holder.tvCinemaAddress.setText(cinema.getAddress());
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }
}
