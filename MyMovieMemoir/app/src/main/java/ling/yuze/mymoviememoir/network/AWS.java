package ling.yuze.mymoviememoir.network;

import com.amplifyframework.core.Amplify;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.data.Cinema;

public class AWS extends NetworkConnection{
    private static final String BASE_URL = "https://j0wq6141yh.execute-api.ap-southeast-2.amazonaws.com/beta/";
    public AWS() {super();}

    @Override
    public void setUrl(String path) {
        super.setUrl(BASE_URL + path);
    }

    public boolean userSignUp(String request) {
        boolean success = false;
        final String path = "signup";
        setUrl(path);
        try {
            int responseCode = httpPost(request);
            if (responseCode == 200) {
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    public List<Cinema> getCinemas(String state, String region) {
        List<Cinema> cinemas = new ArrayList<>();

        final String path = "cinema/" + state + "/" + region;
        setUrl(path);

        try {
            String response = httpGet();

            JSONArray cinemasJSON = new JSONArray(response);

            for (int i = 0; i < cinemasJSON.length(); i++) {
                JSONObject cinemaJSON = cinemasJSON.getJSONObject(i);
                String id = cinemaJSON.getString("cinemaId");
                String name = cinemaJSON.getString("name");
                String address = cinemaJSON.getString("address");
                Cinema cinema = new Cinema(id, name, address, state, region);
                cinemas.add(cinema);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return cinemas;
    }

    public List<String> getCinemaRegionsByState(String state) {
        List<String> regions = new ArrayList<>();

        final String path = "cinema/" + state + "/regions";
        setUrl(path);

        try {
            String response = httpGet();

            JSONArray regionsJSON = new JSONArray(response);

            for (int i = 0; i < regionsJSON.length(); i++) {
                JSONObject regionJSON = regionsJSON.getJSONObject(i);
                String region = regionJSON.getString("region");
                regions.add(region);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return regions;
    }
}
