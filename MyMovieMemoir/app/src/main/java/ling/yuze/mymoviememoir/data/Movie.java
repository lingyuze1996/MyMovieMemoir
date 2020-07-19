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
    private List<String> genres;

    public Movie() {}

    public Movie(String name) {
        this.name = name;
    }

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

    public List<String> getGenres() {
        return genres;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public static List<Movie> createMovieList() {
        List<Movie> movieList = new ArrayList<>();
        return movieList;
    }
}
