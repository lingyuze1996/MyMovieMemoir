package ling.yuze.mymoviememoir.data.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ling.yuze.mymoviememoir.data.Memoir;

public class MemoirViewModel extends ViewModel {
    private MutableLiveData<List<Memoir>> memoirsAll;
    private MutableLiveData<List<Memoir>> memoirsForDisplay;
    private MutableLiveData<List<String>> genres;


    public MemoirViewModel() {
        memoirsAll = new MutableLiveData<>();
        memoirsForDisplay = new MutableLiveData<>();
        genres = new MutableLiveData<>();
    }

    public MutableLiveData<List<Memoir>> getMemoirsAll() {
        return memoirsAll;
    }

    public MutableLiveData<List<Memoir>> getMemoirsForDisplay() {
        return memoirsForDisplay;
    }

    public MutableLiveData<List<String>> getGenres() {
        return genres;
    }

    public void setMemoirsAll(List<Memoir> memoirsAll) {
        this.memoirsAll.setValue(memoirsAll);
    }

    public void setMemoirsForDisplay(List<Memoir> memoirsForDisplay) {
        this.memoirsForDisplay.setValue(memoirsForDisplay);
    }

    public void setGenres(List<String> genres) {
        this.genres.setValue(genres);
    }

    public void filterMemoirsByGenres(String genre) {
        List<Memoir> memoirsByGenres = new ArrayList<>();
        for (Memoir memoir: memoirsAll.getValue()) {
            if (memoir.getMovie().getGenres().contains(genre)) {
                memoirsByGenres.add(memoir);
            }
        }

        setMemoirsForDisplay(memoirsByGenres);
    }

    public void sortMyRatingDescending() {
        Collections.sort(memoirsForDisplay.getValue(), new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMemoirRating(), o2.getMemoirRating()) * (-1);
            }
        });

    }

    public void sortMyRatingAscending() {
        Collections.sort(memoirsForDisplay.getValue(), new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMemoirRating(), o2.getMemoirRating());
            }
        });
    }

    public void sortPublicRatingDescending() {
        Collections.sort(memoirsForDisplay.getValue(), new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating()) * (-1);
            }
        });
    }

    public void sortPublicRatingAscending() {
        Collections.sort(memoirsForDisplay.getValue(), new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return Float.compare(o1.getMovie().getPublicRating(), o2.getMovie().getPublicRating());
            }
        });
    }

    public void sortWatchingDateAscending() {
        Collections.sort(memoirsForDisplay.getValue(), new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return o1.getMemoirTime().compareTo(o2.getMemoirTime());
            }
        });
    }

    public void sortWatchingDateDescending() {
        Collections.sort(memoirsForDisplay.getValue(), new Comparator<Memoir>() {
            @Override
            public int compare(Memoir o1, Memoir o2) {
                return o1.getMemoirTime().compareTo(o2.getMemoirTime()) * (-1);
            }
        });
    }



}
