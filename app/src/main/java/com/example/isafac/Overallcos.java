package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Overallcos extends AppCompatActivity {

    private static final String TAG = "TAG";
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button SaveUpdateBtn;

    EditText DisplayDateCOS, DisplayOverallStrCOS, DisplayReportedByCOS, DisplayAmPmStatusCOS;

    //Update data from ReportStrList
    String UpdateUID, UpdateDate,  UpdateBranch, UpdateAMPMstatus, UpdateOverallStr, UpdateReportedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overallcos);

        DisplayDateCOS = findViewById(R.id.dateIDCOS);
        DisplayOverallStrCOS = findViewById(R.id.OverallStrengthIDCOS);
        DisplayReportedByCOS = findViewById(R.id.ReportedByIDCOS);
        DisplayAmPmStatusCOS = findViewById(R.id.AmPmStatusIDCOS);
        SaveUpdateBtn = findViewById(R.id.SaveCOSBtn);

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase authentication to perform operations on the database
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database
        userID = fAuth.getCurrentUser().getUid(); // get unique ID of user who is logged in.

        //If we came here after clicking update option on Alert Dialog from ReportStrList, then get the data (UID,date,Branch,ampmstatus,reportedstrength,reportedbyuser)
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            //update button name to "UPDATE"
            SaveUpdateBtn.setText("UPDATE");

            // retrieve update data details
            UpdateUID = bundle.getString("putUID");
            UpdateDate = bundle.getString("putDate");
            UpdateAMPMstatus = bundle.getString("putAMPMstatus");
            UpdateOverallStr = bundle.getString("putOverallStr");
            UpdateReportedBy = bundle.getString("putReportedBy");

            //Actual user who is updating the data
            DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    //Actual user who is updating the data
                    String ActualUser = documentSnapshot.getString("FullRankAndName");
                    //Actual date for the user who is updating
                    String ActualDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                    DisplayReportedByCOS.setText(UpdateReportedBy);
                    DisplayOverallStrCOS.setText(UpdateOverallStr + "\n\nUpdated By: " + ActualUser + " " + "on " + ActualDate);
                    DisplayDateCOS.setText(UpdateDate);
                    DisplayAmPmStatusCOS.setText(UpdateAMPMstatus);

                }
            });

        } else {

            SaveUpdateBtn.setText("SAVE");

            //Display today's date
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            DisplayDateCOS.setText(date);
            DisplayDateCOS.setSelection(DisplayDateCOS.getText().length());

            //Set Branches Text to Overall STr
            String overallStr = "HR Total Strength: \n\nOPS Total Strength: \n\nCDB Total Strength: \n\nMSB Total Strength: \n\nESB Total Strength: \n\nLOGS Total Strength: \n\nOverall Total Strength: \n\n";
            DisplayOverallStrCOS.setText(overallStr);

            DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    //display user currently logged in
                    DisplayReportedByCOS.setText(documentSnapshot.getString("FullRankAndName"));
                }
            });
        }


    }

    public void SaveCOSBtn(View view) {

        //initialise firestore
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database

        //store report strength details on firestore
        String TodayDate = DisplayDateCOS.getText().toString();
        String AmPmStatus = DisplayAmPmStatusCOS.getText().toString();
        String OverallStrStatus = DisplayOverallStrCOS.getText().toString();
        String ReportedByUser = DisplayReportedByCOS.getText().toString();
        String uniqueId = UUID.randomUUID().toString();

        if(TextUtils.isEmpty(TodayDate)){
            DisplayDateCOS.setError("Please enter Today's Date and Time");
            DisplayDateCOS.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(AmPmStatus)){
            DisplayAmPmStatusCOS.setError("Please indicate AM or PM status");
            DisplayAmPmStatusCOS.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(OverallStrStatus)){
            DisplayOverallStrCOS.setError("Please indicate the overall strength");
            DisplayOverallStrCOS.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(ReportedByUser)){
            DisplayReportedByCOS.setError("Please indicate who will be reporting the parade state");
            DisplayReportedByCOS.requestFocus();
            return;
        }

        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {

            //update data
            DocumentReference UpdateStrengthStatus = fStore.collection("Overall_Strength").document(UpdateUID); //updateUID is the data we retrieve from ReportStrList
            Map<String,Object> UpdateStrength = new HashMap<>();
            UpdateStrength.put("Date",TodayDate);
            UpdateStrength.put("AM_PM_Status",AmPmStatus);
            UpdateStrength.put("OverallStrength",OverallStrStatus);
            UpdateStrength.put("ReportedByUser",ReportedByUser);

            UpdateStrengthStatus.update(UpdateStrength).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"OnSuccess : Records are updated successfully for" + UpdateUID);
                    Toast.makeText(Overallcos.this,"Records are updated successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() { //check for any failure for connection -> need to adjust rules in firestore web
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : " +e.toString());
                }
            }); //end of updating user info in firestore database

            startActivity(new Intent(getApplicationContext(),OverallCosList.class));
            finish();

        } else {

            //add data
            DocumentReference OverallStrengthStatus = fStore.collection("Overall_Strength").document(uniqueId);
            Map<String,Object> OverallStrength = new HashMap<>();
            OverallStrength.put("Date",TodayDate);
            OverallStrength.put("AM_PM_Status",AmPmStatus);
            OverallStrength.put("OverallStrength",OverallStrStatus);
            OverallStrength.put("ReportedByUser",ReportedByUser);

            OverallStrengthStatus.set(OverallStrength).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"OnSuccess : Records are saved successfully for" + userID);
                    Toast.makeText(Overallcos.this,"Records are saved successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() { //check for any failure for connection -> need to adjust rules in firestore web
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : " +e.toString());
                }
            }); //end of storing user info in firestore database

            startActivity(new Intent(getApplicationContext(),OverallCosList.class));
            finish();

        }

    }

    public void CloseCOSBtn(View view) {

        startActivity(new Intent(getApplicationContext(),OverallCosList.class));
        finish();
    }

}

