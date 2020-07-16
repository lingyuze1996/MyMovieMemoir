package ling.yuze.mymoviememoir;

import android.app.Application;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;

public class MovieMemoirApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("App", "Amplify Initialized");
        } catch(AmplifyException e) {
            Log.e("App", "Amplify Initialization Error", e);
        }
    }
}
