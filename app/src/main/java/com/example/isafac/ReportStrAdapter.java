package com.example.isafac;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class ReportStrAdapter extends FirestoreRecyclerAdapter<ReportStrModel, ReportStrAdapter.ReportStrHolder> {
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OnItemClickListener listener;

    public ReportStrAdapter(@NonNull FirestoreRecyclerOptions<ReportStrModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReportStrHolder holder, int position, @NonNull ReportStrModel model) {
        holder.Date.setText("Date: " + model.getDate());
       holder.Branch.setText(model.getBranch());
        holder.AmPmStatus.setText("\nAM/PM Status: " + model.getAM_PM_Status() +"\n");
        //holder.ReportStrStatus.setVisibility(View.GONE); -> to remove view from user
        holder.ReportStrStatus.setText("Reported Strength: \n\n" + model.getReportedStrength() +"\n");
       holder.ReportedBy.setText("Reported By: " + model.getReportedByUser());

        //holder.Date.setText(Html.fromHtml("<b>Date: </b>" + model.getDate()));
        //holder.Branch.setText(model.getBranch()); // default already bold set in reportstr_item xml
        //holder.AmPmStatus.setText(Html.fromHtml("<b>AM/PM Status: </b>" + model.getAM_PM_Status() + "\n"));
        //holder.ReportStrStatus.setText(Html.fromHtml("<b>Reported Strength: </b>" + "<br />" + model.getReportedStrength() + "\n"));
       // holder.ReportedBy.setText(Html.fromHtml("<b>Reported By: </b>" + model.getReportedByUser()));

        if (model.getAM_PM_Status().contains("AM"))
        {
            //if AM light blue color
            holder.RS_CardView.setCardBackgroundColor(Color.rgb(200, 230, 255));
        }

        if (model.getAM_PM_Status().contains("PM"))
        {
            //if PM light yellow color
            holder.RS_CardView.setCardBackgroundColor(Color.rgb(255, 252, 200));
        }

    }

    @NonNull
    @Override
    public ReportStrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reportstr_item,parent,false);
        return new ReportStrHolder(v);
    }

    public void deleteitem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class ReportStrHolder extends RecyclerView.ViewHolder {

        TextView Date;
        TextView Branch;
        TextView AmPmStatus;
        TextView ReportStrStatus;
        TextView ReportedBy;
        CardView RS_CardView;

        public ReportStrHolder(@NonNull View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.RS_DateCardView);
            Branch = itemView.findViewById(R.id.RS_BranchCardView);
            AmPmStatus = itemView.findViewById(R.id.RS_AmPmStatusCardView);
            ReportStrStatus = itemView.findViewById(R.id.RS_ReportedStrCardView);
            ReportedBy = itemView.findViewById(R.id.RS_reportedbyCardView);
            RS_CardView = itemView.findViewById(R.id.RS_CardViewLayout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
