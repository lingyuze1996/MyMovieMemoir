package ling.yuze.mymoviememoir.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SearchMovieDB extends NetworkConnection {
    private String API_KEY;
    private String BASE_URL;
    private final String BASE_URL_DETAIL = "https://api.themoviedb.org/3/movie/";
    private String TAIL_URL_DETAIL;

    public void setAPIKey(String key) {
        API_KEY = key;
        BASE_URL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=";
        TAIL_URL_DETAIL = "?api_key=" + API_KEY;
    }

    public SearchMovieDB() {super();}
    @Override
    public void setUrl(String path) {
        super.setUrl(BASE_URL + path);
    }

    public void setUrl(int id, String key) {
        super.setUrl(BASE_URL_DETAIL + id + key + TAIL_URL_DETAIL);
    }

    public List<Object[]> searchBasics(String content) {
        List<Object[]> basics = new ArrayList<>();
        final String path = content.replace(" ", "+");
        setUrl(path);
        try {
            String response = httpGet();
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray infos = jsonResponse.getJSONArray("results");

            if (infos.length() == 0) return null;

            for (int i = 0; i < infos.length(); i++) {
                Object[] basic = new Object[6];
                JSONObject info = infos.getJSONObject(i);
                basic[0] = info.getInt("id");
                basic[1] = info.getString("title");
                basic[2] = info.getString("release_date");
                basic[3] = info.getString("poster_path").substring(1);
                basic[4] = info.getString("overview");
                basic[5] = info.getDouble("vote_average");
                basics.add(basic);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return basics;
        }
    }

    public List<String> searchCast(int id) {
        setUrl(id, "/credits");

        List<String> list = new ArrayList<>();

        try {
            String response = httpGet();
            JSONObject jsonInfo = new JSONObject(response);
            JSONArray castJson = jsonInfo.getJSONArray("cast");
            for (int i = 0; i < castJson.length() && i <= 5; i ++) {
                String member = castJson.getJSONObject(i).getString("name");
                list.add(member);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    public List<String> searchCountries(int id) {
        setUrl(id, "");

        List<String> list = new ArrayList<>();

        try {
            String response = httpGet();
            JSONObject jsonInfo = new JSONObject(response);
            JSONArray countryJson = jsonInfo.getJSONArray("production_countries");
            for (int i = 0; i < countryJson.length(); i ++) {
                String country = countryJson.getJSONObject(i).getString("name");
                list.add(country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    public List<String> searchDirector(int id) {
        setUrl(id, "/credits");

        List<String> list = new ArrayList<>();

        try {
            String response = httpGet();
            JSONObject jsonInfo = new JSONObject(response);
            JSONArray crewJson = jsonInfo.getJSONArray("crew");
            for (int i = 0; i < crewJson.length(); i ++) {
                JSONObject memberJson = crewJson.getJSONObject(i);
                String job = memberJson.getString("job");
                if (job.equals("Director")) {
                    String director = memberJson.getString("name");
                    list.add(director);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return list;
        }
    }

    public List<String> searchGenres(int id) {
        setUrl(id, "");

        List<String> genreList = new ArrayList<>();

        try {
            String response = httpGet();
            JSONObject jsonInfo = new JSONObject(response);
            JSONArray genreJson = jsonInfo.getJSONArray("genres");
            for (int i = 0; i < genreJson.length(); i ++) {
                String genre = genreJson.getJSONObject(i).getString("name");
                genreList.add(genre);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return genreList;
        }
    }



}
