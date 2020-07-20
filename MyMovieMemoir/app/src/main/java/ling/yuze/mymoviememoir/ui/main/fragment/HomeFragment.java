package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.HomeRecyclerAdapter;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.network.RestService;

public class HomeFragment extends Fragment {

    private int id;
    private TextView tvDate;
    private RecyclerView recyclerView;
    private HomeRecyclerAdapter adapter;
    private List<Movie> movies;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // get person id
        //SharedPreferences shared = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
        //id = shared.getInt("id", 0);

        tvDate = v.findViewById(R.id.tv_home_date);
        Calendar c = Calendar.getInstance();
        String currentDate =
                c.get(Calendar.YEAR) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH);
        tvDate.setText(currentDate); // display current date

        recyclerView = v.findViewById(R.id.home_recycler);
        movies = Movie.createMovieList();
        adapter = new HomeRecyclerAdapter(movies);
        recyclerView.setAdapter(adapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);

        // retrieve top five recent movies
        //new TaskFindTopFiveRecentMovies().execute();

        return v;
    }

    private class TaskFindTopFiveRecentMovies extends AsyncTask<Void, Void, List<Object[]>> {
        @Override
        protected List doInBackground(Void... voids) {
            RestService rs = new RestService();
            List<Object[]> list = rs.getTopFiveRecentMovies(id);
            return list;
        }

        @Override
        protected void onPostExecute(List<Object[]> list) {
            for (Object[] item : list) {
                adapter.addMovie(new Movie((String) item[0], (String) item[2], (float) ((double) item[1])));
            }
        }
    }
}
