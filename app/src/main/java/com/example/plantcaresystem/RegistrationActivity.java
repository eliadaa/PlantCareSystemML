package com.example.plantcaresystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class RegistrationActivity extends AppCompatActivity {
    private EditText editFullName, editEmail, editPassword, editConfirmPassword;

    // add also a link to the Login page? or the user can simply go back to the main activity and choose it from there
    private ProgressBar progressBar;

    // Define the password pattern
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?!~])(?=\\S+$).{8,}$";

    private static final String TAG = "RegistrationActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        getSupportActionBar().setTitle("Registration");
        Toast.makeText(RegistrationActivity.this, "You can register now", Toast.LENGTH_LONG).show();

        progressBar = findViewById(R.id.progressBar);

        editFullName = findViewById(R.id.editText_register_full_name);
        editEmail = findViewById(R.id.editText_register_email);
        editPassword = findViewById(R.id.editText_register_password);
        editConfirmPassword = findViewById(R.id.editText_register_confirm_password);

        // not defined globally because it's not called by other methods from other classes, so it would be redundant
        Button register_button = findViewById(R.id.button_registration);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get entered data from the editText fields
                String textFullName, textEmail, textPassword, textConfirmPassword;

                textFullName = editFullName.getText().toString();
                textEmail = editEmail.getText().toString();
                textPassword = editPassword.getText().toString();
                textConfirmPassword = editConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegistrationActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    editFullName.setError("Full Name is required");
                    editFullName.requestFocus();
                } else if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegistrationActivity.this, "Please enter your email", Toast.LENGTH_LONG).show();
                    editEmail.setError("Email is required");
                    editEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){  // check if the email entered matches a valid email address
                    Toast.makeText(RegistrationActivity.this, "Please re-enter your email", Toast.LENGTH_LONG).show();
                    editEmail.setError("Valid email address is required");
                    editEmail.requestFocus();
                } else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegistrationActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    editPassword.setError("Password is required");
                    editPassword.requestFocus();
                } else if(!isPasswordValid(textPassword)){
                    Toast.makeText(RegistrationActivity.this, "Please enter a valid password", Toast.LENGTH_LONG).show();
                    // Perform action when the password does not meet the requirements
                    editPassword.setError("Valid password is required:\n" +
                            "At least 8 characters long\n" +
                            "Contains at least one digit (0-9)\n" +
                            "Contains at least one lowercase letter (a-z)\n" +
                            "Contains at least one uppercase letter (A-Z)\n" +
                            "Contains at least one special character (e.g., @#$%^&+=)\n" +
                            "Does not contain any whitespace");
                    editPassword.requestFocus();
                } else if(TextUtils.isEmpty(textConfirmPassword)){
                    Toast.makeText(RegistrationActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    editConfirmPassword.setError("Password Confirmation is required");
                    editConfirmPassword.requestFocus();
                } else if(!textPassword.equals(textConfirmPassword)){ // check if the passwords match
                    Toast.makeText(RegistrationActivity.this, "Please match the password", Toast.LENGTH_LONG).show();
                    editConfirmPassword.setError("Enter the same password");
                    editConfirmPassword.requestFocus();

                    // clear the entered passwords:
                    editPassword.clearComposingText();
                    editConfirmPassword.clearComposingText();
                } else { // valid information entered, you can register the user
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName, textEmail, textPassword);
                }

            }
        });

    }


    // Register User using the info you got from the fields
    private void registerUser(String textFullName, String textEmail, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // create an user with a the password and email given
        // user creation successful, checked by the onCompleteListener method
        // create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegistrationActivity.this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) { // this creates the user
                if(task.isSuccessful()) {
                    // user creation was executed successfully

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    if(firebaseUser == null){
                        return;
                    }

                    UserProfile user = new UserProfile(textFullName, textEmail, firebaseUser.getUid(), null);

                    // update display name for user
                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();


                    // once the user was created, you can save his information to the Firebase Realtime Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textEmail); // the user also has info about plants, take care of this...


                    // extracting user reference from database for "Registered Users"
                    // create one parent node
                    DatabaseReference referenceUser = FirebaseDatabase.getInstance().getReference("Registered Users");

                    referenceUser.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) { // set the data into the database
                            // only after the user was successfully saved to the database, the email verification is sent

                            if(task.isSuccessful()){

                                // send an email to the user, email verification
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegistrationActivity.this, "User registrered successfully. We also sent you an email, kindly check it", Toast.LENGTH_LONG).show();


                                // open user profile after successful registration
                                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);  // after registering, go to login

                                // removing previous activity from the back stack, so the back button does not take you back to them
                                // if the current activity is already called it does not kill the task and start a new instance
                                // if the activity is new, a new instance of it is created
                                // to prevent user from returning back to register activity on pressing back button after registration
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // close the Register Activity


                            } else {
                                Toast.makeText(RegistrationActivity.this, "User registration failed. Please try again!", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);

                        }
                    });




                } else { // exception handling
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException exception) { // what happens if exception occurs // in this case, weak password
                        editPassword.setError("The password is too weak. Please enter a stronger password");
                        editPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException exception) {
                        editEmail.setError("This email is invalid or already used. Re-enter an email");
                        editEmail.requestFocus();
                    } catch (FirebaseAuthUserCollisionException exception) {
                        editEmail.setError("An user is already registered with this email. Kindly use another one");
                        editEmail.requestFocus();
                    } catch (Exception exception) {
                        Log.e(TAG, exception.getMessage());
                        Toast.makeText(RegistrationActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    // hide progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });

    }

    // Check if the password matches the requirements
    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        return pattern.matcher(password).matches();
    }
}