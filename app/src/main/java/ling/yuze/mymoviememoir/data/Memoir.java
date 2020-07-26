package ling.yuze.mymoviememoir.data;

public class Memoir {
    private User user;
    private Cinema cinema;
    private Movie movie;
    private String memoirTime;
    private float rating;
    private String comment;

    public Memoir(User user, Cinema cinema, Movie movie, String watchDateTime, float rating, String comment) {
        this.user = user;
        this.cinema = cinema;
        this.movie = movie;
        this.memoirTime = watchDateTime;
        this.rating = rating;
        this.comment = comment;
    }
}


