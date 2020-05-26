package ling.yuze.mymoviememoir.data;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private int id;
    private String name;
    private String releaseDate;
    private float rating;
    private String overview;
    private String imagePath;

    public Movie(int mId, String mName, String mReleaseDate, String mImagePath, String mOverview, float mRating) {
        id = mId;
        name = mName;
        releaseDate = mReleaseDate;
        imagePath = mImagePath;
        overview = mOverview;
        rating = mRating;
    }

    public Movie(String mName, String mReleaseDate, float mRating) {
        name = mName;
        releaseDate = mReleaseDate;
        rating = mRating;
    }

    public int getId() { return id; }
    public String getName() {return name;}
    public String getReleaseDate() {return releaseDate;}
    public float getRating() {return rating;}
    public String getImagePath() {return imagePath;}
    public String getOverview() { return overview; }

    public static List<Movie> createMovieList() {
        List<Movie> movieList = new ArrayList<>();
        return movieList;
    }
}
