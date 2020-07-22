package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private int id;
    private GoogleMap mMap;
    private LatLng homeAddress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // get person id
        SharedPreferences shared = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
        id = shared.getInt("id", 0);

        //retrieve person's home address and show it on the map
        new TaskGetAddress().execute();

        //retrieve addresses of all cinemas in server database and show them on the map
        new TaskGetCinemas().execute();

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private class TaskGetCinemas extends AsyncTask<Void, Void, List<Object[]>> {
        @Override
        protected List<Object[]> doInBackground(Void... voids) {
            List<Object[]> cinemaList = new ArrayList<>();
            //RestService rs = new RestService();
            //cinemaList = rs.getAllCinemas();
            return cinemaList;
        }

        @Override
        protected void onPostExecute(List<Object[]> list) {
            for (Object[] cinema : list) {
                String suburb = (String) cinema[1];
                String name = (String) cinema[2];

                try {
                    Geocoder geocoder = new Geocoder(getContext());
                    Address address = geocoder
                            .getFromLocationName(suburb + "Australia", 1)
                            .get(0);

                    LatLng location = new LatLng(address.getLatitude(), address.getLongitude());

                    // add marker for each cinema and set color to azure
                    mMap.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .position(location).title(name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class TaskGetAddress extends AsyncTask<Void, Void, Double[]> {
        @Override
        protected Double[] doInBackground(Void... voids) {
            Double[] coordinate = new Double[2];
            /*
            RestService rs = new RestService();
            String address = rs.getAddressByPersonId(id);
            Geocoder geocoder = new Geocoder(getContext());
            try {
                List<Address> addressList = geocoder.getFromLocationName(address, 1);
                double latitude = addressList.get(0).getLatitude();
                double longitude = addressList.get(0).getLongitude();
                coordinate[0] = latitude;
                coordinate[1] = longitude;
            } catch (Exception e) {
                e.printStackTrace();
            }

             */
            return coordinate;
        }

        @Override
        protected void onPostExecute(Double[] coordinate) {
            if (coordinate[0] == null || coordinate[1] == null) return;
            homeAddress = new LatLng(coordinate[0], coordinate[1]);
            mMap.addMarker(new MarkerOptions().position(homeAddress).title("Home"));

            float zoomLevel = 10f;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeAddress, zoomLevel));
        }
    }

}
