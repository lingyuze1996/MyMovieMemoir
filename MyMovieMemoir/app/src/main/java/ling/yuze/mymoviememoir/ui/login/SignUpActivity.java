package ling.yuze.mymoviememoir.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
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
import ling.yuze.mymoviememoir.data.Credentials;
import ling.yuze.mymoviememoir.data.Person;
import ling.yuze.mymoviememoir.utility.DateFormat;
import ling.yuze.mymoviememoir.utility.Encryption;
import ling.yuze.mymoviememoir.utility.JsonParser;
import ling.yuze.mymoviememoir.utility.Validation;
import ling.yuze.mymoviememoir.network.RestService;

public class SignUpActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {
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

    private String firstName = "";
    private String lastName = "";
    private String gender = "";
    private int[] dob = new int[3];
    private String address = "";
    private String postcodeString = "";
    private String state = "";
    private String username = "";
    private String password = "";

    @SuppressLint("ClickableViewAccessibility")
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
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                dob[0] = year;
                dob[1] = month + 1;
                dob[2] = dayOfMonth;
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

        etPassword.setOnTouchListener(this);
        etCheckPassword.setOnTouchListener(this);
        btSignUp = findViewById(R.id.btSignUp);
        btSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSignUp:
                if (!etPassword.getText().toString().equals(etCheckPassword.getText().toString())) {
                    Toast.makeText(this, R.string.error_passwordCheck, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                username = etUsername.getText().toString().trim();
                if (username.equals("")) {
                    Toast.makeText(this, R.string.error_username_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                password = etPassword.getText().toString();
                if (!Validation.checkLength(password, 16)) {
                    Toast.makeText(this, R.string.error_password_length, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                firstName = etFirstName.getText().toString().trim();
                if (firstName.equals("")) {
                    Toast.makeText(this, R.string.error_first_name_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                lastName = etLastName.getText().toString().trim();
                if (lastName.equals("")) {
                    Toast.makeText(this, R.string.error_last_name_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                if (gender.equals("")) {
                    Toast.makeText(this, R.string.error_gender_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                if (dob[0] == 0 && dob[1] == 0 && dob[2] == 0) {
                    Toast.makeText(this, R.string.error_dob_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                address = etAddress.getText().toString().trim();
                if (address.equals("")) {
                    Toast.makeText(this, R.string.error_address_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                postcodeString = etPostcode.getText().toString().trim();
                if (postcodeString.equals("")) {
                    Toast.makeText(this, R.string.error_postcode_empty, Toast.LENGTH_LONG)
                            .show();
                    break;
                }
                if (postcodeString.length() != 4) {
                    Toast.makeText(this, R.string.error_postcode_format, Toast.LENGTH_LONG)
                            .show();
                    break;
                }

                TaskCheckUsername taskCheckUsername = new TaskCheckUsername();
                taskCheckUsername.execute(username);
                break;

            case R.id.imageDate:
                if (calendar.getVisibility() == View.GONE)
                    calendar.setVisibility(View.VISIBLE);
                else
                    calendar.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.etRegisterPassword:
                alterVisibility(etPassword, event);
                break;
            case R.id.etCheckPassword:
                alterVisibility(etCheckPassword, event);
                break;
        }
        return false;
    }

    private void alterVisibility(EditText et, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Drawable drawable_right = et.getCompoundDrawables()[2];
            if (drawable_right == null)
                return;
            else {
                if (event.getRawX() >= (et.getRight() - drawable_right.getBounds().width())) {
                    if (drawable_right.getConstantState().equals
                            (getResources().getDrawable(R.drawable.ic_visibility, null).getConstantState())) {
                        et.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.drawable.ic_visibility_off, null), null);
                        et.setTransformationMethod(PasswordTransformationMethod.getInstance());;
                    }
                    else {
                        et.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                getResources().getDrawable(R.drawable.ic_visibility, null), null);
                        et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    }
                }
            }
        }
    }

    private class TaskCheckUsername extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean available = true;
            RestService rs = new RestService();
            String credentials = rs.getCredentialsByUsername(params[0]);
            if (!credentials.equals(""))
                available = false;
            return available;
        }

        @Override
        protected void onPostExecute(Boolean available) {
            if (!available) {
                Toast.makeText(getBaseContext(), R.string.error_username, Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(getBaseContext(), R.string.success_sign_up, Toast.LENGTH_LONG).show();

                String dobString = DateFormat.toCompleteDateString(dob[0], dob[1], dob[2]);
                Person person = new Person(0, firstName, lastName, gender,
                        dobString, address, state, postcodeString);

                String passwordHash = Encryption.md5_encryption(password);
                String signUpDate = DateFormat.getCurrentDate();
                Credentials credentials = new Credentials(username, signUpDate, passwordHash);

                new TaskUploadData().execute(person, credentials);
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }

        }
    }

    private class TaskUploadData extends AsyncTask<Object, Void, Void> {
        @Override
        protected Void doInBackground(Object... params) {
            //Firstly get the largest id number
            RestService rs = new RestService();
            String persons = rs.getAllPersons();
            int largestId = rs.getLargestPersonId(persons);
            int newId = largestId + 1; //set the newly arranged id
            Person person = (Person) params[0];
            person.setPId(newId);
            String jsonPerson = JsonParser.objectToJson(person);
            rs.post(jsonPerson, "person");

            Credentials credentials = (Credentials) params[1];
            credentials.setPId(newId);
            String jsonCredentials = JsonParser.objectToJson(credentials);
            rs.post(jsonCredentials, "credentials");

            return null;
        }

    }
}
