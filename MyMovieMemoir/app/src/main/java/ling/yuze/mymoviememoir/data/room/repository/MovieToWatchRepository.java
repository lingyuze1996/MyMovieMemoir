package ling.yuze.mymoviememoir.data.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import ling.yuze.mymoviememoir.data.room.dao.MovieToWatchDAO;
import ling.yuze.mymoviememoir.data.room.database.MovieToWatchDatabase;
import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;

public class MovieToWatchRepository {
    private MovieToWatchDAO dao;
    private MovieToWatch movieToWatch;
    private LiveData<List<MovieToWatch>> moviesToWatch;

    public MovieToWatchRepository(Application application) {
        MovieToWatchDatabase db = MovieToWatchDatabase.getInstance(application);
        dao = db.movieToWatchDao();
    }

    public LiveData<List<MovieToWatch>> getMoviesToWatch() {
        moviesToWatch = dao.getAll();
        return moviesToWatch;
    }

    public void insert (final MovieToWatch m) {
        MovieToWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insert(m);
            }
        });
    }

    public void deleteAll(){
        MovieToWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }

    public void delete(final MovieToWatch m){
        MovieToWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.delete(m);
            }
        });
    }
    public void insertAll(final MovieToWatch... moviesToWatch){
        MovieToWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAll(moviesToWatch);
            }
        });
    }

    public void updateMoviesToWatch(final MovieToWatch... MoviesToWatch){
        MovieToWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.updateMoviesToWatch(MoviesToWatch);
            }
        });
    }
    public MovieToWatch findByID(final String name, final String release){
        MovieToWatchDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                MovieToWatch runMovieToWatch= dao.findByID(name, release);
                setMovieToWatch(runMovieToWatch);
            }
        });
        return movieToWatch;
    }

    public void setMovieToWatch(MovieToWatch m){
        this.movieToWatch = m;
    }

}
