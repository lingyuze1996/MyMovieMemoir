package ling.yuze.mymoviememoir.network;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.User;

public class AWS extends NetworkConnection{
    private static final String BASE_URL = "https://j0wq6141yh.execute-api.ap-southeast-2.amazonaws.com/beta/";
    public AWS() {super();}

    @Override
    public void setUrl(String path) {
        super.setUrl(BASE_URL + path);
    }

    public boolean userSignUp(User user) {
        boolean success = false;
        final String path = "signup";
        setUrl(path);
        String request = new Gson().toJson(user);
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

    public boolean postMemoir(Memoir memoir) {
        boolean success = false;
        final String path = "memoir";
        setUrl(path);
        String request = new Gson().toJson(memoir);
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

    public User getUserInfo(String username) {
        User user = new User(username);

        final String path = "user/" + username;
        setUrl(path);

        try {
            String response = httpGet();

            JSONArray jsonArray = new JSONArray(response);

            if (jsonArray.length() != 1)
                return user;

            JSONObject userJSON = jsonArray.getJSONObject(0);
            user.setUserId(userJSON.getString("userId"));
            user.setFirstName(userJSON.getString("firstName"));
            user.setSurname(userJSON.getString("surname"));
            user.setGender(userJSON.getString("gender"));
            user.setDob(userJSON.getString("dob"));
            user.setAddress(userJSON.getString("address"));
            user.setState(userJSON.getString("state"));
            user.setPostcode(userJSON.getString("postcode"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;

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
