package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GeneratePasswordCos extends AppCompatActivity {

    TextView PasswordGenerated;
    TextView GeneratedDate;
    Random r;

    private static final String TAG = "TAG";
    FirebaseFirestore fStore;

    private String SavedGenPw, SavedGenDate;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String GENPW = "generatedpw";
    public static final String GENDATE = "generateddate";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_password_cos);

        PasswordGenerated = findViewById(R.id.passwordGenID);
        GeneratedDate = findViewById(R.id.GeneratedOnDateID);

        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database

        r = new Random();

        loadData();
        updateViews();

    }

    public void generatepw(View view) {

        //Generate Today's Date when button is clicked
        final String TodayDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        //Generate Password
        final int Min, Max, output;
        Min = 100000;
        Max = 999999;
        output = r.nextInt((Max - Min) + 1) + Min;

        Toast.makeText(this, "Password Generated: " + output, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder genpw = new AlertDialog.Builder(view.getContext());
        genpw.setTitle("GENERATED PASSWORD");
        genpw.setMessage("Password Generated : " + output);
        genpw.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                PasswordGenerated.setText(String.valueOf(output));
                GeneratedDate.setText("Generated On: " + TodayDate);
                savedata(); //save data in shared preferences so that data will be retrieved when app is closed or back is pressed.

                DocumentReference genpassword = fStore.collection("Generated_Password").document(TodayDate);
                Map<String,Object> pw_gen = new HashMap<>();
                pw_gen.put("Date",TodayDate);
                pw_gen.put("Generated_Password",PasswordGenerated.getText().toString());


                genpassword.set(pw_gen).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG,"OnSuccess : Records are saved successfully for" + TodayDate);
                        Toast.makeText(GeneratePasswordCos.this,"New Password generated successfully.", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() { //check for any failure for connection -> need to adjust rules in firestore web
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG,"onFailure : " +e.toString());
                    }
                }); //end of storing user info in firestore database

                //startActivity(new Intent(getApplicationContext(),EnterPasswordCos.class));
                //finish();

            }
        });

        genpw.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //to prevent item from disappearing when item is clicked on no.
            }
        });

        genpw.create().show();

    }

    private void savedata() {

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //mode private means no other apps can change this sharedpreferences
        SharedPreferences.Editor editor =  sharedPreferences.edit();

        editor.putString(GENPW, PasswordGenerated.getText().toString());
        editor.putString(GENDATE, GeneratedDate.getText().toString());

        editor.apply();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SavedGenDate = sharedPreferences.getString(GENDATE, "");
        SavedGenPw = sharedPreferences.getString(GENPW, "");
    }

    public void updateViews() {

        GeneratedDate.setText(SavedGenDate);
        PasswordGenerated.setText(SavedGenPw);
    }
}
