package com.example.tripreminderapp.ui.upcoming_trips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminderapp.R;
import com.example.tripreminderapp.database.trip.Trip;

import java.util.List;

public class UpcomingTripAdapter extends RecyclerView.Adapter<UpcomingTripAdapter.ViewHolder> {
    public AddNoteClickListener setAddNoteClickListener = null;
    public TripClickListener setTripClickListener = null;
    public List<Trip> data = null;

    //Mido
    public StartTrip setStartTrip = null;
    public DeletTrip setDeletTrip = null;


    public  void changeData(List<Trip> tripsData) {
        this.data = tripsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = data.get(position);
        holder.nameTv.setText(trip.getName());
        holder.startPointTv.setText(trip.getStartPoint());
        holder.endPointTv.setText(trip.getEndPoint());
        holder.dateTv.setText(trip.getDate());
        holder.timeTv.setText(trip.getTime());
        if (setAddNoteClickListener != null) {
            holder.addNoteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAddNoteClickListener.onClick(trip);
                }
            });
        }

        if (setTripClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setTripClickListener.onClick(trip);
                }
            });
        }


        //Mido
        if (setStartTrip != null) { //java is not safty
            holder.startIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStartTrip.onClick(trip);
                }
            });
        }



        if(setDeletTrip != null){
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDeletTrip.onClick(trip);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (data == null)
            return 0;
        else
            return data.size();
    }

    public interface AddNoteClickListener {
        void onClick(Trip trip);
    }

    public interface TripClickListener {
        void onClick(Trip trip);
    }

    //Mido
    //to start trip to destinatio
    public interface StartTrip {
        void onClick(Trip trip);
    }

    //Mido
    //to delete trip
    public interface DeletTrip{
        void onClick(Trip trip);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTv;
        TextView startPointTv;
        TextView endPointTv;
        TextView timeTv;
        TextView dateTv;
        ImageView addNoteIv;
        ImageView startIv;
        ImageView deleteIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.item_tv_name);
            startPointTv = itemView.findViewById(R.id.item_tv_start);
            endPointTv = itemView.findViewById(R.id.item_tv_end);
            dateTv = itemView.findViewById(R.id.item_tv_date);
            timeTv = itemView.findViewById(R.id.item_tv_time);
            addNoteIv = itemView.findViewById(R.id.item_iv_addNote);
            startIv = itemView.findViewById(R.id.item_iv_start);
            deleteIv = itemView.findViewById(R.id.delete_trip);
        }

    }

}

