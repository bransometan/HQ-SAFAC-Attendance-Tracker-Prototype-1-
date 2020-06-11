package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfile extends AppCompatActivity {

    public static final String TAG = "TAG";

    EditText  newNameAndRank, newHandphone;
    Button updateButton, changepasswordButton, deleteaccountButton;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        newNameAndRank = findViewById(R.id.updateRankName);
        newHandphone = findViewById(R.id.updateHandphone);
        updateButton = findViewById(R.id.updatebutton);
        changepasswordButton = findViewById(R.id.changepasswordbutton);
        deleteaccountButton = findViewById(R.id.deleteaccbtn);

        fAuth = FirebaseAuth.getInstance(); //get connecting instance with firebase to perform operations on the database
        fStore = FirebaseFirestore.getInstance(); //get connecting instance with firestore database to perform operations on the database

        progressbar =  findViewById(R.id.progressBar3);

        //Retrieve and Display user profile from firestore to edittext
        String userID = fAuth.getCurrentUser().getUid(); // get unique ID of user who is logged in.

        DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                newNameAndRank.setText(documentSnapshot.getString("FullRankAndName"));
                newHandphone.setText(documentSnapshot.getString("HandphoneNumber"));

            }
        });

        //update your full rank and name and handphone button when pressed
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newRANKNAME = newNameAndRank.getText().toString();
                String newPHONE = newHandphone.getText().toString();

                if(TextUtils.isEmpty(newRANKNAME)){
                    newNameAndRank.setError("Please enter your full rank and name");
                    newNameAndRank.requestFocus();
                    return;
                }
                if(TextUtils.isEmpty(newPHONE)){
                    newHandphone.setError("Please enter your handphone number");
                    newHandphone.requestFocus();
                    return;
                }

                if(newPHONE.length() < 8){
                    newHandphone.setError("Please enter a valid handphone number");
                    newHandphone.requestFocus();
                    return;
                }

                AlertDialog.Builder updateacc = new AlertDialog.Builder(v.getContext());
                updateacc.setTitle("UPDATE YOUR PROFILE?");
                updateacc.setMessage("Do you want to update your RANK,NAME and HANDPHONE NUMBER?");

                //set a yes button
                updateacc.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = fAuth.getCurrentUser(); //get current user who is logged in
                        String userID = fAuth.getCurrentUser().getUid();
                        String newRANKNAME = newNameAndRank.getText().toString();
                        String newPHONE = newHandphone.getText().toString();

                        progressbar.setVisibility(View.VISIBLE);
                        //check if user is logged in
                        if(user!=null){
                            //update user on firestore database
                            DocumentReference updateprofile = fStore.collection("Users").document(userID);
                            Map<String,Object> updateuser = new HashMap<>();
                            updateuser.put("FullRankAndName",newRANKNAME);
                            updateuser.put("HandphoneNumber",newPHONE);

                            updateprofile.update(updateuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressbar.setVisibility(View.GONE);
                                    Log.d(TAG,"onSuccess: We have updated the document");
                                    Toast.makeText(UpdateProfile.this,"User Profile has been updated successfully.", Toast.LENGTH_SHORT).show();
                                    fAuth.signOut(); //signout after updating
                                    //startActivity(new Intent(getApplicationContext(),LoginPage.class));
                                    Intent updateaccverify = new Intent(UpdateProfile.this, AnnouncementPage.class); //transition from register page to emailverifypage
                                    updateaccverify.putExtra("updateaccverify","updateyouraccount");
                                    startActivity(updateaccverify);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG,"onFailure: ", e);
                                    Toast.makeText(UpdateProfile.this,"Please Re-Login and try again.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                });

                //set a no button
                updateacc.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog , do need to put anything here bcause the default builder will dismiss/close on itself
                    }
                });

                updateacc.create().show();
            }
        });

        //change password button when pressed
        changepasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText changepassword = new EditText((v.getContext()));
                AlertDialog.Builder newpassword = new AlertDialog.Builder(v.getContext());
                newpassword.setTitle("CHANGE YOUR PASSWORD?");
                newpassword.setMessage("Please enter your new password.");
                newpassword.setView(changepassword);
                //set a yes button
                newpassword.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here because we override this button later to change the close behaviour.
                        //However, we still need this because on older versions of Android unless we
                        //pass a handler the button doesn't get instantiated
                    }
                });

                //set a no button
                newpassword.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog , do need to put anything here bcause the default builder will dismiss/close on itself
                    }
                });

                //create a custom dialog to prevent the dialog from closing when positive button "YES" is clicked
                final AlertDialog dialog = newpassword.create();
                dialog.show();
                //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        String pwchange = changepassword.getText().toString(); //convert email input from user to store it in a string
                        FirebaseUser user = fAuth.getCurrentUser();
                        Boolean wantToCloseDialog = false;  //Do stuff, possibly set wantToCloseDialog to true then...

                        if(TextUtils.isEmpty(pwchange)){
                            changepassword.setError("Please enter your new password");
                            changepassword.requestFocus();
                            return;
                        }

                        if(pwchange.length() < 6){
                            changepassword.setError("New password need to be at least 6 Characters");
                            changepassword.requestFocus();
                            return;
                        }

                        wantToCloseDialog = true;
                        progressbar.setVisibility(View.VISIBLE);
                        if(user!=null){
                            user.updatePassword(pwchange).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressbar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        progressbar.setVisibility(View.GONE);
                                        Toast.makeText(UpdateProfile.this,"Password has been changed successfully.", Toast.LENGTH_SHORT).show();
                                        fAuth.signOut(); //signout after changing password
                                        //startActivity(new Intent(getApplicationContext(),LoginPage.class));
                                        Intent changepwverify = new Intent(UpdateProfile.this, AnnouncementPage.class); //transition from register page to emailverifypage
                                        changepwverify.putExtra("changepwverify","changeyourpassword");
                                        startActivity(changepwverify);
                                        finish();
                                    } else{
                                        Toast.makeText(UpdateProfile.this,"Please Re-Login and try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        if(wantToCloseDialog == true)
                            dialog.dismiss();
                        //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                    }
                });
            }
        });

        //delete account button when pressed
        deleteaccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder deleteacc = new AlertDialog.Builder(v.getContext());
                deleteacc.setTitle("DELETE YOUR ACCOUNT?");
                deleteacc.setMessage("Do you want to delete your account? This account will be permanently removed.");

                //set a yes button
                deleteacc.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseUser user = fAuth.getCurrentUser(); //get current user who is logged in
                        String userID = fAuth.getCurrentUser().getUid();
                        progressbar.setVisibility(View.VISIBLE);
                        //check if user is logged in
                        if(user!=null){
                            //delete user on firestore database
                            DocumentReference docRef = fStore.collection("Users").document(userID);
                            docRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"onSuccess: We have deleted the document");

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "onFailure: ",e);
                                }
                            });
                            //delete user on firebase authentication
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) { //delete user on firebase authentication
                                    progressbar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        progressbar.setVisibility(View.GONE);
                                        Toast.makeText(UpdateProfile.this,"Your account is deleted successfully.", Toast.LENGTH_SHORT).show();
                                        fAuth.signOut(); //signout after changing password
                                        //startActivity(new Intent(getApplicationContext(),LoginPage.class));
                                        Intent deleteaccverify = new Intent(UpdateProfile.this, AnnouncementPage.class); //transition from register page to emailverifypage
                                        deleteaccverify.putExtra("deleteaccverify","deleteyouraccount");
                                        startActivity(deleteaccverify);
                                        finish();
                                    } else{
                                        Toast.makeText(UpdateProfile.this,"Please Re-Login and try again.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                });

                //set a no button
                deleteacc.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog , do need to put anything here bcause the default builder will dismiss/close on itself
                    }
                });

                deleteacc.create().show();
            }
        });


    }

}
