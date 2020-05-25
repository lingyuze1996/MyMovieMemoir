package ling.yuze.mymoviememoir.data.viewmodel;

import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> firstName;
    private MutableLiveData<Integer> id;

    public SharedViewModel() {
        firstName = new MutableLiveData<>();
        id = new MutableLiveData<>();
    }

    public LiveData<String> getFirstName() {
        return firstName;
    }

    public LiveData<Integer> getId() {
        return id;
    }

    public void setFirstName(String newFirstName) {
        firstName.setValue(newFirstName);
    }

    public void setId(int newId) {
        id.setValue(newId);
    }
}
