package ling.yuze.mymoviememoir.ui.main.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import java.util.ArrayList;
import java.util.List;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.Cinema;
import ling.yuze.mymoviememoir.data.Memoir;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.utility.DateFormat;
import ling.yuze.mymoviememoir.utility.JsonParser;

import static ling.yuze.mymoviememoir.network.ImageDownload.setImage;

public class AddMemoirFragment extends Fragment implements View.OnClickListener {
    private TextView tvMovieName;
    private ImageView image;
    private TextView tvRelease;
    private ImageView calendarImage;
    private CalendarView calendar;
    private ImageView timePickerImage;
    private TimePicker timePicker;
    private Spinner spinnerCinema;
    private TextView tvAddCinemaClick;
    private TextView tvAddCinemaHeading;
    private EditText etCinemaName;
    private EditText etCinemaPostcode;
    private TextView tvConfirm;
    private EditText etAddComment;
    private RatingBar ratingBar;
    private Button buttonSubmit;

    private int maxMemoirId;
    private String watchingDate;
    private int maxCinemaId = 0;
    private List<String> cinemas;
    private List<Object[]> cinemasList;
    private ArrayAdapter<String> cinemaAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_memoir, container, false);

        new TaskGetMaxMemoirId().execute();

        // Get movie information from movie view fragment
        SharedPreferences shared = getContext().getSharedPreferences("movie", Context.MODE_PRIVATE);

        String name = shared.getString("name", "Unknown");
        String releaseDate = shared.getString("releaseDate", "Unknown");
        String imagePath = shared.getString("imagePath", "");

        // initialize widgets
        tvMovieName = v.findViewById(R.id.tv_add_name);
        tvMovieName.setText(name);

        image = v.findViewById(R.id.image_add_poster);
        setImage(image, imagePath);

        tvRelease = v.findViewById(R.id.tv_add_release);
        tvRelease.setText(releaseDate);

        calendarImage = v.findViewById(R.id.imageWatchingDate);
        calendarImage.setOnClickListener(this);

        calendar = v.findViewById(R.id.calendar_watching);
        calendar.setVisibility(View.GONE);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                watchingDate = DateFormat.toDateString(year, month + 1, dayOfMonth);
            }
        });

        timePickerImage = v.findViewById(R.id.imageWatchingTime);
        timePickerImage.setOnClickListener(this);

        timePicker = v.findViewById(R.id.time_picker);
        timePicker.setVisibility(View.GONE);
        timePicker.setIs24HourView(true);

        // Spinner for cinema name and postcode
        spinnerCinema = v.findViewById(R.id.spinner_add_cinema);

        cinemas = new ArrayList<>();

        cinemaAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, cinemas);

        spinnerCinema.setAdapter(cinemaAdapter);

        // retrieve cinema names and postcodes into spinners
        new TaskGetCinemas().execute();

        // Click button to add a cinema not in the list
        tvAddCinemaClick = v.findViewById(R.id.tv_supplement_cinema);
        tvAddCinemaClick.setOnClickListener(this);

        // View of supplement cinema
        tvAddCinemaHeading = v.findViewById(R.id.tv_supplement_cinema_heading);
        etCinemaName = v.findViewById(R.id.etAddCinemaName);
        etCinemaPostcode = v.findViewById(R.id.etAddCinemaPostcode);
        tvConfirm = v.findViewById(R.id.tv_supplement_cinema_confirm);
        tvConfirm.setOnClickListener(this);
        setAddCinemaVisibility(View.GONE);

        etAddComment = v.findViewById(R.id.etAddComment);

        ratingBar = v.findViewById(R.id.ratingBar_add);

        buttonSubmit = v.findViewById(R.id.btAddSubmit);
        buttonSubmit.setOnClickListener(this);

        return v;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.imageWatchingTime:
                if (timePicker.getVisibility() == View.GONE)
                    timePicker.setVisibility(View.VISIBLE);
                else
                    timePicker.setVisibility(View.GONE);
                break;

            case R.id.imageWatchingDate:
                if (calendar.getVisibility() == View.GONE)
                    calendar.setVisibility(View.VISIBLE);
                else
                    calendar.setVisibility(View.GONE);
                break;

            case R.id.tv_supplement_cinema:
                if (tvAddCinemaHeading.getVisibility() == View.GONE)
                    setAddCinemaVisibility(View.VISIBLE);
                else
                    setAddCinemaVisibility(View.GONE);
                break;

            case R.id.tv_supplement_cinema_confirm:
                String newCinemaName = etCinemaName.getText().toString();
                String newCinemaPostcode = etCinemaPostcode.getText().toString();

                if (newCinemaPostcode.length() != 4) {
                    Toast.makeText(getContext(), R.string.error_postcode_format, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                if (newCinemaName.length() == 0) {
                    Toast.makeText(getContext(), R.string.error_cinema_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                setAddCinemaVisibility(View.GONE);

                // check whether the cinema is already provided
                if (checkCinemaId(newCinemaName + " " + newCinemaPostcode) != 0) {
                    Toast.makeText(getContext(), R.string.error_cinema_exist, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                String newCinema = newCinemaName + " " + newCinemaPostcode;
                cinemas.add(newCinema);

                cinemasList.add(new Object[]{++ maxCinemaId, newCinemaPostcode, newCinemaName});

                spinnerCinema.setSelection(cinemaAdapter.getPosition(newCinema));

                // post newly added cinema to server database
                new TaskPostNewCinema().execute(newCinemaName, newCinemaPostcode);
                Toast.makeText(getContext(), R.string.success_add_cinema, Toast.LENGTH_LONG)
                        .show();
                break;

            case R.id.btAddSubmit:
                if (watchingDate == null) {
                    Toast.makeText(getContext(), R.string.error_date_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                String watchingDateTime = watchingDate
                        + DateFormat.toCompleteTimeString(hour, minute);
                String comment = etAddComment.getText().toString();
                float rating = ratingBar.getRating();

                Memoir memoir = new Memoir(tvMovieName.getText().toString(), tvRelease.getText().toString() + "T00:00:00+10:00",
                        watchingDateTime, comment, rating);

                String cinemaSelected = spinnerCinema.getSelectedItem().toString();

                int cinemaId = checkCinemaId(cinemaSelected);

                memoir.setCId(cinemaId);

                SharedPreferences sharedPersonInfo = getContext().
                        getSharedPreferences("Info", Context.MODE_PRIVATE);

                int personId = sharedPersonInfo.getInt("id", 0);
                memoir.setPId(personId);

                // post memoir into server database
                new TaskPostNewMemoir().execute(memoir);
                Toast.makeText(getContext(), R.string.success_add_memoir, Toast.LENGTH_LONG).show();

                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.content_frame, new MovieMemoirFragment());
                transaction.addToBackStack(null);
                transaction.commit();

                break;
        }
    }

    private int checkCinemaId(String cString) {
        for (Object[] cinema : cinemasList) {
            String cinemaString = (String) cinema[2] + " " + (String) cinema[1];
            if (cinemaString.equals(cString)) {
                return (Integer) cinema[0];
            }
        }

        return 0;
    }

    private void setAddCinemaVisibility(int visibilityId) {
        tvAddCinemaHeading.setVisibility(visibilityId);
        etCinemaName.setVisibility(visibilityId);
        etCinemaPostcode.setVisibility(visibilityId);
        tvConfirm.setVisibility(visibilityId);
    }

    private class TaskPostNewCinema extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            RestService rs = new RestService();
            Cinema cinema = new Cinema(maxCinemaId, params[0], params[1]);
            String cinemaJson = JsonParser.objectToJson(cinema);
            rs.post(cinemaJson, "cinema");

            return null;
        }
    }

    private class TaskPostNewMemoir extends AsyncTask<Memoir, Void, Void> {
        @Override
        protected Void doInBackground(Memoir... memoirs) {
            RestService rs = new RestService();
            Memoir memoir = memoirs[0];
            memoir.setId(++ maxMemoirId);
            String memoirJson = JsonParser.objectToJson(memoir);
            rs.post(memoirJson, "memoir");

            return null;
        }
    }

    private class TaskGetCinemas extends AsyncTask<Void, Void, List<Object[]>> {
        @Override
        protected List<Object[]> doInBackground(Void... voids) {
            List<Object[]> cinemaList = new ArrayList<>();
            RestService rs = new RestService();
            cinemaList = rs.getAllCinemas();
            return cinemaList;
        }

        @Override
        protected void onPostExecute(List<Object[]> cinemaList) {
            cinemasList = cinemaList;
            for (Object[] cinema : cinemaList) {
                String name = (String) cinema[2];
                String postcode = (String) cinema[1];
                int id = (Integer) cinema[0];

                cinemas.add(name + " " + postcode);
            }

            cinemaAdapter.notifyDataSetChanged();

            for (Object[] cinema : cinemaList) {
                int id = (Integer) cinema[0];
                if (id > maxCinemaId)
                    maxCinemaId = id;
            }
        }
    }

    private class TaskGetMaxMemoirId extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            RestService rs = new RestService();
            return rs.getMaxMemoirId();
        }

        @Override
        protected void onPostExecute(Integer integer) {
            maxMemoirId = integer;
        }
    }
}
