package ling.yuze.mymoviememoir.ui.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.CinemaChooseRecyclerAdapter;
import ling.yuze.mymoviememoir.adapter.HomeRecyclerAdapter;
import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.network.NetworkConnection;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.network.SearchAWS;
import okhttp3.internal.concurrent.Task;

public class CinemaChooseFragment extends Fragment {
    private Spinner spinnerState;
    private Spinner spinnerRegion;

    private Button buttonSearch;
    private Button buttonConfirm;

    private List<String> regions;
    private ArrayAdapter adapterRegion;

    private List<Cinema> cinemas;
    private CinemaChooseRecyclerAdapter adapterCinemas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_cinema, container, false);

        // spinner for cinema state
        spinnerState = v.findViewById(R.id.spinner_cinema_state);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                new TaskGetCinemaRegions().execute(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // spinner for cinema region
        spinnerRegion = v.findViewById(R.id.spinner_cinema_region);

        regions = new ArrayList<>();
        regions.add("All");

        adapterRegion = new ArrayAdapter<>(getContext(), R.layout.spinner_item, regions);
        spinnerRegion.setAdapter(adapterRegion);

        // button for cinema search
        buttonSearch = v.findViewById(R.id.bt_search_cinemas);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = spinnerState.getSelectedItem().toString();
                String region = spinnerRegion.getSelectedItem().toString();
                new TaskGetCinemas().execute(state, region);
            }
        });

        // recycler view for cinemas list
        RecyclerView recyclerView = v.findViewById(R.id.cinemas_recycler);

        cinemas = new ArrayList<>();
        adapterCinemas = new CinemaChooseRecyclerAdapter(cinemas);
        recyclerView.setAdapter(adapterCinemas);

        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));

        // button for cinema selection
        buttonConfirm = v.findViewById(R.id.bt_confirm_cinema);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getTargetFragment() == null)
                    return;
                Intent intent = new Intent();
                intent.putExtra("cinemaChosen", "some cinema");
                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);

                FragmentManager fm = getFragmentManager();
                fm.popBackStack();

            }
        });

        return v;
    }
    private class TaskGetCinemas extends AsyncTask<String, Void, List<Cinema>> {
        @Override
        protected List<Cinema> doInBackground(String... strings) {
            SearchAWS search = new SearchAWS();
            List<Cinema> cinemas = search.getCinemas(strings[0], strings[1]);
            return cinemas;
        }

        @Override
        protected void onPostExecute(List<Cinema> cinemasList) {
            cinemas.clear();
            cinemas.addAll(cinemasList);
            adapterCinemas.notifyDataSetChanged();
        }
    }

    private class TaskGetCinemaRegions extends AsyncTask<String, Void, List<String>> {
        @Override
        protected List<String> doInBackground(String... strings) {
            SearchAWS search = new SearchAWS();
            List<String> regions = search.getCinemaRegionsByState(strings[0]);
            return regions;
        }

        @Override
        protected void onPostExecute(List<String> regionsList) {
            regions.clear();
            regions.add("All");
            regions.addAll(regionsList);
            adapterRegion.notifyDataSetChanged();
            spinnerRegion.setSelection(0);
        }
    }
}
