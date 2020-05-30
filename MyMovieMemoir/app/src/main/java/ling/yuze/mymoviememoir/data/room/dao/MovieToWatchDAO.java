package ling.yuze.mymoviememoir.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MovieToWatchDAO {
    @Query("SELECT * FROM MOVIETOWATCH")
    LiveData<List<MovieToWatch>> getAll();
    @Query("SELECT * FROM MOVIETOWATCH WHERE movieName = :mName AND releaseDate = :mRelease LIMIT 1")
    MovieToWatch findByID(String mName, String mRelease);
    @Insert
    void insertAll(MovieToWatch... moviesToWatch);
    @Insert
    long insert(MovieToWatch movieToWatch);
    @Delete
    void delete(MovieToWatch movieToWatch);
    @Update(onConflict = REPLACE)
    void updateMoviesToWatch(MovieToWatch... moviesToWatch);
    @Query("DELETE FROM MovieToWatch")
    void deleteAll();
}
