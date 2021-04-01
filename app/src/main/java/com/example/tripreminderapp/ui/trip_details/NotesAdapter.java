package com.example.tripreminderapp.ui.trip_details;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminderapp.R;
import com.example.tripreminderapp.database.note.Note;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    public NoteClickListener setNoteClickListener = null;
    public NoteDeleteClickListener setNoteDeleteClickListener = null;
    private List<Note> data = null;

    public void changeData(List<Note> tripsData) {
        this.data = tripsData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Note note = data.get(position);
        holder.titleTv.setText(note.getTitle());
        holder.descTv.setText(note.getDescription());

        if (setNoteClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNoteClickListener.onClick(note);
                }
            });
        }

        if (setNoteDeleteClickListener != null) {
            holder.deleteIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setNoteDeleteClickListener.onClick(note);
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

    public interface NoteClickListener {
        void onClick(Note note);
    }

    public interface NoteDeleteClickListener {
        void onClick(Note note);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv;
        TextView descTv;
        ImageView deleteIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.tv_note_item_tilte);
            descTv = itemView.findViewById(R.id.tv_note_item_desc);
            deleteIv = itemView.findViewById(R.id.iv_item_note_delete);
        }
    }

}

