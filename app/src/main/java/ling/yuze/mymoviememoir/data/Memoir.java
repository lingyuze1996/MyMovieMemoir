package ling.yuze.mymoviememoir.data;

public class Memoir {
    private User user;
    private Cinema cinema;
    private Movie movie;
    private String memoirTime;
    private float memoirRating;
    private String comment;

    public Memoir(User user, Cinema cinema, Movie movie, String watchDateTime, float memoirRating, String comment) {
        this.user = user;
        this.cinema = cinema;
        this.movie = movie;
        this.memoirTime = watchDateTime;
        this.memoirRating = memoirRating;
        this.comment = comment;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public void setMemoirTime(String memoirTime) {
        this.memoirTime = memoirTime;
    }

    public void setMemoirRating(float memoirRating) {
        this.memoirRating = memoirRating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getMemoirTime() {
        return memoirTime;
    }

    public float getMemoirRating() {
        return memoirRating;
    }

    public String getComment() {
        return comment;
    }
}


