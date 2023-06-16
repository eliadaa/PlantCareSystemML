package com.example.plantcaresystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText login_email_edit_text, login_password_edit_text;
    private ProgressBar progressBar;
    private Button buttonLogin;

    // Variables to store email and password entered by the user
    String textPassword, textEmail;

    // Firebase authentication instance
    private FirebaseAuth authProfile;

    // Tag for logging purposes
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide the title bar
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        // Initialize UI elements
        init();
        // Setup show/hide password functionality
        showHidePass();
        // Setup login button click listener
        LoginButtonListener();
    }

    // Initialize UI elements
    private void init(){
        login_email_edit_text = findViewById(R.id.editText_login_email);
        login_password_edit_text = findViewById(R.id.editText_login_password);
        progressBar = findViewById(R.id.progressBar_login);
        authProfile = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.button_login);
    }

    // Show/hide password functionality
    private void showHidePass(){
        ImageView imageViewShowHidePassword = findViewById(R.id.image_show_hide_password);
        imageViewShowHidePassword.setImageResource(R.drawable.id_hide_password);
        imageViewShowHidePassword.setOnClickListener(v -> {
            if(login_password_edit_text.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                login_password_edit_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                imageViewShowHidePassword.setImageResource(R.drawable.id_hide_password);
            } else {
                login_password_edit_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                imageViewShowHidePassword.setImageResource(R.drawable.ic_show_password);
            }
        });
    }

    // Handle login button click
    private void LoginButtonListener(){
        buttonLogin.setOnClickListener(v -> {
            // Get email and password entered by the user
            textPassword = login_password_edit_text.getText().toString();
            textEmail = login_email_edit_text.getText().toString();

            // Check if user entered email and password
            if(TextUtils.isEmpty(textEmail)){
                Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                login_email_edit_text.setError("Email is required!");
                login_email_edit_text.requestFocus();
            } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                Toast.makeText(LoginActivity.this, "Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                login_email_edit_text.setError("Valid email required!");
                login_password_edit_text.requestFocus();
            } else if(TextUtils.isEmpty(textPassword)){
                Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                login_password_edit_text.setError("Password is required");
                login_password_edit_text.requestFocus();
            } else{
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);
                // Login user
                loginUser(textEmail, textPassword);
            }
        });
    }

    // Login user with email and password
    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                    FirebaseUser currentUser = authProfile.getCurrentUser();
                    if(currentUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "You're already logged in", Toast.LENGTH_SHORT).show();
                        continueProcessGoToPlantActivity();
                    } else{
                        currentUser.sendEmailVerification();
                        showDialogEmailNotVerified();
                    }
                    // Retrieve user profile from the database
                    Databases.usersDB.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserProfile userProfile = snapshot.getValue(UserProfile.class);
                            CurrentLoggedUser.getInstance().setCurrentUserProfile(userProfile);
                            if (userProfile != null) {
                                Toast.makeText(LoginActivity.this, "UserProfile is NOT null", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "User profile retrieved: " + userProfile.getFullName());
                            } else {
                                Toast.makeText(LoginActivity.this, "UserProfile is null", Toast.LENGTH_LONG).show();
                                Log.d(TAG, "User profile is null");
                                Toast.makeText(LoginActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e(TAG, "Database error: " + error.getMessage());
                            Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else{
                    // Handle login exceptions
                    try{
                        throw  task.getException();
                    } catch (FirebaseAuthInvalidUserException exception){
                        login_email_edit_text.setError("User does not exist or no longer valid. Please register");
                        login_email_edit_text.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException exception){
                        login_email_edit_text.setError("Invalid credentials, please re-enter");
                        login_email_edit_text.requestFocus();
                    } catch (Exception exception){
                        Log.e(TAG, exception.getMessage());
                        Toast.makeText(LoginActivity.this, "Something went wrong!\n" + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(LoginActivity.this, "ERROR logging in!", Toast.LENGTH_SHORT).show();
                }
                // Hide progress bar
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Show dialog for email not verified
    private void showDialogEmailNotVerified() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Sent you an email verification link. Please verify your email");

        builder.setPositiveButton("Open Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                continueProcessGoToPlantActivity();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    // Continue with the login process; go to
    // DataActivity if the user doesnt have a plant set up already
    // PlantActivity if the user has a plant
    private void continueProcessGoToPlantActivity() {
        if (CurrentLoggedUser.getInstance().getCurrentUserProfile() != null) {
            Plant plant = CurrentLoggedUser.getInstance().getCurrentUserProfile().getPlant();
            if (plant != null) {
                // Plant object exists, navigate to PlantActivity
                startActivity(new Intent(LoginActivity.this, PlantActivity.class));
            } else {
                // Plant object is null or plantName is null, navigate to DataActivity
                startActivity(new Intent(LoginActivity.this, DataActivity.class));
            }
            finish(); // close login activity
        }
    }
}


// create an alert for first time login, to tell the user to enter plant info