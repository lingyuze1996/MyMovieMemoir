package ling.yuze.mymoviememoir.ui.main.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.adapter.CinemaChooseRecyclerAdapter;
import ling.yuze.mymoviememoir.adapter.OnItemClickListener;
import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.viewModel.CinemaViewModel;
import ling.yuze.mymoviememoir.network.AWS;

public class CinemaChooseFragment extends Fragment {
    private Spinner spinnerState;
    private Spinner spinnerRegion;

    private List<String> regions;
    private ArrayAdapter<String> adapterRegion;

    private List<Cinema> cinemas = new ArrayList<>();
    private CinemaChooseRecyclerAdapter adapterCinemas;

    private CinemaViewModel cinemaViewModel;
    private Handler mHandler = new Handler();
    private AWS aws = new AWS();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_choose_cinema, container, false);

        // initialize cinema view model
        cinemaViewModel = new ViewModelProvider(getActivity()).get(CinemaViewModel.class);

        // spinner for cinema state
        spinnerState = v.findViewById(R.id.spinner_cinema_state);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String state = parent.getSelectedItem().toString();
                clearCinemaSelection();
                new Thread(new GetCinemaRegions(state)).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // spinner for cinema region
        spinnerRegion = v.findViewById(R.id.spinner_cinema_region);
        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String region = parent.getSelectedItem().toString();
                String state = spinnerState.getSelectedItem().toString();
                clearCinemaSelection();
                new Thread(new GetCinemas(state, region)).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        regions = new ArrayList<>();
        regions.add("All");

        adapterRegion = new ArrayAdapter<>(getContext(), R.layout.spinner_item, regions);
        spinnerRegion.setAdapter(adapterRegion);

        // recycler view for cinemas list
        RecyclerView recyclerView = v.findViewById(R.id.cinemas_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapterCinemas = new CinemaChooseRecyclerAdapter(cinemas);
        adapterCinemas.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                Cinema cinema = (Cinema) item;
                cinemaViewModel.setCinema(cinema);
                Toast.makeText(getContext(), cinema.getName() + " Selected!", Toast.LENGTH_LONG).show();
            }
        });

        recyclerView.setAdapter(adapterCinemas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // button for cinema selection
        Button buttonConfirm = v.findViewById(R.id.bt_confirm_cinema);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cinemaViewModel.getCinema().getValue() == null) {
                    Toast.makeText(getContext(), R.string.error_cinema_empty, Toast.LENGTH_LONG).show();
                }
                else
                    getFragmentManager().popBackStack();
            }
        });

        return v;
    }



    private class GetCinemas implements Runnable {
        private String mState;
        private String mRegion;

        public GetCinemas(String state, String region) {
            mState = state;
            mRegion = region;
        }

        @Override
        public void run() {
            AWS search = new AWS();
            final List<Cinema> cinemaList = search.getCinemas(mState, mRegion);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    cinemas.clear();
                    cinemas.addAll(cinemaList);
                    adapterCinemas.notifyDataSetChanged();
                }
            });
        }
    }

    private class GetCinemaRegions implements Runnable {
        private String mState;

        public GetCinemaRegions(String state) {
            mState = state;
        }

        @Override
        public void run() {
            final List<String> regionsList = aws.getCinemaRegionsByState(mState);
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    regions.clear();
                    regions.add("All");
                    regions.addAll(regionsList);
                    adapterRegion.notifyDataSetChanged();
                    spinnerRegion.setSelection(0);
                    new Thread(new GetCinemas(spinnerState.getSelectedItem().toString(),
                            spinnerRegion.getSelectedItem().toString())).start();
                }
            });
        }
    }

    private void clearCinemaSelection() {
        adapterCinemas.setPositionSelected(RecyclerView.NO_POSITION);
        cinemaViewModel.setCinema(null);
    }
}
