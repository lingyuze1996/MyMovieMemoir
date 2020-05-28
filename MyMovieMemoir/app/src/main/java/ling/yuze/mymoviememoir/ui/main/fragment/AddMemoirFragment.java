package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



import ling.yuze.mymoviememoir.R;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class AddMemoirFragment extends Fragment implements View.OnClickListener {
    private TextView tvMovieName;
    private ImageView image;
    private RatingBar ratingBar;
    private TextView tvRelease;
    private Button buttonAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_to_memoir, container, false);

        // Get movie information from movie view fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);

        String name = shared.getString("name", "Unknown");
        String releaseDate = shared.getString("releaseDate", "Unknown");
        String imagePath = shared.getString("imagePath", "");

        // initialize widgets
        tvMovieName = v.findViewById(R.id.tv_add_name);
        image = v.findViewById(R.id.image_add_poster);
        ratingBar = v.findViewById(R.id.ratingBar_add);
        tvRelease = v.findViewById(R.id.tv_add_release);

        buttonAdd = v.findViewById(R.id.btAddMemoir);
        buttonAdd.setOnClickListener(this);

        tvMovieName.setText(name);
        tvRelease.setText(releaseDate);
        setImage(image, imagePath);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btSignUp:

                break;
        }
    }

}
