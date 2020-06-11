package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportStrength extends AppCompatActivity {
    private static final String TAG = "TAG";
    TextView DisplayBranch;
    EditText DisplayDate, DisplayAmPmStatus, DisplayReportStrStatus, DisplayReportedBy;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Button SaveUpdateBtn;

    //Update data from ReportStrList
    String UpdateUID, UpdateDate,  UpdateBranch, UpdateAMPMstatus, UpdateReportedStr, UpdateReportedBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_strength);

        //Retrieve and Display user profile from firestore
        DisplayBranch = findViewById(R.id.FetchBranchID);
        DisplayDate = findViewById(R.id.dateID);
        DisplayAmPmStatus = findViewById(R.id.AmPmStatusID);
        DisplayReportStrStatus = findViewById(R.id.ReportStrStatusID);
        DisplayReportedBy = findViewById(R.id.ReportedByID);
        SaveUpdateBtn = findViewById(R.id.saveRSbtn);

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
            UpdateBranch = bundle.getString("putBranch");
            UpdateAMPMstatus = bundle.getString("putAMPMstatus");
            UpdateReportedStr = bundle.getString("putReportedStr");
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

                    DisplayBranch.setText(UpdateBranch);
                    DisplayReportedBy.setText(UpdateReportedBy);
                    DisplayReportStrStatus.setText(UpdateReportedStr + "\n\nUpdated By: " + ActualUser + " " + "on " + ActualDate);
                    DisplayDate.setText(UpdateDate);
                    DisplayAmPmStatus.setText(UpdateAMPMstatus);

                }
            });

        } else { //retrieve add new data details

            //update button name to "SAVE"
            SaveUpdateBtn.setText("SAVE");

            //display logged in user branch
            DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    DisplayBranch.setText(documentSnapshot.getString("Branch"));
                    DisplayReportedBy.setText(documentSnapshot.getString("FullRankAndName"));

                    //retrieve multiple data from firestore and extract with maps
                    GetBranchUserInfo();

                }
            });

            //auto set today's date
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            DisplayDate.setText(date);
            DisplayDate.setSelection(DisplayDate.getText().length());

        }

    }

    private void GetBranchUserInfo() {

        //retrieve for hrbranch
         if (DisplayBranch.getText().toString().contains("HR")) {
                fStore.collection("Users").whereEqualTo("Branch", "HR")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String Data = "";
                                    int count = 1;
                                    int totalstrength;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Log.d(TAG, document.getId() + " => " + document.getData());
                                        Map<String, Object> users = document.getData();
                                        String Name = users.get("FullRankAndName").toString();
                                        String HP = users.get("HandphoneNumber").toString();
                                        Data += "(" + count + ")" + Name + "(" + HP + "): P" + "\n";
                                        count++;
                                    }
                                    totalstrength = count - 1;
                                    DisplayReportStrStatus.setText(Data);
                                    DisplayReportStrStatus.append("\nTotal Strength: " + totalstrength);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            } else if


            //retrieve for ops branch
            (DisplayBranch.getText().toString().contains("OPS")) {
                fStore.collection("Users").whereEqualTo("Branch", "OPS")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String Data = "";
                                    int count = 1;
                                    int totalstrength;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Log.d(TAG, document.getId() + " => " + document.getData());
                                        Map<String, Object> users = document.getData();
                                        String Name = users.get("FullRankAndName").toString();
                                        String HP = users.get("HandphoneNumber").toString();
                                        Data += "(" + count + ")" + Name + "(" + HP + "): P" + "\n";
                                        count++;
                                    }
                                    totalstrength = count - 1;
                                    DisplayReportStrStatus.setText(Data);
                                    DisplayReportStrStatus.append("\nTotal Strength: " + totalstrength);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }

            //retrieve for Cdb branch
           else if (DisplayBranch.getText().toString().contains("CDB")) {
                fStore.collection("Users").whereEqualTo("Branch", "CDB")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String Data = "";
                                    int count = 1;
                                    int totalstrength;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Log.d(TAG, document.getId() + " => " + document.getData());
                                        Map<String, Object> users = document.getData();
                                        String Name = users.get("FullRankAndName").toString();
                                        String HP = users.get("HandphoneNumber").toString();
                                        Data += "(" + count + ")" + Name + "(" + HP + "): P" + "\n";
                                        count++;
                                    }
                                    totalstrength = count - 1;
                                    DisplayReportStrStatus.setText(Data);
                                    DisplayReportStrStatus.append("\nTotal Strength: " + totalstrength);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }


            //retrieve for MSB branch
            else if (DisplayBranch.getText().toString().contains("MSB")) {
                fStore.collection("Users").whereEqualTo("Branch", "MSB")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String Data = "";
                                    int count = 1;
                                    int totalstrength;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Log.d(TAG, document.getId() + " => " + document.getData());
                                        Map<String, Object> users = document.getData();
                                        String Name = users.get("FullRankAndName").toString();
                                        String HP = users.get("HandphoneNumber").toString();
                                        Data += "(" + count + ")" + Name + "(" + HP + "): P" + "\n";
                                        count++;
                                    }
                                    totalstrength = count - 1;
                                    DisplayReportStrStatus.setText(Data);
                                    DisplayReportStrStatus.append("\nTotal Strength: " + totalstrength);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }

            //retrieve for esb branch
            else if (DisplayBranch.getText().toString().contains("ESB")) {
                fStore.collection("Users").whereEqualTo("Branch", "ESB")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String Data = "";
                                    int count = 1;
                                    int totalstrength;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Log.d(TAG, document.getId() + " => " + document.getData());
                                        Map<String, Object> users = document.getData();
                                        String Name = users.get("FullRankAndName").toString();
                                        String HP = users.get("HandphoneNumber").toString();
                                        Data += "(" + count + ")" + Name + "(" + HP + "): P" + "\n";
                                        count++;
                                    }
                                    totalstrength = count - 1;
                                    DisplayReportStrStatus.setText(Data);
                                    DisplayReportStrStatus.append("\nTotal Strength: " + totalstrength);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }

            //retrieve for logs branch
            else if (DisplayBranch.getText().toString().contains("LOGS")) {
                fStore.collection("Users").whereEqualTo("Branch", "LOGS")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    String Data = "";
                                    int count = 1;
                                    int totalstrength;
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Log.d(TAG, document.getId() + " => " + document.getData());
                                        Map<String, Object> users = document.getData();
                                        String Name = users.get("FullRankAndName").toString();
                                        String HP = users.get("HandphoneNumber").toString();
                                        Data += "(" + count + ")" + Name + "(" + HP + "): P" + "\n";
                                        count++;
                                    }
                                    totalstrength = count - 1;
                                    DisplayReportStrStatus.setText(Data);
                                    DisplayReportStrStatus.append("\nTotal Strength: " + totalstrength);
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            } else {
                Toast.makeText(this, "Error: Unable to retrieve any details with this branch" , Toast.LENGTH_SHORT).show();
            }
    }

    public void saveReportStrength(View view) {

        //initialise firestore
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database

        //store report strength details on firestore
        String branch = DisplayBranch.getText().toString();
        String TodayDate = DisplayDate.getText().toString();
        String AmPmStatus = DisplayAmPmStatus.getText().toString();
        String ReportStrStatus = DisplayReportStrStatus.getText().toString();
        String ReportedByUser = DisplayReportedBy.getText().toString();
        String uniqueId = UUID.randomUUID().toString();

        if(TextUtils.isEmpty(TodayDate)){
            DisplayDate.setError("Please enter Today's Date and Time");
            DisplayDate.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(AmPmStatus)){
           DisplayAmPmStatus.setError("Please indicate AM or PM status");
           DisplayAmPmStatus.requestFocus();
           return;
        }

        if((!AmPmStatus.contains("AM")) && (!AmPmStatus.contains("PM"))){
            DisplayAmPmStatus.setError("Only AM or PM keyword is allowed");
            DisplayAmPmStatus.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(ReportStrStatus)){
           DisplayReportStrStatus.setError("Please indicate the parade strength");
           DisplayReportStrStatus.requestFocus();
           return;
        }

        if(TextUtils.isEmpty(ReportedByUser)){
            DisplayReportedBy.setError("Please indicate who will be reporting the parade state");
           DisplayReportedBy.requestFocus();
            return;
        }

        //If we came here after clicking update option on Alert Dialog from ReportStrList, then get the data (UID,date,Branch,ampmstatus,reportedstrength,reportedbyuser)
        Bundle bundle1 = getIntent().getExtras();
        if (bundle1 != null) {

            //update data
            DocumentReference UpdateStrengthStatus = fStore.collection("Report_Strength").document(UpdateUID); //updateUID is the data we retrieve from ReportStrList
            Map<String,Object> UpdateStrength = new HashMap<>();
            UpdateStrength.put("Branch",branch);
            UpdateStrength.put("Date",TodayDate);
            UpdateStrength.put("AM_PM_Status",AmPmStatus);
            UpdateStrength.put("ReportedStrength",ReportStrStatus);
            UpdateStrength.put("ReportedByUser",ReportedByUser);

            UpdateStrengthStatus.update(UpdateStrength).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"OnSuccess : Records are updated successfully for" + UpdateUID);
                    Toast.makeText(ReportStrength.this,"Records are updated successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() { //check for any failure for connection -> need to adjust rules in firestore web
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : " +e.toString());
                }
            }); //end of updating user info in firestore database

            startActivity(new Intent(getApplicationContext(),ReportStrList.class));
            finish();

        } else {
            //add data
            DocumentReference ReportStrengthStatus = fStore.collection("Report_Strength").document(uniqueId);
            Map<String,Object> reportStrength = new HashMap<>();
            reportStrength.put("Branch",branch);
            reportStrength.put("Date",TodayDate);
            reportStrength.put("AM_PM_Status",AmPmStatus);
            reportStrength.put("ReportedStrength",ReportStrStatus);
            reportStrength.put("ReportedByUser",ReportedByUser);

            ReportStrengthStatus.set(reportStrength).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG,"OnSuccess : Records are saved successfully for" + userID);
                    Toast.makeText(ReportStrength.this,"Records are saved successfully.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() { //check for any failure for connection -> need to adjust rules in firestore web
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG,"onFailure : " +e.toString());
                }
            }); //end of storing user info in firestore database

            startActivity(new Intent(getApplicationContext(),ReportStrList.class));
            finish();
        }


    }

    public void closeReportStrength(View view) {

        startActivity(new Intent(getApplicationContext(),ReportStrList.class));
        finish();
    }
}
