package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import java.io.InputStream;

import ling.yuze.mymoviememoir.R;

public class MovieViewFragment extends Fragment implements View.OnClickListener {
    private TextView tvCast;
    private TextView tvGenre;
    private TextView tvCountry;
    private TextView tvDirector;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view, container, false);

        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);

        int movieId = shared.getInt("id", -1);
        String releaseDate = shared.getString("releaseDate", "Unknown");
        String overview = shared.getString("overview", "Unknown");
        float rating = shared.getFloat("rating", 0);
        //Toast.makeText(getContext(), overview, Toast.LENGTH_LONG).show();

        //Button addToMemoir = v.findViewById()




        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            //case
        }
    }

    public String readFile(int resourceId) {
        String content = "";
        try {
            InputStream inputStream = getResources().openRawResource(resourceId);
            while (inputStream)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
