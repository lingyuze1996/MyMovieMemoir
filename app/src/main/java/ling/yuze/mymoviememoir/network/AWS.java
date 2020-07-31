package ling.yuze.mymoviememoir.network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.User;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AWS extends NetworkConnection {
    private final String BASE_URL = "https://j0wq6141yh.execute-api.ap-southeast-2.amazonaws.com/prod/";
    private String token;

    public AWS() {
        super();
    }

    @Override
    public String httpGet() throws IOException {
        String responseString;
        Request.Builder builder = new Request.Builder();
        builder.url(getUrl());
        if (token != null)
            builder.addHeader("Authorization", token);
        Request request = builder.build();
        Response response = getClient().newCall(request).execute();
        responseString = response.body().string();

        return responseString;
    }

    @Override
    public String httpPost(String jsonString) throws IOException {
        String responseString;

        RequestBody body = RequestBody.create(jsonString, JSON);
        Request.Builder builder = new Request.Builder();
        builder.url(getUrl());
        if (token != null)
            builder.addHeader("Authorization", token);
        Request request = builder.post(body).build();
        Response response = getClient().newCall(request).execute();
        responseString = response.body().toString();
        return responseString;
    }

    @Override
    public void setUrl(String path) {
        super.setUrl(BASE_URL + path);
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String userSignUp(User user) {
        String ret = "Invalid";
        final String path = "signup";
        setUrl(path);
        String request = new Gson().toJson(user);
        try {
            String response = httpPost(request);
            if (response.isEmpty()) {
                ret = "success";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public String userSignIn(User user) {
        String userToken;
        final String path = "signin";
        setUrl(path);
        String request = new Gson().toJson(user);
        try {
            String response = httpPost(request);
            JSONObject responseJSON = new JSONObject(response);
            userToken = responseJSON.getString("token");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return userToken;
    }

    public boolean postMemoir(Memoir memoir) {
        boolean success = false;
        final String path = "memoir";
        setUrl(path);
        String request = new Gson().toJson(memoir);
        try {
            String response = httpPost(request);
            if (response.isEmpty()) {
                success = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    //public List<Memoir> getUserMemoirs(String UserId)

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
            return null;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            return null;
        }

        return regions;
    }
}
