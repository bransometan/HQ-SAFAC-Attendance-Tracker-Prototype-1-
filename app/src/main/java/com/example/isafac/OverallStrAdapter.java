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

public class OverallStrAdapter extends FirestoreRecyclerAdapter<OverallStrModel, OverallStrAdapter.OverallStrHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */

    private OverallStrAdapter.OnItemClickListener listener;

    public OverallStrAdapter(@NonNull FirestoreRecyclerOptions<OverallStrModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull OverallStrHolder holder, int position, @NonNull OverallStrModel model) {
        holder.Date.setText("Date: " + model.getDate());
        holder.AmPmStatus.setText(model.getAM_PM_Status());
        //holder.OverallStrStatus.setVisibility(View.GONE); -> to remove view from user
        holder.OverallStrStatus.setText("\nOverall Strength: \n\n" + model.getOverallStrength() +"\n");
        holder.ReportedBy.setText("Reported By: " + model.getReportedByUser());

        if (model.getAM_PM_Status().contains("AM"))
        {
            //if AM light blue color
            holder.OS_CardView.setCardBackgroundColor(Color.rgb(200, 230, 255));
        }

        if (model.getAM_PM_Status().contains("PM"))
        {
            //if PM light yellow color
            holder.OS_CardView.setCardBackgroundColor(Color.rgb(255, 252, 200));
        }
    }

    @NonNull
    @Override
    public OverallStrHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.overallstr_item,parent,false);
        return new OverallStrHolder(v);
    }

    public void deleteitem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class OverallStrHolder extends RecyclerView.ViewHolder {

        TextView Date;
        TextView AmPmStatus;
        TextView OverallStrStatus;
        TextView ReportedBy;
        CardView OS_CardView;

        public OverallStrHolder(@NonNull View itemView) {
            super(itemView);

            Date = itemView.findViewById(R.id.OS_DateCardView);
            AmPmStatus = itemView.findViewById(R.id.OS_AmPmStatusCardView);
            OverallStrStatus = itemView.findViewById(R.id.OS_OverallStrCardView);
            ReportedBy = itemView.findViewById(R.id.OS_ReportedByCardView);
            OS_CardView = itemView.findViewById(R.id.OS_CardViewLayout);

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

    public void setOnItemClickListener(OverallStrAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
