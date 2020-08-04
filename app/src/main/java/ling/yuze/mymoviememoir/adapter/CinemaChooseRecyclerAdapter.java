package ling.yuze.mymoviememoir.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Cinema;

public class CinemaChooseRecyclerAdapter extends RecyclerView.Adapter<CinemaChooseRecyclerAdapter.CinemaViewHolder> {

    private OnItemClickListener itemClickListener;
    private List<Cinema> cinemas;
    private int positionSelected = RecyclerView.NO_POSITION;

    public CinemaChooseRecyclerAdapter(List<Cinema> cinemas) {
        this.cinemas = cinemas;
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setPositionSelected(int positionSelected) {
        this.positionSelected = positionSelected;
    }

    @NonNull
    @Override
    public CinemaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cinemaView = inflater.inflate(R.layout.recycler_item_cinema_choose, parent, false);
        return new CinemaViewHolder(cinemaView);
    }

    @Override
    public void onBindViewHolder(@NonNull CinemaViewHolder holder, final int position) {
        final Cinema cinema = cinemas.get(position);
        holder.tvCinemaName.setText(cinema.getName());
        holder.tvCinemaAddress.setText(cinema.getAddress());

        if (position == positionSelected)
            holder.itemView.setBackgroundColor(Color.parseColor("#b2ebf2"));
        else
            holder.itemView.setBackgroundResource(R.drawable.text_background);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null && position != RecyclerView.NO_POSITION) {

                    // Perform event defined by the fragment/activity
                    itemClickListener.onItemClick(cinemas.get(position));

                    // Set distinct color for item selected
                    if (positionSelected != RecyclerView.NO_POSITION)
                        notifyItemChanged(positionSelected);
                    positionSelected = position;
                    notifyItemChanged(positionSelected);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cinemas.size();
    }

    class CinemaViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCinemaName;
        private TextView tvCinemaAddress;

        public CinemaViewHolder(View itemView) {
            super(itemView);
            tvCinemaName = itemView.findViewById(R.id.tv_rec_cinema_choose_name);
            tvCinemaAddress = itemView.findViewById(R.id.tv_rec_cinema_choose_address);
        }
    }
}
