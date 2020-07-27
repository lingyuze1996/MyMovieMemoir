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
     * Url setting: Search for detailed information of a movie
     * @param id Integer: the id of the movie
     */
    public void setUrl(int id) {
        super.setUrl(BASE_URL + id + TAIL_URL);
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

    public List<Movie> searchByQuery(String query) {
        List<Movie> movies = null;
        final String path = query.replace(" ", "+");
        setQueryUrl(path);
        try {
            String response = httpGet();
            movies = extractBasics(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movies;
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

    public Movie getCredits(Movie movie) {
        setUrl(movie.getId(), "credits");

        List<String> cast = new ArrayList<>();
        List<String> directors = new ArrayList<>();

        try {
            String response = httpGet();
            JSONObject responseJSON = new JSONObject(response);

            JSONArray castJson = responseJSON.getJSONArray("cast");
            for (int i = 0; i < castJson.length() && i <= 5; i ++) {
                String member = castJson.getJSONObject(i).getString("name");
                cast.add(member);
            }

            JSONArray crewJson = responseJSON.getJSONArray("crew");
            for (int i = 0; i < crewJson.length(); i ++) {
                JSONObject memberJson = crewJson.getJSONObject(i);
                String job = memberJson.getString("job");
                if (job.equals("Director")) {
                    String director = memberJson.getString("name");
                    directors.add(director);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        movie.setCast(cast);
        movie.setDirectors(directors);

        return movie;
    }

    public Movie getDetails(Movie movie) {
        setUrl(movie.getId());
        List<String> countries = new ArrayList<>();
        List<String> genres = new ArrayList<>();

        try {
            String response = httpGet();
            JSONObject responseJSON = new JSONObject(response);

            JSONArray countriesJson = responseJSON.getJSONArray("production_countries");
            for (int i = 0; i < countriesJson.length(); i ++) {
                String country = countriesJson.getJSONObject(i).getString("name");
                countries.add(country);
            }

            JSONArray genresJson = responseJSON.getJSONArray("genres");
            for (int i = 0; i < genresJson.length(); i ++) {
                String genre = genresJson.getJSONObject(i).getString("name");
                genres.add(genre);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        movie.setCountries(countries);
        movie.setGenres(genres);

        return movie;
    }

}
