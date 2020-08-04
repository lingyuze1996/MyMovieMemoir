package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.WatchlistRecyclerAdapter;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.viewModel.MovieToWatchViewModel;

public class WatchlistFragment extends Fragment {
    private MovieToWatchViewModel viewModel;
    private WatchlistRecyclerAdapter adapter;

    private List<MovieToWatch> movies = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_watchlist, container, false);

        // room database view model observer
        viewModel = new ViewModelProvider(getActivity()).get(MovieToWatchViewModel.class);

        //movies.addAll(viewModel.getAllMoviesToWatch().getValue());

        RecyclerView recyclerView = v.findViewById(R.id.recycler_view_watchlist);

        adapter = new WatchlistRecyclerAdapter(movies);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.getAllMoviesToWatch().observe(this.getActivity(), new Observer<List<MovieToWatch>>() {
            @Override
            public void onChanged(@Nullable List<MovieToWatch> moviesToWatch)
            {
                // update the list of movies to watch
                movies.clear();
                movies.addAll(moviesToWatch);
                adapter.notifyDataSetChanged();
            }
        });

        /*
        // show view/delete options when long clicking on the item
        recyclerView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {
                // locate the watchlist item selected
                final MovieToWatch m = (MovieToWatch) recyclerView.getAdapter().getItem(position);

                Button delete = view.findViewById(R.id.bt_delete);
                final Button viewMovie = view.findViewById(R.id.bt_view);

                viewMovie.setVisibility(View.VISIBLE);
                viewMovie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // when view button is clicked, redirect to view screen
                        //new TaskGetMovieBasics().execute(m.getMovieName(), m.getReleaseDate());
                    }
                });

                delete.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // when delete button is clicked, pop the alert dialogue
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Delete from watchlist?");
                        builder.setTitle("Attention");
                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), R.string.success_delete, Toast.LENGTH_LONG).show();
                                viewModel.delete(m);
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        });
                        builder.create().show();
                    }
                });
                return true;
            }
        });

         */


        return v;
    }
}
