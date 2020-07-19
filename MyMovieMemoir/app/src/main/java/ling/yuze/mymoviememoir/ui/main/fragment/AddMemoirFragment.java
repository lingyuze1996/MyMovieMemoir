package ling.yuze.mymoviememoir.ui.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.data.Movie;
import ling.yuze.mymoviememoir.data.User;
import ling.yuze.mymoviememoir.network.AWS;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.utility.DateFormat;
import ling.yuze.mymoviememoir.utility.JsonParser;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class AddMemoirFragment extends Fragment implements View.OnClickListener {

    // widgets
    private TextView tvMovieName;
    private ImageView movieImage;
    private TextView tvRelease;

    private ImageView calendarImage;
    private CalendarView calendar;
    private TextView tvDate;

    private ImageView timePickerImage;
    private TimePicker timePicker;
    private TextView tvTime;

    private ImageView cinemaImage;
    private TextView tvCinema;

    private EditText etAddComment;

    private RatingBar ratingBar;

    private Button buttonSubmit;

    // information

    private String watchingDate;
    private String watchingTime;
    private Cinema cinema;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_memoir, container, false);

        //new TaskGetMaxMemoirId().execute();

        // Get movie information from movie view fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);

        String name = shared.getString("name", "Unknown");
        String releaseDate = shared.getString("releaseDate", "Unknown");
        String imagePath = shared.getString("imagePath", "");

        // initialize widgets
        tvMovieName = v.findViewById(R.id.tv_add_name);
        tvMovieName.setText(name);

        movieImage = v.findViewById(R.id.image_add_poster);
        setImage(movieImage, imagePath);

        tvRelease = v.findViewById(R.id.tv_add_release);
        tvRelease.setText(releaseDate);

        calendarImage = v.findViewById(R.id.imageWatchingDate);
        calendarImage.setOnClickListener(this);

        tvDate = v.findViewById(R.id.tv_add_date);

        calendar = v.findViewById(R.id.calendar_watching);
        calendar.setVisibility(View.GONE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                watchingDate = DateFormat.toDateString(year, month + 1, dayOfMonth);
                tvDate.setText(watchingDate);
            }
        });

        timePickerImage = v.findViewById(R.id.imageWatchingTime);
        timePickerImage.setOnClickListener(this);

        tvTime = v.findViewById(R.id.tv_add_time);

        timePicker = v.findViewById(R.id.time_picker);
        timePicker.setVisibility(View.GONE);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                watchingTime = DateFormat.toTimeString(hourOfDay, minute);
                tvTime.setText(watchingTime);
            }
        });

        cinemaImage = v.findViewById(R.id.image_cinema);
        cinemaImage.setOnClickListener(this);

        tvCinema = v.findViewById(R.id.tv_cinema_chosen);

        etAddComment = v.findViewById(R.id.etAddComment);

        ratingBar = v.findViewById(R.id.ratingBar_add);

        buttonSubmit = v.findViewById(R.id.btAddSubmit);
        buttonSubmit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageWatchingDate:
                if (calendar.getVisibility() == View.GONE)
                    calendar.setVisibility(View.VISIBLE);
                else
                    calendar.setVisibility(View.GONE);
                break;

            case R.id.imageWatchingTime:
                if (timePicker.getVisibility() == View.GONE)
                    timePicker.setVisibility(View.VISIBLE);
                else
                    timePicker.setVisibility(View.GONE);
                break;

            case R.id.image_cinema:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();

                CinemaChooseFragment fragment = new CinemaChooseFragment();
                fragment.setTargetFragment(this, 0);

                transaction.add(R.id.content_frame, fragment);
                transaction.hide(this);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case R.id.btAddSubmit:
                if (watchingDate == null) {
                    Toast.makeText(getContext(), R.string.error_date_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                String comment = etAddComment.getText().toString();
                float rating = ratingBar.getRating();

                Memoir memoir = new Memoir(new User("testuser"),
                        new Cinema("test", "test", "test", "test", "test"),
                        new Movie("test"),
                        watchingDate + " " + watchingTime, rating, comment);

                //SharedPreferences sharedPersonInfo = getContext().
                //        getSharedPreferences("Info", Context.MODE_PRIVATE);

                //int personId = sharedPersonInfo.getInt("id", 0);

                // post memoir into server database
                new TaskPostNewMemoir().execute(memoir);
                Toast.makeText(getContext(), R.string.success_add_memoir, Toast.LENGTH_LONG).show();

                replaceFragment(new MovieMemoirFragment());

                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            tvCinema.setText(data.getStringExtra("cinemaChosen"));
            tvCinema.setVisibility(View.VISIBLE);
        }

    }

    private void replaceFragment(Fragment next) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.content_frame, next);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private class TaskPostNewMemoir extends AsyncTask<Memoir, Void, Void> {
        @Override
        protected Void doInBackground(Memoir... memoirs) {
            AWS aws = new AWS();
            Memoir memoir = memoirs[0];
            aws.postMemoir(memoir);
            return null;
        }
    }
}
