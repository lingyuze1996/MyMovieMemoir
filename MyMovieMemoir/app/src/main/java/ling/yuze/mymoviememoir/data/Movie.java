package ling.yuze.mymoviememoir.data;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private String name;
    private String releaseDate;
    private float rating;

    public Movie(String mName, String mReleaseDate, float mRating) {
        name = mName;
        releaseDate = mReleaseDate;
        rating = mRating;
    }

    public String getName() {return name;}
    public String getReleaseDate() {return releaseDate;}
    public float getRating() {return rating;}

    public static List<Movie> createMovieList() {
        List<Movie> movieList = new ArrayList<>();
        return movieList;
    }
}
