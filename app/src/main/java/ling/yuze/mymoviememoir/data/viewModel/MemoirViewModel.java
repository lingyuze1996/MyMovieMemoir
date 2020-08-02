package ling.yuze.mymoviememoir.data.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.Movie;

public class MemoirViewModel extends ViewModel {
    private MutableLiveData<List<Memoir>> memoirs;

    public MemoirViewModel() {
        memoirs = new MutableLiveData<>();
    }

    public List<Memoir> getMemoirs() {
        return memoirs.getValue();
    }

    public void setMemoirs(List<Memoir> memoirs) {
        this.memoirs.setValue(memoirs);
    }
}
