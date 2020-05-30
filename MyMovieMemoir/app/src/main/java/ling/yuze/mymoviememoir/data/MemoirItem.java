package ling.yuze.mymoviememoir.data;

import java.util.ArrayList;
import java.util.List;

public class MemoirItem {
    private Movie movie;
    private String watching;
    private String suburb;
    private String comment;
    private float myRating;

    public MemoirItem() {}

    public MemoirItem(String mName, String mRelease, String mWatching,
                      String mSuburb, String mComment, float mMyRating) {
        movie = new Movie();
        movie.setName(mName);
        movie.setReleaseDate(mRelease);

        watching = mWatching;
        suburb = mSuburb;
        myRating = mMyRating;
        comment = mComment;
    }

    public float getMyRating() {
        return myRating;
    }

    public String getSuburb() {
        return suburb;
    }

    public String getComment() {
        return comment;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getWatching() { return watching; }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setMyRating(float myRating) {
        this.myRating = myRating;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public void setWatching(String watching) {
        this.watching = watching;
    }

    public static List<MemoirItem> createList() {
        List<MemoirItem> list = new ArrayList<>();
        return list;
    }
}
