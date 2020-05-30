package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.ListAdapterWatchlist;
import ling.yuze.mymoviememoir.data.room.database.MovieToWatchDatabase;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.viewmodel.MovieToWatchViewModel;

public class WatchlistFragment extends Fragment {
    private ListView listView;
    private MovieToWatchViewModel viewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_watchlist, container, false);

        listView = v.findViewById(R.id.list_view_watchlist);

        viewModel = new ViewModelProvider(getActivity()).get(MovieToWatchViewModel.class);
        viewModel.initializeVars(requireActivity().getApplication());
        viewModel.getAllMoviesToWatch().observe(this.getActivity(), new Observer<List<MovieToWatch>>() {
            @Override
            public void onChanged(@Nullable final List<MovieToWatch> moviesToWatch)
            {
                listView.setAdapter(new ListAdapterWatchlist(getContext(), R.layout.list_view_watchlist, moviesToWatch));
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieToWatch m = (MovieToWatch) listView.getAdapter().getItem(position);
            }
        });


        return v;
    }
}
