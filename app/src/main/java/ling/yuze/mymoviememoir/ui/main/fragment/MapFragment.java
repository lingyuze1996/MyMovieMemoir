package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.viewModel.UserViewModel;
import ling.yuze.mymoviememoir.network.AWS;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private UserViewModel userViewModel;
    private GoogleMap mMap;
    private Handler handler = new Handler();
    private Geocoder geocoder;
    private AWS aws;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Initialize Geocoder
        geocoder = new Geocoder(getContext());

        View v = inflater.inflate(R.layout.fragment_map, container, false);

        // Retrieve token information
        String token = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
                .getString("token", null);

        aws = new AWS();
        aws.setToken(token);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Retrieve user's home address and show it on the map
            String homeAddressString = userViewModel.getUser().getValue().getAddress();
            Address homeAddress = geocoder.getFromLocationName(homeAddressString, 1).get(0);
            LatLng homeLatLng = new LatLng(homeAddress.getLatitude(), homeAddress.getLongitude());

            showLocation("Home", homeLatLng);

            // Retrieve addresses of all cinemas nearby and show them on the map
            new Thread(new GetCinemasNearby(homeAddress, 6)).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class GetCinemasNearby implements Runnable {
        private Address address;
        private double maxDistanceKm;

        public GetCinemasNearby(Address address, double maxDistanceKm) {
            this.address = address;
            this.maxDistanceKm = maxDistanceKm;
        }

        @Override
        public void run() {
            List<Cinema> cinemas = aws.getCinemas(userViewModel.getUser().getValue().getState(), "All");
            try {
                for (final Cinema cinema : cinemas) {
                    // Address for an individual cinema
                    Address cinemaAddress = geocoder
                            .getFromLocationName(cinema.getAddress(), 1)
                            .get(0);

                    if (getDistance(cinemaAddress, address) > maxDistanceKm * 1000)
                        continue;

                    final LatLng cinemaLatLng = new LatLng(cinemaAddress.getLatitude(), cinemaAddress.getLongitude());

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            // add marker for each cinema and set color to azure
                            mMap.addMarker(new MarkerOptions()
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                    .position(cinemaLatLng).title(cinema.getName()));
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private float getDistance(Address a1, Address a2) {
        float[] results = new float[1];
        double startLatitude = a1.getLatitude();
        double startLongitude = a1.getLongitude();
        double endLatitude = a2.getLatitude();
        double endLongitude = a2.getLongitude();

        Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);

        return results[0];
    }


    private void showLocation(String title, LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).title(title));

        float zoomLevel = 10f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel));
    }

}
