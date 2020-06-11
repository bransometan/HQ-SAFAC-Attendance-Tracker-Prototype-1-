package com.example.isafac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SelectBranchRS extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID, branch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_branch_r_s);

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase authentication to perform operations on the database
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database

        userID = fAuth.getCurrentUser().getUid(); // get unique ID of user who is logged in.

        DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                branch = documentSnapshot.getString("Branch");
            }
        });
    }

    public void HR(View view) {

        if (branch.contains("HR") || branch.contains("ADMIN")) {
            //go to hr parade strength list
            Intent HR = new Intent(SelectBranchRS.this, ReportStrList.class); //transition from register page to emailverifypage
            HR.putExtra("HR", "hr");
            startActivity(HR);
            finish();
        } else {
            Toast.makeText(this, "Do not have access to HR Branch database", Toast.LENGTH_SHORT).show();
        }
    }

    public void OPS(View view) {

        if (branch.contains("OPS") || branch.contains("ADMIN")) {
            //go to ops parade strength list
            Intent OPS = new Intent(SelectBranchRS.this, ReportStrList.class); //transition from register page to emailverifypage
            OPS.putExtra("OPS","ops");
            startActivity(OPS);
            finish();
        } else {
            Toast.makeText(this, "Do not have access to OPS Branch database", Toast.LENGTH_SHORT).show();
        }
    }

    public void CDB(View view) {

        if (branch.contains("CDB") || branch.contains("ADMIN")) {
            //go to cdb parade strength list
            Intent CDB = new Intent(SelectBranchRS.this, ReportStrList.class); //transition from register page to emailverifypage
            CDB.putExtra("CDB","cdb");
            startActivity(CDB);
            finish();
        } else {
            Toast.makeText(this, "Do not have access to CDB Branch database", Toast.LENGTH_SHORT).show();
        }


    }

    public void MSB(View view) {

        if (branch.contains("MSB") || branch.contains("ADMIN")) {
            //go to cdb parade strength list
            Intent MSB = new Intent(SelectBranchRS.this, ReportStrList.class); //transition from register page to emailverifypage
            MSB.putExtra("MSB","msb");
            startActivity(MSB);
            finish();
        } else {
            Toast.makeText(this, "Do not have access to MSB Branch database", Toast.LENGTH_SHORT).show();
        }
    }

    public void ESB(View view) {

        if (branch.contains("ESB") || branch.contains("ADMIN")) {
            //go to cdb parade strength list
            Intent ESB = new Intent(SelectBranchRS.this, ReportStrList.class); //transition from register page to emailverifypage
            ESB.putExtra("ESB","esb");
            startActivity(ESB);
            finish();
        } else {
            Toast.makeText(this, "Do not have access to ESB Branch database", Toast.LENGTH_SHORT).show();
        }
    }

    public void LOGS(View view) {

        if (branch.contains("LOGS") || branch.contains("ADMIN")) {
            //go to cdb parade strength list
            Intent LOGS = new Intent(SelectBranchRS.this, ReportStrList.class); //transition from register page to emailverifypage
            LOGS.putExtra("LOGS","logs");
            startActivity(LOGS);
            finish();
        } else {
            Toast.makeText(this, "Do not have access to LOGS Branch database", Toast.LENGTH_SHORT).show();
        }
    }
}
