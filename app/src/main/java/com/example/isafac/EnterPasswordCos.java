package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EnterPasswordCos extends AppCompatActivity {

    EditText passwordCOS;
    String userID, branch;
    String GenPW = "000000";

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password_cos);

        passwordCOS = findViewById(R.id.passwordtoCOSID);

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase authentication to perform operations on the database
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database
        userID = fAuth.getCurrentUser().getUid(); // get unique ID of user who is logged in.

        DocumentReference documentReference2 = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
        documentReference2.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                branch = documentSnapshot.getString("Branch");
            }
        });

        String TodayDate = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());

        DocumentReference documentReference = fStore.collection("Generated_Password").document(TodayDate); // state which collection and document you are going to retrieve
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                GenPW = documentSnapshot.getString("Generated_Password");

            }
        });

    }

    public void authentication(View view) {

        if(TextUtils.isEmpty(passwordCOS.getText().toString()))
        {
            passwordCOS.setError("Please enter the password");
           passwordCOS.requestFocus();
            return;
        }

        if(passwordCOS.getText().toString().length() < 6){
            passwordCOS.setError("Password should be 6-digits");
            passwordCOS.requestFocus();
            return;
        }

        if(GenPW.contains("000000"))
        {
            passwordCOS.setError("Admin has not generated the password");
            passwordCOS.requestFocus();
            return;
        }

        if(!passwordCOS.getText().toString().contains(GenPW))
        {
            passwordCOS.setError("Invalid Password, Please Try Again!");
            passwordCOS.requestFocus();
            return;
        }

        startActivity(new Intent(getApplicationContext(),OverallCosList.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(branch.contains("ADMIN"))
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.cospassword, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.genpasswordID:
                startActivity(new Intent(getApplicationContext(),GeneratePasswordCos.class));
                finish();
                return true;
            case R.id.proceedoverallcosID:
                startActivity(new Intent(getApplicationContext(),OverallCosList.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
