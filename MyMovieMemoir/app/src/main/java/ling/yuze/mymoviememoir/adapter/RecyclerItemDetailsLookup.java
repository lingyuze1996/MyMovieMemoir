package ling.yuze.mymoviememoir.adapter;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemDetailsLookup extends ItemDetailsLookup<Long> {
    private final RecyclerView recyclerView;

    public RecyclerItemDetailsLookup(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<Long> getItemDetails(@NonNull MotionEvent e) {
        View view = recyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(view);
            if (viewHolder instanceof CinemaChooseRecyclerAdapter.CinemaViewHolder) {
                return ((CinemaChooseRecyclerAdapter.CinemaViewHolder) viewHolder).getItemDetails();
            }
        }

        return null;
    }
}
