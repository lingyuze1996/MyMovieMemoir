package ling.yuze.mymoviememoir.data.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ling.yuze.mymoviememoir.data.Cinema;

public class CinemaViewModel extends ViewModel {
    private MutableLiveData<Cinema> cinema;

    public CinemaViewModel() {
        cinema = new MutableLiveData<>();
    }

    public MutableLiveData<Cinema> getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema.setValue(cinema);
    }
}
