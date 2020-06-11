package com.example.isafac;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegistrationPage extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText mFullRankAndName, mEmail, mPassword, mHandphoneNo;
    Button mRegisterBtn; //click this button to register
    TextView MovetoLogin; // click here to direct to login page
    RadioGroup mBranchGroup; //contains the radiobutton
    RadioButton mBranchButton; //contains all the branches button
    FirebaseAuth fAuth;
    ProgressBar progressbar;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_page);

        mFullRankAndName = findViewById(R.id.RankName);
        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.Password);
        mHandphoneNo = findViewById(R.id.PhoneNo);
        mRegisterBtn = findViewById(R.id.RegisterButton);
        mBranchGroup = findViewById(R.id.BranchGroup);
        MovetoLogin = findViewById(R.id.MovetoLogin);

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase Authentication to perform operations on the database
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firebase Firestore to perform ops
        progressbar =  findViewById(R.id.progressBar);

        int branchID = mBranchGroup.getCheckedRadioButtonId(); // get radiobutton id when selected
        mBranchButton = findViewById(branchID); // depends on selection, assign selected button id to mBranchButton variable

        MovetoLogin.setOnClickListener(new View.OnClickListener() { //Click this text below login to move to registration page
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginPage.class));
                finish();
            }
        });

        mRegisterBtn.setOnClickListener(new View.OnClickListener() { //what happens when u click the register button
            @Override
            public void onClick(View v) {
                //convert object to string
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String rankname = mFullRankAndName.getText().toString();
                final String handphone = mHandphoneNo.getText().toString();
                final String branch = mBranchButton.getText().toString();

                String HandphonePattern = "[0-9]{8}";


                //some validations

                if(TextUtils.isEmpty(rankname)){
                    mFullRankAndName.setError("Please enter your full rank and name");
                    mFullRankAndName.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(handphone)){
                    mHandphoneNo.setError("Please enter your handphone number");
                    mHandphoneNo.requestFocus();
                    return;
                }

                if(handphone.length() < 8){
                    mHandphoneNo.setError("Please enter a valid handphone number");
                    mHandphoneNo.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Please enter your email");
                    mEmail.requestFocus();
                    return;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Please enter a valid email");
                    mEmail.requestFocus();
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Please enter your password");
                    mPassword.requestFocus();
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password need to be at least 6 Characters");
                    mPassword.requestFocus();
                    return;
                }

                progressbar.setVisibility(View.VISIBLE); //set invisible progress bar to become visible

                //register/authenticate user into firebase
                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if(task.isSuccessful()){ //check whether connection is successful
                            //store user info in firestore database
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("Users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("FullRankAndName",rankname);
                            user.put("Branch",branch);
                            user.put("HandphoneNumber",handphone);
                            user.put("Email",email);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"OnSuccess : User Profile is created successfully for" + userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() { //check for any failure for connection -> need to adjust rules in firestore web
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onFailure : " +e.toString());
                                }
                            }); //end of storing user info in firestore database

                            // Send Email verification to users to verify account
                            fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) { //determine whether email verification is sent out successfully to user
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegistrationPage.this,"Account Created Successfully. Please Verify Your Email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(RegistrationPage.this,"Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //startActivity(new Intent(getApplicationContext(), AnnouncementPage.class)); //if registration success, move to Home page
                            Intent registerverify = new Intent(RegistrationPage.this, AnnouncementPage.class); //transition from register page to emailverifypage
                            registerverify.putExtra("registerverify","registerverification");
                            startActivity(registerverify);
                            finish();
                        }else {

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) { //check if user is already registered
                                Toast.makeText(RegistrationPage.this,"You are already registered", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(RegistrationPage.this,"Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                });
            }
        });
    }

    public void checkBranch(View v) {
        int branchID = mBranchGroup.getCheckedRadioButtonId();
        mBranchButton = findViewById(branchID);
        Toast.makeText(this,"Selected Branch : " + mBranchButton.getText(), Toast.LENGTH_SHORT).show();
    }
}






