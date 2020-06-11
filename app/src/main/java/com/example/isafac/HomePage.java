package com.example.isafac;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class HomePage extends AppCompatActivity {

    TextView DisplayRankName, DisplayBranch, DisplayEmail, DisplayHandphone;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //Retrieve and Display user profile from firestore
        DisplayRankName = findViewById(R.id.DisplayRankName);
        DisplayBranch = findViewById(R.id.DisplayBranch);
        DisplayEmail = findViewById(R.id.DisplayEmail);
        DisplayHandphone = findViewById(R.id.DisplayHandphone);

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase authentication to perform operations on the database
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database

        userID = fAuth.getCurrentUser().getUid(); // get unique ID of user who is logged in.

        DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                DisplayRankName.setText(documentSnapshot.getString("FullRankAndName"));
                DisplayBranch.setText(documentSnapshot.getString("Branch"));
                DisplayBranch.append(" Branch"); // Add extra words to the branch retrieved on firestore
                DisplayEmail.setText(documentSnapshot.getString("Email"));
                DisplayHandphone.setText(documentSnapshot.getString("HandphoneNumber"));
            }
        });

    }


    public void logout(View view) {
        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase to perform operations on the database
        fAuth.signOut();
        startActivity(new Intent(getApplicationContext(),LoginPage.class));
        finish();
    }

    public void updateprofile(View view) {

        startActivity(new Intent(getApplicationContext(),UpdateProfile.class));
    }

    public void overallCOS(View view) {

        startActivity(new Intent(getApplicationContext(),EnterPasswordCos.class));
    }

    public void reportStrength(View view) {

        startActivity(new Intent(getApplicationContext(),SelectBranchRS.class));
    }
}
