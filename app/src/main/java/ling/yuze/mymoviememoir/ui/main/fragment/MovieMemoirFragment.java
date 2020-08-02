package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
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
    private List<Memoir> memoirs;
    private RecyclerView recyclerView;
    private MemoirRecyclerAdapter recyclerAdapter;
    private MovieViewModel movieViewModel;
    private UserViewModel userViewModel;
    private MemoirViewModel memoirViewModel;
    private Spinner spinnerGenreFilter;
    private ArrayAdapter<String> genreAdapter;
    private HashSet<String> genreSet = new HashSet<>();
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

        // Initialize view models
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        movieViewModel = new ViewModelProvider(getActivity()).get(MovieViewModel.class);
        memoirViewModel = new ViewModelProvider(getActivity()).get(MemoirViewModel.class);

        // Retrieve token information
        String token = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("token", null);

        aws = new AWS();
        aws.setToken(token);

        /*
        spinnerGenreFilter = v.findViewById(R.id.spinner_genre_filter);

        spinnerGenreFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerGenreFilter.getSelectedItem().toString();

                new TaskFilterGenre().execute(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });*/

        spinnerSort = v.findViewById(R.id.spinner_memoir_sort);

        /*
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinnerSort.getSelectedItem().toString();
                // sort the list based on different options and notify the adapter to refresh
                switch (selected) {
                    case "my rating ↓":
                        sortMyRatingDescending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "my rating ↑":
                        sortMyRatingAscending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "public rating ↓":
                        sortPublicRatingDescending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "public rating ↑":
                        sortPublicRatingAscending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "watching date ↓":
                        sortWatchingDateDescending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                    case "watching date ↑":
                        sortWatchingDateAscending();
                        recyclerAdapter.notifyDataSetChanged();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

         */

        recyclerView = v.findViewById(R.id.memoirs_recycler);
        memoirs = new ArrayList<>(); // store memoir items for display
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
            List<Memoir> memoirList = aws.getUserMemoirs(username);

            setMemoirs(memoirList);
            memoirViewModel.setMemoirs(memoirList);
            
            handler.post(new Runnable() {
                @Override
                public void run() {
                    recyclerAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    private void setMemoirs(List<Memoir> memoirList) {
        memoirs.clear();
        memoirs.addAll(memoirList);
    }

}

    /*private void sortMyRatingDescending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMyRating(), o2.getMyRating()) * (-1);
            }
        });
    }

    private void sortMyRatingAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMyRating(), o2.getMyRating());
            }
        });
    }

    private void sortPublicRatingDescending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating()) * (-1);
            }
        });
    }

    private void sortPublicRatingAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating());
            }
        });
    }

    private void sortWatchingDateAscending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return compareDate(o1.getWatching(), o2.getWatching());
            }
        });
    }

    private void sortWatchingDateDescending() {
        Collections.sort(memoirs, new Comparator<MemoirItem>() {
            @Override
            public int compare(MemoirItem o1, MemoirItem o2) {
                return compareDate(o1.getWatching(), o2.getWatching()) * (-1);
            }
        });
    }

    private class TaskFilterGenre extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            String selected = strings[0];
            if (selected.equals("All")) {
                memoirs = memoirsAll;
                return null;
            }

            List<MemoirItem> filteredMemoirs = new ArrayList<>();
            for (MemoirItem memoir : memoirsAll) {
                if (memoir.getMovie().getGenres().contains(selected)) {
                    filteredMemoirs.add(memoir);
                }
            }
            memoirs = filteredMemoirs;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            adapter = new ListAdapterMemoir(getContext(), R.layout.recycler_item_memoir, memoirs);
            listView.setAdapter(adapter);
        }

    }*/
