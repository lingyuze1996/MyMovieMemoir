package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.MemoirRecyclerAdapter;
import ling.yuze.mymoviememoir.adapter.OnItemClickListener;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.viewModel.MemoirViewModel;
import ling.yuze.mymoviememoir.data.viewModel.MovieViewModel;
import ling.yuze.mymoviememoir.data.viewModel.UserViewModel;
import ling.yuze.mymoviememoir.network.AWS;


public class MovieMemoirFragment extends Fragment {

    private MemoirRecyclerAdapter recyclerAdapter;
    private MovieViewModel movieViewModel;
    private UserViewModel userViewModel;
    private MemoirViewModel memoirViewModel;
    private ArrayAdapter<String> genreAdapter;
    private ArrayAdapter<String> sortOptionAdapter;
    private List<Memoir> memoirs = new ArrayList<>();
    private List<String> genres = new ArrayList<>();
    private List<String> sortOptions = new ArrayList<>();
    private Spinner spinnerGenreFilter;
    private Spinner spinnerSort;
    private AWS aws;
    private Handler handler = new Handler();

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        //inflater.inflate(R.menu.nav_menu, menu);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View v = inflater.inflate(R.layout.fragment_memoir, container, false);

        // Retrieve token information
        String token = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("token", null);

        aws = new AWS();
        aws.setToken(token);

        // Initialize view models
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        memoirViewModel = new ViewModelProvider(getActivity()).get(MemoirViewModel.class);

        memoirViewModel.getMemoirs().observe(this.getActivity(), new Observer<List<Memoir>>() {
            @Override
            public void onChanged(List<Memoir> memoirList) {
                ArrayList<String> genreList = new ArrayList<>(extractGenres(memoirList));
                genres.clear();
                genres.add("All");
                genres.addAll(genreList);
                genreAdapter.notifyDataSetChanged();
            }
        });

        // Initialize genre filter and its adapter
        spinnerGenreFilter = v.findViewById(R.id.spinner_genre_filter);

        genreAdapter = new ArrayAdapter<>(v.getContext(), R.layout.spinner_item, genres);
        spinnerGenreFilter.setAdapter(genreAdapter);

        spinnerGenreFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genre = parent.getSelectedItem().toString();

                filterByGenre(genre);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        // Initialize genre filter and its adapter
        spinnerSort = v.findViewById(R.id.spinner_memoir_sort);

        generateSortOptions();
        sortOptionAdapter = new ArrayAdapter<>(v.getContext(), R.layout.spinner_item, sortOptions);
        spinnerSort.setAdapter(sortOptionAdapter);

        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortOption = parent.getSelectedItem().toString();

                // sort the list based on different options and notify the adapter to refresh
                switch (sortOption) {
                    case "My Rating: High - Low":
                        sortMyRatingDescending();
                        break;
                    case "My Rating: Low - High":
                        sortMyRatingAscending();
                        break;
                    case "Public Rating: High - Low":
                        sortPublicRatingDescending();
                        break;
                    case "Public Rating: Low - High":
                        sortPublicRatingAscending();
                        break;
                    case "New - Old":
                        sortWatchingDateDescending();
                        break;
                    case "Old - New":
                        sortWatchingDateAscending();
                        break;
                }

                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        RecyclerView recyclerView = v.findViewById(R.id.memoirs_recycler);
        recyclerAdapter = new MemoirRecyclerAdapter(memoirs);

        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        new Thread(new GetMemoirs()).start();

        recyclerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                final Memoir memoir = (Memoir) item;
                movieViewModel.setMovie(memoir.getMovie());

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, new MovieViewFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;
    }

    private class GetMemoirs implements Runnable {
        @Override
        public void run() {
            String username = userViewModel.getUser().getValue().getUsername();
            final List<Memoir> memoirList = aws.getUserMemoirs(username);

            setMemoirs(memoirList);


            handler.post(new Runnable() {
                @Override
                public void run() {
                    memoirViewModel.setMemoirs(memoirList);
                    recyclerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void setMemoirs(List<Memoir> memoirList) {
        memoirs.clear();
        memoirs.addAll(memoirList);
    }

    private HashSet<String> extractGenres(List<Memoir> memoirList) {
        HashSet<String> genres = new HashSet<>();
        for (Memoir memoir : memoirList) {
            genres.addAll(memoir.getMovie().getGenres());
        }
        return genres;
    }

    private void filterByGenre(String genre) {
        if (genre.toLowerCase().equals("all")) {
            setMemoirs(memoirViewModel.getMemoirs().getValue());
        } else {
            memoirs.clear();
            for (Memoir memoir : memoirViewModel.getMemoirs().getValue()) {
                if (memoir.getMovie().getGenres().contains(genre))
                    memoirs.add(memoir);
            }

        }
        recyclerAdapter.notifyDataSetChanged();

    }

    private void generateSortOptions() {
        sortOptions.add("New - Old");
        sortOptions.add("Old - New");
        sortOptions.add("My Rating: High - Low");
        sortOptions.add("My Rating: Low - High");
        sortOptions.add("Public Rating: High - Low");
        sortOptions.add("Public Rating: Low - High");
    }


    private void sortMyRatingDescending() {
        Collections.sort(memoirs, new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMemoirRating(), o2.getMemoirRating()) * (-1);
            }
        });

    }

    private void sortMyRatingAscending() {
        Collections.sort(memoirs, new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMemoirRating(), o2.getMemoirRating());
            }
        });
    }

    private void sortPublicRatingDescending() {
        Collections.sort(memoirs, new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating()) * (-1);
            }
        });
    }

    private void sortPublicRatingAscending() {
        Collections.sort(memoirs, new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating());
            }
        });
    }

    private void sortWatchingDateAscending() {
        Collections.sort(memoirs, new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return o1.getMemoirTime().compareTo(o2.getMemoirTime());
            }
        });
    }

    private void sortWatchingDateDescending() {
        Collections.sort(memoirs, new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return o1.getMemoirTime().compareTo(o2.getMemoirTime()) * (-1);
            }
        });
    }


}
