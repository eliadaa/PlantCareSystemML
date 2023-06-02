package com.example.plantcaresystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPassword;
    private ProgressBar progressBar;

    private FirebaseAuth authProfile;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPassword = findViewById(R.id.editText_login_password);
        progressBar = findViewById(R.id.progressBar_login);

        authProfile = FirebaseAuth.getInstance();

        // Login button
        Button buttonLogin = findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() { // we assume user has entered email and password
            @Override
            public void onClick(View v) {
                String textPassword, textEmail;

                textPassword = editTextLoginPassword.getText().toString();
                textEmail = editTextLoginEmail.getText().toString();

                // check if user enter data or not
                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editTextLoginEmail.setError("Email is required!");
                    editTextLoginEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid email required!");
                    editTextLoginPassword.requestFocus();
                } else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editTextLoginPassword.setError("Password is required");
                    editTextLoginPassword.requestFocus();
                } else{
                    // progress bar visible
                    progressBar.setVisibility(View.VISIBLE);
                    // login user
                    loginUser(textEmail, textPassword);
                }

            }
        });



    }

    private void loginUser(String email, String password) {

        // on complete listener to check for the complition of the task
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                } else{

                    // handling exceptions
                    try{
                       throw  task.getException();
                    } catch (FirebaseAuthInvalidUserException exception){ // user does not exist anymore, account deleted maybe
                        editTextLoginEmail.setError("User does not exist or no longer valid. Please register");
                        editTextLoginEmail.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException exception){ // invalid credentials, email and password do not match
                        editTextLoginEmail.setError("Invalid credentials, please re-enter");
                        editTextLoginEmail.requestFocus();
                    } catch (Exception exception){ // any other exception is logged here
                        Log.e(TAG, exception.getMessage());
                        Toast.makeText(LoginActivity.this, "Something went wrong!\n" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    Toast.makeText(LoginActivity.this, "ERROR logging in!", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}