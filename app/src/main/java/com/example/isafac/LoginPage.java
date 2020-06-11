package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPage extends AppCompatActivity {

    EditText  mEmail, mPassword;
    Button mLoginBtn;
    TextView ClickToRegister; // click here to direct to login page
    TextView ResetPassword; //click here to reset your password

    FirebaseAuth fAuth;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        mEmail = findViewById(R.id.Email2);
        mPassword = findViewById(R.id.Password2);
        mLoginBtn = findViewById(R.id.LoginButton);
        ClickToRegister = findViewById(R.id.ClickToRegister);
        ResetPassword = findViewById(R.id.ResetPassword);

        TextView Emailverifyannouncement;

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase to perform operations on the database
        progressbar =  findViewById(R.id.progressBar2);
        Emailverifyannouncement = findViewById(R.id.announcementID);

        ClickToRegister.setOnClickListener(new View.OnClickListener() { //Click this text below login to move to registration page
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),RegistrationPage.class));
                finish();
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetEmail = new EditText((v.getContext()));
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("FORGET YOUR PASSWORD?");
                passwordResetDialog.setMessage("Please enter your email to reset your password.");
                passwordResetDialog.setView(resetEmail);
                //set a yes button
                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });

                //set a no button
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog , do need to put anything here bcause the default builder will dismiss/close on itself
                    }
                });

                //create a custom dialog to prevent the dialog from closing when positive button "YES" is clicked
                final AlertDialog dialog = passwordResetDialog.create();
                dialog.show();
                //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Boolean wantToCloseDialog = false;  //Do stuff, possibly set wantToCloseDialog to true then...
                        //Extract email and send the reset link
                        String emailToReset = resetEmail.getText().toString(); //convert email input from user to store it in a string
                        if(TextUtils.isEmpty(emailToReset)){
                            resetEmail.setError("Please enter your email");
                            resetEmail.requestFocus();
                            return;
                        }

                        if(!Patterns.EMAIL_ADDRESS.matcher(emailToReset).matches()) {
                            resetEmail.setError("Please enter a valid email");
                            resetEmail.requestFocus();
                            return;
                        }
                        wantToCloseDialog = true;
                        progressbar.setVisibility(View.VISIBLE);
                        fAuth.sendPasswordResetEmail(emailToReset).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(LoginPage.this,"Reset Link has been sent to your email.", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(),ResetPassword.class));
                                Intent resetpwverify = new Intent(LoginPage.this, AnnouncementPage.class); //transition from register page to emailverifypage
                                resetpwverify.putExtra("resetpassword","resetyourpassword");
                                startActivity(resetpwverify);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressbar.setVisibility(View.GONE);
                                Toast.makeText(LoginPage.this,"Error! Reset Link is not sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        if(wantToCloseDialog == true)
                            dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });
            }
        });

        mLoginBtn.setOnClickListener(new View.OnClickListener() { //what happens when login button is clicked
            @Override
            public void onClick(View v) {
                //convert object to string
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                //some validations
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

                //Authenticate the users

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressbar.setVisibility(View.GONE);
                        if(task.isSuccessful()){ //check whether connection is successful
                            Toast.makeText(LoginPage.this,"Login Successfully", Toast.LENGTH_SHORT).show();
                            if(fAuth.getCurrentUser().isEmailVerified()) //check if email is verified in login page
                            {
                                startActivity(new Intent(getApplicationContext(), HomePage.class)); //if email verified , move to homepage
                                finish();
                            } else {
                                Toast.makeText(LoginPage.this,"Please Verify Your Email", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(getApplicationContext(), AnnouncementPage.class)); //else move to email verification screen to ask for verification
                                Intent loginverify = new Intent(LoginPage.this, AnnouncementPage.class); //transition from register page to emailverifypage
                                loginverify.putExtra("loginverify","loginverification");
                                startActivity(loginverify);
                                finish();
                            }

                        }else {
                            Toast.makeText(LoginPage.this,"Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }
}
