package ling.yuze.mymoviememoir.data.viewmodel;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ling.yuze.mymoviememoir.data.room.entity.MovieToWatch;
import ling.yuze.mymoviememoir.data.room.repository.MovieToWatchRepository;

public class MovieToWatchViewModel extends ViewModel {
    private MovieToWatchRepository repository;
    private MutableLiveData<List<MovieToWatch>> allMoviesToWatch;
    public MovieToWatchViewModel () {
        allMoviesToWatch = new MutableLiveData<>();
    }
    public void setAllMoviesToWatch(List<MovieToWatch> moviesToWatch) {
        allMoviesToWatch.setValue(moviesToWatch);
    }
    public LiveData<List<MovieToWatch>> getAllMoviesToWatch() {
        return repository.getMoviesToWatch();
    }
    public void initializeVars(Application application){
        repository = new MovieToWatchRepository(application);
    }
    public void insert(MovieToWatch movieToWatch) {
        repository.insert(movieToWatch);
    }
    public void insertAll(MovieToWatch... moviesToWatch) {
        repository.insertAll(moviesToWatch);
    }
    public void deleteAll() {
        repository.deleteAll();
    }
    public void delete(MovieToWatch movieToWatch) {
        repository.delete(movieToWatch);
    }
    public void update(MovieToWatch... moviesToWatch) {
        repository.updateMoviesToWatch(moviesToWatch);
    }
    public MovieToWatch findByID(String name, String release){
        return repository.findByID(name, release);
    }
}
