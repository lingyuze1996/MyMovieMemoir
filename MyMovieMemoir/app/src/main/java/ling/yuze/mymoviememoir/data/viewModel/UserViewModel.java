package ling.yuze.mymoviememoir.data.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ling.yuze.mymoviememoir.data.User;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;

    public UserViewModel() {
        user = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }
}
