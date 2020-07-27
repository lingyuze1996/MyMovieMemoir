package ling.yuze.mymoviememoir.network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.data.Movie;


public class SearchMovieDB extends NetworkConnection {
    private String API_KEY;
    private String BASE_URL_QUERY;
    private final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    private String TAIL_URL;

    public void setAPIKey(String key) {
        API_KEY = key;
        BASE_URL_QUERY = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=";
        TAIL_URL = "?api_key=" + API_KEY;
    }

    public SearchMovieDB() {super();}

    /**
     * Url Setting: Search for a list of movies with a query String
     * @param query String: the query String
     */
    public void setQueryUrl(String query) {
        super.setUrl(BASE_URL_QUERY + query);
    }

    /**
     * Url Setting: Search for a list of movies from a specified aspect
     * @param aspect String: the key aspect of movie searching
     */
    @Override
    public void setUrl(String aspect) {
        super.setUrl(BASE_URL + aspect + TAIL_URL);
    }

    /**
     * Url setting: Search for a specified aspect of a movie
     * @param id Integer: the id of the movie
     * @param aspect String: the key aspect of the movie
     */
    public void setUrl(int id, String aspect) {
        super.setUrl(BASE_URL + id + "/" + aspect + TAIL_URL);
    }

    /**
     * Extract basic movie information from the response json provided by the api
     * @param response String: response json from the api
     * @return List<Movie>: a list of Movie objects
     */
    public List<Movie> extractBasics(String response) {
        List<Movie> movies = new ArrayList<>();
        try {
            JSONArray moviesJSON = new JSONObject(response).getJSONArray("results");
            for (int i = 0; i < moviesJSON.length(); i++) {
                JSONObject movieJSON = moviesJSON.getJSONObject(i);
                int id = movieJSON.getInt("id");
                String title = movieJSON.getString("title");
                String releaseDate = movieJSON.getString("release_date");
                String imagePath = movieJSON.getString("poster_path");
                float publicRating = (float) movieJSON.getDouble("vote_average");
                String overview = movieJSON.getString("overview");

                Movie movie = new Movie(id, title, releaseDate, imagePath, overview, publicRating);
                movies.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    public List<Object[]> searchByQuery(String query) {
        List<Object[]> basics = new ArrayList<>();
        final String path = query.replace(" ", "+");
        setQueryUrl(path);
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
        }

        return basics;
    }

    public List<Movie> searchPopular() {
        setUrl("popular");
        List<Movie> movies = null;

        try {
            String response = httpGet();
            movies = extractBasics(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public List<String> searchCast(int id) {
        setUrl(id, "credits");

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
        setUrl(id, "credits");

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
