package ling.yuze.mymoviememoir.data.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ling.yuze.mymoviememoir.data.Movie;

public class MovieViewModel extends ViewModel {
    private MutableLiveData<Movie> movie;

    public MovieViewModel() {
        movie = new MutableLiveData<>();
    }

    public MutableLiveData<Movie> getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie.setValue(movie);
    }
}
