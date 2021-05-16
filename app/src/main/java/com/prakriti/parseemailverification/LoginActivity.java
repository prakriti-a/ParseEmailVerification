package com.prakriti.parseemailverification;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AppCompatActivity {

    private EditText edtLoginUsername, edtLoginPassword;
    private Button btnGoToSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Login Page");

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);

        btnGoToSignup = findViewById(R.id.btnGoToSignup);
        btnGoToSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    public void userLoggedIn(View v) {
        try {
            ParseUser.logInInBackground(edtLoginUsername.getText().toString(), edtLoginPassword.getText().toString(),
                    new LogInCallback() { // we will be notified post login
                        @Override
                        public void done(ParseUser user, ParseException e) {
                            if(user!=null) {
                                if(user.getBoolean("emailVerified")) // if true
                                {
                                    alertDisplayer("Login Successful!", "Welcome " + edtLoginUsername.getText().toString() + "!",
                                            false);
                                }
                                else {
                                    ParseUser.logOut();
                                    alertDisplayer("Login failed", "Please verify your email first", true);
                                }
                        }
                            else {
                                ParseUser.logOut();
                                alertDisplayer("Login failed", e.getMessage() + "\nPlease try again", true);
                            }
                    }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void alertDisplayer(String title, String message, final boolean error) {

        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
//                        if(!error) {
//                            Intent intent = new Intent(LoginActivity.this, LoginActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
                    }
                });
        builder.create().show();
    }
}