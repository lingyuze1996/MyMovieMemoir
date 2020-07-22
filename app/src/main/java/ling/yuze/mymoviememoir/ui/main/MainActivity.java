package ling.yuze.mymoviememoir.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.User;
import ling.yuze.mymoviememoir.data.viewModel.MovieToWatchViewModel;
import ling.yuze.mymoviememoir.data.viewModel.UserViewModel;
import ling.yuze.mymoviememoir.network.AWS;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.ui.main.fragment.HomeFragment;
import ling.yuze.mymoviememoir.ui.main.fragment.MapFragment;
import ling.yuze.mymoviememoir.ui.main.fragment.MovieMemoirFragment;
import ling.yuze.mymoviememoir.ui.main.fragment.MovieSearchFragment;
import ling.yuze.mymoviememoir.ui.main.fragment.ReportFragment;
import ling.yuze.mymoviememoir.ui.main.fragment.SettingsFragment;
import ling.yuze.mymoviememoir.ui.main.fragment.WatchlistFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences shared;
    private MovieToWatchViewModel viewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize local room database view model
        viewModel = new ViewModelProvider(this).get(MovieToWatchViewModel.class);
        viewModel.initializeVars(getApplication());

        // initialize user view model to share user's information within main activity
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

        // get username passed from login page
        String username = getIntent().getStringExtra("username");

        // retrieve personal information details
        new TaskGetUserInfo().execute(username);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this,
                drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
    }


    private void replaceFragment(Fragment next) {
        drawerLayout.closeDrawer(GravityCompat.START);
        FragmentManager fm = getSupportFragmentManager();
        try {
            String currentFragment = fm.findFragmentById(R.id.content_frame).getClass().toString();
            String nextFragment = next.getClass().toString();

            // don't replace if on the same fragment
            if (nextFragment.equals(currentFragment))
                return;
        } catch (NullPointerException e) {}

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, next);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_item_home:
                replaceFragment(new HomeFragment());
                break;
            case R.id.menu_item_movie_search:
                replaceFragment(new MovieSearchFragment());
                break;
            case R.id.menu_item_map:
                replaceFragment(new MapFragment());
                break;
            case R.id.menu_item_report:
                replaceFragment(new ReportFragment());
                break;
            case R.id.menu_item_watchlist:
                replaceFragment(new WatchlistFragment());
                break;
            case R.id.menu_item_movie_memoir:
                replaceFragment(new MovieMemoirFragment());
                break;
            case R.id.menu_item_settings:
                replaceFragment(new SettingsFragment());
                break;
        }
        return true;
    }

    private class TaskGetUserInfo extends AsyncTask<String, Void, User> {
        @Override
        protected User doInBackground(String... params) {
            AWS aws = new AWS();
            return aws.getUserInfo(params[0]);
        }

        @Override
        protected void onPostExecute(User user) {
            userViewModel.setUser(user);

            Toast.makeText(getBaseContext(), "Welcome, " + user.getFirstName() + "!", Toast.LENGTH_LONG).show();
            TextView tv = findViewById(R.id.nav_header_text);
            String name = user.getFirstName() + " " + user.getSurname();
            tv.setText(name);

            //automatically redirect to home page after passing the information
            replaceFragment(new HomeFragment());

        }
    }

    private class TaskGetInfo extends AsyncTask<String, Void, Object[]> {
        @Override
        protected Object[] doInBackground(String... params) {
            RestService rs = new RestService();
            String credentials = rs.getCredentialsByUsername(params[0]);
            String firstName = rs.getFirstNameFromCredentials(credentials);
            String lastName = rs.getLastNameFromCredentials(credentials);
            Integer id = rs.getIdFromCredentials(credentials);
            Object[] info = {firstName, lastName, id};
            return info;
        }

        @Override
        protected void onPostExecute(Object[] info) {
            if (!info[0].equals("")) {


                // share personal information among all the fragments under main activity
                shared = getBaseContext().getSharedPreferences("Info", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putInt("id", (int) info[2]);
                editor.putString("firstName", (String) info[0]);
                editor.putString("lastName", (String) info[1]);
                editor.apply();


            }
        }
    }
}
