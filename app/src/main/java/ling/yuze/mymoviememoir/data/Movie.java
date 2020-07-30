package ling.yuze.mymoviememoir.data;

import java.util.ArrayList;
import java.util.List;

public class Movie {
    private Integer id;
    private String name;
    private String releaseDate;
    private Float publicRating;
    private String overview;
    private String imagePath;
    private List<String> genres;
    private List<String> cast;
    private List<String> countries;
    private List<String> directors;
    private boolean inWatchlist = false;

    public Movie() {
    }

    public Movie(String name) {
        this.name = name;
    }

    public Movie(int mId, String mName, String mReleaseDate, String mImagePath, String mOverview, float mRating) {
        id = mId;
        name = mName;
        releaseDate = mReleaseDate;
        imagePath = mImagePath;
        overview = mOverview;
        publicRating = mRating;
    }

    public Movie(String mName, String mReleaseDate, float mRating) {
        name = mName;
        releaseDate = mReleaseDate;
        publicRating = mRating;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getReleaseYear() {
        if (releaseDate != null && !releaseDate.isEmpty())
            return releaseDate.substring(0, 4);
        return null;
    }

    public float getPublicRating() {
        return publicRating;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getOverview() {
        return overview;
    }

    public List<String> getCountries() {
        return countries;
    }

    public List<String> getGenres() {
        return genres;
    }

    public List<String> getCast() {
        return cast;
    }

    public List<String> getDirectors() {
        return directors;
    }

    public boolean isInWatchlist() {
        return inWatchlist;
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

    public void setPublicRating(float publicRating) {
        this.publicRating = publicRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public void setCast(List<String> cast) {
        this.cast = cast;
    }

    public void setDirectors(List<String> directors) {
        this.directors = directors;
    }

    public static List<Movie> createMovieList() {
        List<Movie> movieList = new ArrayList<>();
        return movieList;
    }

    public void setInWatchlist(boolean inWatchlist) {
        this.inWatchlist = inWatchlist;
    }
}
