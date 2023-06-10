package com.example.plantcaresystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

    private EditText login_email_edit_text, login_password_edit_text;
    private ProgressBar progressBar;
    private Button buttonLogin;

    String textPassword, textEmail;

    private FirebaseAuth authProfile;

    private static final String TAG = "LoginActivity";

    private void init(){
        login_email_edit_text = findViewById(R.id.editText_login_email);
        login_password_edit_text = findViewById(R.id.editText_login_password);
        progressBar = findViewById(R.id.progressBar_login);
        authProfile = FirebaseAuth.getInstance();
        buttonLogin = findViewById(R.id.button_login);
    }

    private void showHidePass(){
        // show/hide password using eye icon
        ImageView imageViewShowHidePassword = findViewById(R.id.image_show_hide_password);
        imageViewShowHidePassword.setImageResource(R.drawable.id_hide_password);
        imageViewShowHidePassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // check if the password was hidden or shown
                if(login_password_edit_text.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())){
                    // if password is visible, hide it
                    login_password_edit_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // change icon
                    imageViewShowHidePassword.setImageResource(R.drawable.id_hide_password);
                } else {
                    login_password_edit_text.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    imageViewShowHidePassword.setImageResource(R.drawable.ic_show_password);
                }
            }
        });
    }

    private void buttonListener(){
        buttonLogin.setOnClickListener(new View.OnClickListener() { // we assume user has entered email and password
            @Override
            public void onClick(View v) {
                textPassword = login_password_edit_text.getText().toString();
                textEmail = login_email_edit_text.getText().toString();

                // check if user enter data or not
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
                    // progress bar visible
                    progressBar.setVisibility(View.VISIBLE);
                    // login user
                    loginUser(textEmail, textPassword);
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        init();
        showHidePass();
        buttonListener();
    }

    private void loginUser(String email, String password) {

        // on complete listener to check for the completion of the task
        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();

                    // get the current user instance
                    FirebaseUser currentUser = authProfile.getCurrentUser();

                    // Toast.makeText(LoginActivity.this, "current user:"+currentUser, Toast.LENGTH_SHORT).show();

                    // is the email verified?
                    if(currentUser.isEmailVerified()){
                        Toast.makeText(LoginActivity.this, "You're already logged in", Toast.LENGTH_SHORT).show();
                        // open plant or data page
                        continueProcessGoToMainPage();
                    }
                    else{
                        currentUser.sendEmailVerification();
                        // sign out user
                        // authProfile.signOut();
                        showDialogEmailNotVerified();
                    }
/*
                    if(currentUser.getUid() == null)
                        Toast.makeText(LoginActivity.this, "No uid", Toast.LENGTH_LONG).show();
                    else if(Databases.usersDB == null)
                        Toast.makeText(LoginActivity.this, "DB null", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(LoginActivity.this, "neither", Toast.LENGTH_SHORT).show();*/


                    // Start the DataActivity or perform any other necessary action
                    // startActivity(new Intent(LoginActivity.this, DataActivity.class));

/*
                    Databases.usersDB.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserProfile userProfile = snapshot.getValue(UserProfile.class);
                            CurrentLoggedUser.getInstance().setCurrentUserProfile(userProfile);

                            if (userProfile != null) {
                                // User profile is retrieved successfully
                                // Display a toast message indicating that the user profile is not null
                                Toast.makeText(LoginActivity.this, "UserProfile is NOT null", Toast.LENGTH_LONG).show();

                                // Log the retrieved user profile for debugging purposes
                                Log.d(TAG, "User profile retrieved: " + userProfile.getFullName());

                                // Start the DataActivity or perform any other necessary action
                                startActivity(new Intent(LoginActivity.this, DataActivity.class));
                            } else {
                                // User profile is null, indicating that it was not found in the database
                                // Display a toast message indicating that the user profile is null
                                Toast.makeText(LoginActivity.this, "UserProfile is null", Toast.LENGTH_LONG).show();

                                // Log a message indicating that the user profile is null for debugging purposes
                                Log.d(TAG, "User profile is null");

                                // Handle the case when the user profile is not found in the database
                                Toast.makeText(LoginActivity.this, "User profile not found", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // An error occurred while accessing the database
                            // Log the database error for debugging purposes
                            Log.e(TAG, "Database error: " + error.getMessage());

                            // Handle the database error
                            Toast.makeText(LoginActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
*/



/*                    Databases.usersDB.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserProfile userProfile = snapshot.getValue(UserProfile.class);
                           //  CurrentLoggedUser.getInstance().setCurrentUser(userProfile);

                            startActivity(new Intent(LoginActivity.this, DataActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/


                } else{
                    // handling exceptions
                    try{
                       throw  task.getException();
                    } catch (FirebaseAuthInvalidUserException exception){ // user does not exist anymore, account maybe deleted
                        login_email_edit_text.setError("User does not exist or no longer valid. Please register");
                        login_email_edit_text.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException exception){ // invalid credentials, email and password do not match
                        login_email_edit_text.setError("Invalid credentials, please re-enter");
                        login_email_edit_text.requestFocus();
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

    private void showDialogEmailNotVerified() {
        // setup alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Sent you an email verification link\n Please verify your email");

        // open the email app if user clicks "open email"
        builder.setPositiveButton("Open Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // open the email app as a new app, in a new windows, not on top of ours
                startActivity(intent);
            }
        });

        // Close the alert and continue with the process
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Close the dialog
                continueProcessGoToMainPage(); // Add your code to continue with the process here
            }
        });

        // create an alertbox
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }

    // close the alert box and continue with the logging in
    private void continueProcessGoToMainPage() {
        startActivity(new Intent(LoginActivity.this, DataActivity.class));
        finish(); // close login activity
    }

    // is the user already logged in?
    // if so, take the user to the plant or the data page ( to be decided )
/*    @Override
    protected void onStart() {
        super.onStart();

        if(authProfile.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, "You're already logged in", Toast.LENGTH_SHORT).show();

            // start the data or the plant profile, not decided yet
            startActivity(new Intent(LoginActivity.this, UserProfileActivity.class));
            finish(); // close login activity
        }
        else{
            Toast.makeText(LoginActivity.this, "Please login!", Toast.LENGTH_SHORT).show();
        }
    }*/
}