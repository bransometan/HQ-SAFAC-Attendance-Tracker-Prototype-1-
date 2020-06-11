package com.example.isafac;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIMEOUT = 3000; // Set timer for Splash Screen before reaching login page

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                FirebaseAuth fAuth;
                fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase to perform operations on the database

                if(fAuth.getCurrentUser() !=null && fAuth.getCurrentUser().isEmailVerified()) { //check if user already logged in AND if email is verified, will direct him to homepage if so.
                   startActivity(new Intent(SplashScreen.this,HomePage.class));

                } else {
                    Intent LoginIntent = new Intent(SplashScreen.this, LoginPage.class); //transition from splashscreen to login page
                    startActivity(LoginIntent);
                     // to prevent going back to splash screen after reaching login page
                }
                finish();
            }
        },SPLASH_SCREEN_TIMEOUT);


    }
}