package ling.yuze.mymoviememoir.data.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.Movie;

public class MemoirViewModel extends ViewModel {
    private MutableLiveData<List<Memoir>> memoirs;
    private MutableLiveData<List<String>> genres;

    public MemoirViewModel() {
        memoirs = new MutableLiveData<>();
        genres = new MutableLiveData<>();
    }

    public MutableLiveData<List<Memoir>> getMemoirs() {
        return memoirs;
    }

    public MutableLiveData<List<String>> getGenres() {
        return genres;
    }

    public void setMemoirs(List<Memoir> memoirs) {
        this.memoirs.setValue(memoirs);
    }

    public void setGenres(List<String> genres) {
        this.genres.setValue(genres);
    }

}
