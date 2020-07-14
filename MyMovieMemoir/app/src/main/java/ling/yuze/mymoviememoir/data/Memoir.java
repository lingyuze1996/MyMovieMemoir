package ling.yuze.mymoviememoir.data;

public class Memoir {
    private int MId;
    private String MComment;
    private String MMovieName;
    private String MMovieReleaseDate;
    private String MWatchingDatetime;
    private float MRating;
    private Person PId;
    private Cinema CId;

    public Memoir(String name, String release, String time, String comment, float rating) {
        MMovieName = name;
        MMovieReleaseDate = release;
        MWatchingDatetime = time;
        MComment = comment;
        MRating = rating;
    }

    public void setPId(int id) {
        PId = new Person(id);
    }
    //public void setCId(int id) { CId = new Cinema(id); }
    public void setId(int id) { MId = id; }
}


