package ling.yuze.mymoviememoir.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.User;
import ling.yuze.mymoviememoir.network.AWS;
import ling.yuze.mymoviememoir.utility.DateFormat;
import ling.yuze.mymoviememoir.utility.Validation;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etFirstName;
    private EditText etLastName;
    private RadioGroup radioGroup;
    private ImageView dateImage;
    private CalendarView calendar;
    private EditText etAddress;
    private Spinner spinnerState;
    private EditText etPostcode;
    private EditText etUsername;
    private EditText etPassword;
    private EditText etCheckPassword;
    private Button btSignUp;

    private String firstName;
    private String surname;
    private String gender;
    private String dob;
    private String address;
    private String postcode;
    private String state;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etFirstName = findViewById(R.id.etRegisterFirstName);
        etLastName = findViewById(R.id.etRegisterLastName);
        radioGroup = findViewById(R.id.radio_group_gender);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.bt_radio_female:
                        gender = "F";
                        break;
                    case R.id.bt_radio_male:
                        gender = "M";
                        break;
                }
            }
        });

        dateImage = findViewById(R.id.imageDate);
        dateImage.setOnClickListener(this);

        calendar = findViewById(R.id.calendar);
        calendar.setVisibility(View.GONE);

        // automatically store dob into a String
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dob = DateFormat.toDateString(year, month + 1, dayOfMonth);
            }
        });

        etAddress = findViewById(R.id.etRegisterAddress);
        etPostcode = findViewById(R.id.etRegisterPostcode);

        spinnerState = findViewById(R.id.spinner_state);
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getSelectedItem().toString();
                state = selected;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        etUsername = findViewById(R.id.etRegisterUsername);
        etPassword = findViewById(R.id.etRegisterPassword);
        etCheckPassword = findViewById(R.id.etCheckPassword);

        btSignUp = findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSignUp:

                // check whether the password entered twice matches
                if (!etPassword.getText().toString().equals(etCheckPassword.getText().toString())) {
                    Toast.makeText(this, R.string.error_passwordCheck, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether username is empty
                username = etUsername.getText().toString().trim();
                if (username.equals("")) {
                    Toast.makeText(this, R.string.error_username_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check password length
                password = etPassword.getText().toString();
                if (!Validation.checkLength(password, 8)) {
                    Toast.makeText(this, R.string.error_password_length, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether user's first name is empty
                firstName = etFirstName.getText().toString().trim();
                if (firstName.equals("")) {
                    Toast.makeText(this, R.string.error_first_name_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether user's last name is empty
                surname = etLastName.getText().toString().trim();
                if (surname.equals("")) {
                    Toast.makeText(this, R.string.error_last_name_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether gender is empty
                if (gender == null) {
                    Toast.makeText(this, R.string.error_gender_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether dob is empty
                if (dob == null) {
                    Toast.makeText(this, R.string.error_dob_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether address is empty
                address = etAddress.getText().toString().trim();
                if (address.equals("")) {
                    Toast.makeText(this, R.string.error_address_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // check whether postcode is a four digit number
                postcode = etPostcode.getText().toString().trim();
                if (postcode.equals("")) {
                    Toast.makeText(this, R.string.error_postcode_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }
                if (postcode.length() != 4) {
                    Toast.makeText(this, R.string.error_postcode_format, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                // try sign up
                new TaskSignUp().execute();
                break;

            case R.id.imageDate:
                if (calendar.getVisibility() == View.GONE)
                    calendar.setVisibility(View.VISIBLE);
                else
                    calendar.setVisibility(View.GONE);
                break;
        }
    }

    private class TaskSignUp extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            AWS aws = new AWS();
            User user = new User(username, password, firstName, surname, gender, dob,
                    address, state, postcode);
            return aws.userSignUp(user);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (!success)
                Toast.makeText(getBaseContext(), R.string.error_username, Toast.LENGTH_LONG).show();
            else {
                Toast.makeText(getBaseContext(), R.string.success_sign_up, Toast.LENGTH_LONG).show();
                // redirect to login screen
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}
