package ling.yuze.mymoviememoir.data.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class MovieToWatch {
    @PrimaryKey(autoGenerate = true)
    public int mid;
    @ColumnInfo(name = "movie_name")
    public String movieName;
    @ColumnInfo(name = "release_date")
    public String release;
    @ColumnInfo(name = "time_added")
    public String timeAdded;

    public MovieToWatch(String name, String releaseDate, String time){
        movieName = name;
        release = releaseDate;
        timeAdded = time;
    }

    public String getRelease() {
        return release;
    }

    public int getMid() {
        return mid;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }
}
