package com.example.isafac;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class AnnouncementPage extends AppCompatActivity {

    TextView Announcement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement_page);

        Announcement = findViewById(R.id.announcementID);

        String registerEmailVerify;
        String loginEmailVerify;
        String resetpasswordverify;
        String deleteaccountverify;
        String changepasswordverify;
        String updateaccountverify;


       registerEmailVerify = getIntent().getExtras().getString("registerverify");
       loginEmailVerify = getIntent().getExtras().getString("loginverify");
       resetpasswordverify = getIntent().getExtras().getString("resetpassword");
       updateaccountverify =  getIntent().getExtras().getString("updateaccverify");
       changepasswordverify =  getIntent().getExtras().getString("changepwverify");
       deleteaccountverify = getIntent().getExtras().getString("deleteaccverify");

            //Edit the textview of the annoucement depending on which activity user is at
            if(registerEmailVerify != null)
            {
                Announcement.setText("Please Verify Your Email Before Proceeding...");
            }

        else if(loginEmailVerify != null)
        {
            Announcement.setText("Please Verify Your Email Before Proceeding...");
        }

        else if(resetpasswordverify != null)
        {
            Announcement.setText("Password Reset Link Has Been Sent To Your Email...");
        }

        else if(updateaccountverify != null)
        {
            Announcement.setText("Your Profile Has Been Successfully Updated.");
        }

        else if(changepasswordverify != null)
        {
            Announcement.setText("Your Password Has Been Changed Successfully.");
        }

        else if(deleteaccountverify != null)
        {
            Announcement.setText("Your Account Is Permanently Deleted.");
        } else {
                Toast.makeText(this, "Error : Cannot retrieve announcement page", Toast.LENGTH_SHORT).show();
            }


    }

    public void EmailVerifyToLogin(View view) {
        startActivity(new Intent(getApplicationContext(),LoginPage.class));
        finish();
    }
}
