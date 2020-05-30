package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterWatchlist;
import ling.yuze.mymoviememoir.data.room.database.MovieToWatchDatabase;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.viewmodel.MovieToWatchViewModel;

public class WatchlistFragment extends Fragment {
    private ListView listView;
    private ListAdapterWatchlist adapter;
    private MovieToWatchViewModel viewModel;
    private List<MovieToWatch> movieList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_watchlist, container, false);
        listView = v.findViewById(R.id.list_view_watchlist);
        adapter = new ListAdapterWatchlist(getContext(), R.layout.list_view_watchlist, movieList);
        listView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MovieToWatchViewModel.class);
        viewModel.initializeVars(this.getActivity().getApplication());
        viewModel.getAllMoviesToWatch().observe(this, new Observer<List<MovieToWatch>>() {
            @Override
            public void onChanged(@Nullable final List<MovieToWatch> moviesToWatch)
            {
                movieList = moviesToWatch;
                adapter.notifyDataSetChanged();
            }
        });


        return v;
    }
}
