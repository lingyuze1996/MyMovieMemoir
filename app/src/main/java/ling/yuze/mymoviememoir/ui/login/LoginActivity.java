package ling.yuze.mymoviememoir.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ling.yuze.mymoviememoir.R;
import ling.yuze.mymoviememoir.data.User;
import ling.yuze.mymoviememoir.network.AWS;
import ling.yuze.mymoviememoir.ui.main.MainActivity;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etUsername;
    private EditText etPassword;
    private String username;
    private String password;
    private AWS aws;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);

        etUsername.setText(getIntent().getStringExtra("username"));
        etPassword.setText(getIntent().getStringExtra("password"));

        Button btSignIn = findViewById(R.id.btSignIn);
        TextView tvSignUp = findViewById(R.id.tv_sign_up);

        btSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btSignIn:
                username = etUsername.getText().toString();
                password = etPassword.getText().toString();
                loginProgress();
                break;

            case R.id.tv_sign_up:
                // redirect to sign up screen
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
        }
    }


    private void loginProgress() {
        aws = new AWS();
        final User user = new User(username, password);
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        String ret = "";
                        try {
                            ret = aws.userSignIn(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (ret.equals("success")) {
                            ///take actions after the post
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    intent.putExtra("username", username);
                                    startActivity(intent);
                                }
                            });
                        } else {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), R.string.error_sign_in, Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                }
        ).start();

    }


}
