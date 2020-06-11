package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class OverallCosList extends AppCompatActivity {

    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth =  FirebaseAuth.getInstance(); //get connecting instance with firebase authentication to perform operations on the database
    private OverallStrAdapter OSadapter;
    CollectionReference overallstrReference = fStore.collection("Overall_Strength");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overall_cos_list);

        setupRSrecyclerview();
    }

    private void setupRSrecyclerview() {
        Query query = overallstrReference.orderBy("Date", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<OverallStrModel> options = new FirestoreRecyclerOptions.Builder<OverallStrModel>().setQuery(query, OverallStrModel.class).build();

        OSadapter = new OverallStrAdapter(options);

        RecyclerView recyclerViewOS = findViewById(R.id.recycler_view_overallstr);
        recyclerViewOS.setHasFixedSize(true);
        recyclerViewOS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOS.setAdapter(OSadapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder removeOS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                removeOS.setTitle("DELETE");
                removeOS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                removeOS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                        OSadapter.deleteitem(viewHolder.getAdapterPosition());
                    }
                });

                removeOS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //to prevent item from disappearing when item is clicked on no.
                        OSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });

                removeOS.create().show();
            }
        }).attachToRecyclerView(recyclerViewOS);

        //on clicking the respective cardviews
        OSadapter.setOnItemClickListener(new OverallStrAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                final String date = documentSnapshot.getString("Date");
                final String am_pm_status = documentSnapshot.getString("AM_PM_Status");
                final String overallstrength = documentSnapshot.getString("OverallStrength");
                final String reportedbyuser = documentSnapshot.getString("ReportedByUser");
                final String uniqueid = documentSnapshot.getId();

                AlertDialog.Builder updateOS = new AlertDialog.Builder(OverallCosList.this);
                updateOS.setTitle("UPDATE");
                updateOS.setMessage("Do you want to update the overall parade state?");

                updateOS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent updateoverallstrength = new Intent(OverallCosList.this, Overallcos.class); //transition back to reportstrength class
                        updateoverallstrength.putExtra("putUID",uniqueid);
                        updateoverallstrength.putExtra("putDate",date);
                        updateoverallstrength.putExtra("putAMPMstatus",am_pm_status);
                        updateoverallstrength.putExtra("putOverallStr",overallstrength);
                        updateoverallstrength.putExtra("putReportedBy",reportedbyuser);
                        startActivity(updateoverallstrength);
                        finish();
                    }
                });

                updateOS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                updateOS.create().show();

                //Toast.makeText(ReportStrList.this, "Position :" + position + "info: " + reportbyuser, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //only listen when app is on the report strength page
    @Override
    protected void onStart() {
        super.onStart();
        OSadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        OSadapter.stopListening();
    }

    //when floating button is clicked
    public void floatingbtnclickOS(View view) {
        startActivity(new Intent(getApplicationContext(),Overallcos.class));
        finish();
    }

    //Inflate own menu toolbar with search function

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu.xml
        getMenuInflater().inflate(R.menu.new_reportstr_menu, menu);
        //SearchView
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //called when search button is pressed
                searchData(s); //function call to string entered in searchview as parameter
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //called anytime a single letter is entered in searchbar
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void searchData(String s) {
        //search and filter based on branches

        Query query = overallstrReference.whereEqualTo("Date", s);
        FirestoreRecyclerOptions<OverallStrModel> options = new FirestoreRecyclerOptions.Builder<OverallStrModel>().setQuery(query, OverallStrModel.class).build();

        OSadapter = new OverallStrAdapter(options);

        RecyclerView recyclerViewOS = findViewById(R.id.recycler_view_overallstr);
        recyclerViewOS.setHasFixedSize(true);
        recyclerViewOS.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOS.setAdapter(OSadapter);
        OSadapter.startListening();


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                AlertDialog.Builder removeOS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                removeOS.setTitle("DELETE");
                removeOS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                removeOS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                        OSadapter.deleteitem(viewHolder.getAdapterPosition());
                    }
                });

                removeOS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //to prevent item from disappearing when item is clicked on no.
                        OSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                });

                removeOS.create().show();
            }
        }).attachToRecyclerView(recyclerViewOS);

        //on clicking the respective cardviews
        OSadapter.setOnItemClickListener(new OverallStrAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                final String date = documentSnapshot.getString("Date");
                final String am_pm_status = documentSnapshot.getString("AM_PM_Status");
                final String overallstrength = documentSnapshot.getString("OverallStrength");
                final String reportedbyuser = documentSnapshot.getString("ReportedByUser");
                final String uniqueid = documentSnapshot.getId();

                AlertDialog.Builder updateOS = new AlertDialog.Builder(OverallCosList.this);
                updateOS.setTitle("UPDATE");
                updateOS.setMessage("Do you want to update the overall parade state?");

                updateOS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent updateoverallstrength = new Intent(OverallCosList.this, Overallcos.class); //transition back to reportstrength class
                        updateoverallstrength.putExtra("putUID",uniqueid);
                        updateoverallstrength.putExtra("putDate",date);
                        updateoverallstrength.putExtra("putAMPMstatus",am_pm_status);
                        updateoverallstrength.putExtra("putOverallStr",overallstrength);
                        updateoverallstrength.putExtra("putReportedBy",reportedbyuser);
                        startActivity(updateoverallstrength);
                        finish();
                    }
                });

                updateOS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                updateOS.create().show();

                //Toast.makeText(ReportStrList.this, "Position :" + position + "info: " + reportbyuser, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle other item clicks here ,for now not using
        return super.onOptionsItemSelected(item);
    }

}
