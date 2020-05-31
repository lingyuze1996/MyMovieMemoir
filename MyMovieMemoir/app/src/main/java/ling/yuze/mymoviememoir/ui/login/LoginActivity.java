package ling.yuze.mymoviememoir.ui.login;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.utility.Encryption;
import ling.yuze.mymoviememoir.network.RestService;
import ling.yuze.mymoviememoir.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private Button btSignIn;
    private TextView tvSignUp;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etPassword = findViewById(R.id.etLoginPassword);
        etUsername = findViewById(R.id.etRegisterUsername);
        btSignIn = findViewById(R.id.btSignIn);
        tvSignUp = findViewById(R.id.tv_sign_up);

        btSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

        // Hide password by default
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        // switch hide/show when touching the icon
        etPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Drawable drawable_right = etPassword.getCompoundDrawables()[2];
                    if (drawable_right == null)
                        return false;
                    else {
                        if (event.getRawX() >= (etPassword.getRight() - drawable_right.getBounds().width())) {
                            if (drawable_right.getConstantState().equals
                                    (getResources().getDrawable(R.drawable.ic_visibility, null).getConstantState())) {
                                etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                        getResources().getDrawable(R.drawable.ic_visibility_off, null), null);
                                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());;
                            }
                            else {
                                etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                        getResources().getDrawable(R.drawable.ic_visibility, null), null);
                                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            }
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btSignIn:
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                // encrypt password to hash
                String passwordHash = Encryption.md5_encryption(password);

                // Check whether password hash matches
                new TaskGetPassword().execute(username, passwordHash);

                break;
            case R.id.tv_sign_up:
                // redirect to sign up screen
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
        }
    }

    private class TaskGetPassword extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            RestService rs = new RestService();
            String credentials = rs.getCredentialsByUsername(params[0]);
            String passwordRecord = rs.getPasswordByUsername(credentials);
            if (passwordRecord.equals(params[1])) {
                return params[0]; // password hash matches
            }
            return ""; // password hash not match
        }

        @Override
        protected void onPostExecute(String s) {
            // password doesn't match username
            if (s.equals(""))
                Toast.makeText(getBaseContext(), R.string.error_sign_in, Toast.LENGTH_LONG).show();

            // password matches username, proceed
            else {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("username", s);
                startActivity(intent);
            }
        }
    }

}
