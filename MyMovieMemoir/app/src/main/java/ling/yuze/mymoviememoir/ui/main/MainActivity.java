package ling.yuze.mymoviememoir.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.ui.main.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String welcomeString = "";
        String username = getIntent().getStringExtra("username");


        Toast.makeText(this, welcomeString, Toast.LENGTH_LONG).show();

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.nav_view);
       //Toolbar toolbar = findViewById(R.id.toolbar);


        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }
    }


    private void replaceFragment(Fragment next) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        //transaction.replace(R.id.content_frame, next).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private class getFirstName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            RestService rs = new RestService();
            rs.getFirstNameByUsername();
        }
    }
}
