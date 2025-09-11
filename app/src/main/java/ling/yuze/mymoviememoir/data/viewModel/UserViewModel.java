package ling.yuze.mymoviememoir.data.viewModel;

import android.os.Handler;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ling.yuze.mymoviememoir.data.User;
import ling.yuze.mymoviememoir.network.AWS;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> user;
    private Handler handler = new Handler();

    public UserViewModel() {
    }

    public MutableLiveData<User> getUser() {
        if (user == null)
            loadUser();
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }

    private void loadUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AWS aws = new AWS();
                final User userInfo = aws.getUserInfo(user.getValue().getUsername());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        setUser(userInfo);
                    }
                });
            }
        });


    }
}
