package ling.yuze.mymoviememoir.data.room.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import org.jetbrains.annotations.NotNull;

@Entity (primaryKeys = {"movieName", "releaseDate"})
public class MovieToWatch {
    @ColumnInfo(name = "movieName")
    @NotNull
    public String movieName;
    @ColumnInfo(name = "releaseDate")
    @NotNull
    public String releaseDate;
    @ColumnInfo(name = "timeAdded")
    public String timeAdded;

    public MovieToWatch(@NotNull String movieName, @NotNull String releaseDate, String timeAdded) {
        this.releaseDate = releaseDate;
        this.movieName = movieName;
        this.timeAdded = timeAdded;
    }

    public String getTimeAdded() {
        return timeAdded;
    }

    @NotNull
    public String getMovieName() {
        return movieName;
    }

    @NotNull
    public String getReleaseDate() {
        return releaseDate;
    }

    public void setTimeAdded(String timeAdded) {
        this.timeAdded = timeAdded;
    }

    public void setMovieName(@NotNull String movieName) {
        this.movieName = movieName;
    }

    public void setReleaseDate(@NotNull String releaseDate) {
        this.releaseDate = releaseDate;
    }
}
