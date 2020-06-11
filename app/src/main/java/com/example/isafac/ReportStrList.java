package com.example.isafac;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ReportStrList extends AppCompatActivity {

    String userID, Branch;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth =  FirebaseAuth.getInstance(); //get connecting instance with firebase authentication to perform operations on the database
    private ReportStrAdapter RSadapter;
    CollectionReference reportstrReference = fStore.collection("Report_Strength");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_str_list);

        userID = fAuth.getCurrentUser().getUid(); // get unique ID of user who is logged in.

        DocumentReference documentReference = fStore.collection("Users").document(userID); // state which collection and document you are going to retrieve
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() { //addSnapshotListener -> listen to any data changes and retrieve data from firestore
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Branch = documentSnapshot.getString("Branch");
                setupRSrecyclerview();
               RSadapter.startListening();
            }
        });

    }

    private void setupRSrecyclerview() {

            if (Branch.contains("HR"))
            {
                Query query = reportstrReference.whereEqualTo("Branch", "HR").orderBy("Date", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

                RSadapter = new ReportStrAdapter(options);

                RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
                recyclerViewRS.setHasFixedSize(true);
                recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRS.setAdapter(RSadapter);

                //On swiping the cardview
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                        removeRS.setTitle("DELETE");
                        removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                        removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                                RSadapter.deleteitem(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //to prevent item from disappearing when item is clicked on no.
                                RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.create().show();
                    }
                }).attachToRecyclerView(recyclerViewRS);
            }

            else if (Branch.contains("OPS"))
            {
                Query query = reportstrReference.whereEqualTo("Branch", "OPS").orderBy("Date", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

                RSadapter = new ReportStrAdapter(options);

                RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
                recyclerViewRS.setHasFixedSize(true);
                recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRS.setAdapter(RSadapter);

                //On swiping the cardview
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                        removeRS.setTitle("DELETE");
                        removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                        removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                                RSadapter.deleteitem(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //to prevent item from disappearing when item is clicked on no.
                                RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.create().show();
                    }
                }).attachToRecyclerView(recyclerViewRS);
            }

            else if (Branch.contains("CDB"))
            {
                Query query = reportstrReference.whereEqualTo("Branch", "CDB").orderBy("Date", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

                RSadapter = new ReportStrAdapter(options);

                RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
                recyclerViewRS.setHasFixedSize(true);
                recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRS.setAdapter(RSadapter);

                //On swiping the cardview
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                        removeRS.setTitle("DELETE");
                        removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                        removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                                RSadapter.deleteitem(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //to prevent item from disappearing when item is clicked on no.
                                RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.create().show();
                    }
                }).attachToRecyclerView(recyclerViewRS);
            }

            else if (Branch.contains("MSB") || Branch.contains("ADMIN"))
            {
                Query query = reportstrReference.whereEqualTo("Branch", "MSB").orderBy("Date", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

                RSadapter = new ReportStrAdapter(options);

                RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
                recyclerViewRS.setHasFixedSize(true);
                recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRS.setAdapter(RSadapter);

                //On swiping the cardview
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                        removeRS.setTitle("DELETE");
                        removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                        removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                                RSadapter.deleteitem(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //to prevent item from disappearing when item is clicked on no.
                                RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.create().show();
                    }
                }).attachToRecyclerView(recyclerViewRS);
            }

            else if (Branch.contains("ESB"))
            {
                Query query = reportstrReference.whereEqualTo("Branch", "EBS").orderBy("Date", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

                RSadapter = new ReportStrAdapter(options);

                RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
                recyclerViewRS.setHasFixedSize(true);
                recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRS.setAdapter(RSadapter);

                //On swiping the cardview
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                        removeRS.setTitle("DELETE");
                        removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                        removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                                RSadapter.deleteitem(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //to prevent item from disappearing when item is clicked on no.
                                RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.create().show();
                    }
                }).attachToRecyclerView(recyclerViewRS);
            }

            else if (Branch.contains("LOGS"))
            {
                Query query = reportstrReference.whereEqualTo("Branch", "LOGS").orderBy("Date", Query.Direction.DESCENDING);
                FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

                RSadapter = new ReportStrAdapter(options);

                RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
                recyclerViewRS.setHasFixedSize(true);
                recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
                recyclerViewRS.setAdapter(RSadapter);

                //On swiping the cardview
                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                        AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                        removeRS.setTitle("DELETE");
                        removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                        removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                                RSadapter.deleteitem(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //to prevent item from disappearing when item is clicked on no.
                                RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                            }
                        });

                        removeRS.create().show();
                    }
                }).attachToRecyclerView(recyclerViewRS);
            } else {
                Toast.makeText(this, "Error: Unable to retrieve branch database details", Toast.LENGTH_SHORT).show();
            }





        //on clicking the respective cardviews
        RSadapter.setOnItemClickListener(new ReportStrAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                final String date = documentSnapshot.getString("Date");
                final String branch = documentSnapshot.getString("Branch");
                final String am_pm_status = documentSnapshot.getString("AM_PM_Status");
                final String reportedstrength = documentSnapshot.getString("ReportedStrength");
                final String reportedbyuser = documentSnapshot.getString("ReportedByUser");
                final String uniqueid = documentSnapshot.getId();

                AlertDialog.Builder updateRS = new AlertDialog.Builder(ReportStrList.this);
                updateRS.setTitle("UPDATE");
                updateRS.setMessage("Do you want to update the parade state?");

                updateRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent updatestrength = new Intent(ReportStrList.this, ReportStrength.class); //transition back to reportstrength class
                        updatestrength.putExtra("putUID",uniqueid);
                        updatestrength.putExtra("putDate",date);
                        updatestrength.putExtra("putBranch",branch);
                        updatestrength.putExtra("putAMPMstatus",am_pm_status);
                        updatestrength.putExtra("putReportedStr",reportedstrength);
                        updatestrength.putExtra("putReportedBy",reportedbyuser);
                        startActivity(updatestrength);
                        finish();
                    }
                });

                updateRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                updateRS.create().show();

                //Toast.makeText(ReportStrList.this, "Position :" + position + "info: " + reportbyuser, Toast.LENGTH_SHORT).show();
            }
        });
    }


    //only listen when app is on the report strength page
    @Override
    protected void onStart() {
        super.onStart();
       //RSadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
       RSadapter.stopListening();
    }

    //when floating button is clicked
    public void floatingbtnclickRS(View view) {
        startActivity(new Intent(getApplicationContext(),ReportStrength.class));
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
        if (Branch.contains("HR"))
        {
            Query query = reportstrReference.whereEqualTo("Date", s).whereEqualTo("Branch", "HR");
            FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

            RSadapter = new ReportStrAdapter(options);

            RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
            recyclerViewRS.setHasFixedSize(true);
            recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRS.setAdapter(RSadapter);
            RSadapter.startListening();

            //On swiping the cardview
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                    AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    removeRS.setTitle("DELETE");
                    removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                    removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                            RSadapter.deleteitem(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //to prevent item from disappearing when item is clicked on no.
                            RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.create().show();
                }
            }).attachToRecyclerView(recyclerViewRS);
        }

        else if (Branch.contains("OPS"))
        {
            Query query = reportstrReference.whereEqualTo("Date", s).whereEqualTo("Branch", "OPS");
            FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

            RSadapter = new ReportStrAdapter(options);

            RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
            recyclerViewRS.setHasFixedSize(true);
            recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRS.setAdapter(RSadapter);
            RSadapter.startListening();

            //On swiping the cardview
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                    AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    removeRS.setTitle("DELETE");
                    removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                    removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                            RSadapter.deleteitem(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //to prevent item from disappearing when item is clicked on no.
                            RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.create().show();
                }
            }).attachToRecyclerView(recyclerViewRS);
        }

       else if (Branch.contains("CDB"))
        {
            Query query = reportstrReference.whereEqualTo("Date", s).whereEqualTo("Branch", "CDB");
            FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

            RSadapter = new ReportStrAdapter(options);

            RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
            recyclerViewRS.setHasFixedSize(true);
            recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRS.setAdapter(RSadapter);
            RSadapter.startListening();

            //On swiping the cardview
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                    AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    removeRS.setTitle("DELETE");
                    removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                    removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                            RSadapter.deleteitem(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //to prevent item from disappearing when item is clicked on no.
                            RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.create().show();
                }
            }).attachToRecyclerView(recyclerViewRS);
        }

        else if (Branch.contains("MSB"))
        {
            Query query = reportstrReference.whereEqualTo("Date", s).whereEqualTo("Branch", "MSB");
            FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

            RSadapter = new ReportStrAdapter(options);

            RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
            recyclerViewRS.setHasFixedSize(true);
            recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRS.setAdapter(RSadapter);
            RSadapter.startListening();

            //On swiping the cardview
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                    AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    removeRS.setTitle("DELETE");
                    removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                    removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                            RSadapter.deleteitem(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //to prevent item from disappearing when item is clicked on no.
                            RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.create().show();
                }
            }).attachToRecyclerView(recyclerViewRS);
        }

        else if (Branch.contains("ESB"))
        {
            Query query = reportstrReference.whereEqualTo("Date", s).whereEqualTo("Branch", "ESB");
            FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

            RSadapter = new ReportStrAdapter(options);

            RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
            recyclerViewRS.setHasFixedSize(true);
            recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRS.setAdapter(RSadapter);
            RSadapter.startListening();

            //On swiping the cardview
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                    AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    removeRS.setTitle("DELETE");
                    removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                    removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                            RSadapter.deleteitem(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //to prevent item from disappearing when item is clicked on no.
                            RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.create().show();
                }
            }).attachToRecyclerView(recyclerViewRS);
        }

        else if (Branch.contains("LOGS"))
        {
            Query query = reportstrReference.whereEqualTo("Date", s).whereEqualTo("Branch", "LOGS");
            FirestoreRecyclerOptions<ReportStrModel> options = new FirestoreRecyclerOptions.Builder<ReportStrModel>().setQuery(query, ReportStrModel.class).build();

            RSadapter = new ReportStrAdapter(options);

            RecyclerView recyclerViewRS = findViewById(R.id.recycler_view_reportstr);
            recyclerViewRS.setHasFixedSize(true);
            recyclerViewRS.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewRS.setAdapter(RSadapter);
            RSadapter.startListening();

            //On swiping the cardview
            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                    AlertDialog.Builder removeRS = new AlertDialog.Builder(viewHolder.itemView.getContext());
                    removeRS.setTitle("DELETE");
                    removeRS.setMessage("Are you sure to delete this content? It will be permanently removed.");

                    removeRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //close the dialog , do need to put anything here because the default builder will dismiss/close on itself
                            RSadapter.deleteitem(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //to prevent item from disappearing when item is clicked on no.
                            RSadapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        }
                    });

                    removeRS.create().show();
                }
            }).attachToRecyclerView(recyclerViewRS);
        } else {
            Toast.makeText(ReportStrList.this, "Error in searching" , Toast.LENGTH_SHORT).show();
        }


        //on clicking the respective cardviews
        RSadapter.setOnItemClickListener(new ReportStrAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                final String date = documentSnapshot.getString("Date");
                final String branch = documentSnapshot.getString("Branch");
                final String am_pm_status = documentSnapshot.getString("AM_PM_Status");
                final String reportedstrength = documentSnapshot.getString("ReportedStrength");
                final String reportedbyuser = documentSnapshot.getString("ReportedByUser");
                final String uniqueid = documentSnapshot.getId();

                AlertDialog.Builder updateRS = new AlertDialog.Builder(ReportStrList.this);
                updateRS.setTitle("UPDATE");
                updateRS.setMessage("Do you want to update the parade state?");

                updateRS.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent updatestrength = new Intent(ReportStrList.this, ReportStrength.class); //transition back to reportstrength class
                        updatestrength.putExtra("putUID",uniqueid);
                        updatestrength.putExtra("putDate",date);
                        updatestrength.putExtra("putBranch",branch);
                        updatestrength.putExtra("putAMPMstatus",am_pm_status);
                        updatestrength.putExtra("putReportedStr",reportedstrength);
                        updatestrength.putExtra("putReportedBy",reportedbyuser);
                        startActivity(updatestrength);
                        finish();
                    }
                });

                updateRS.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                updateRS.create().show();

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
