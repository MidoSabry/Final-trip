package com.example.tripreminderapp.ui.trip_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminderapp.database.TripDatabase;
import com.example.tripreminderapp.database.note.Note;
import com.example.tripreminderapp.database.trip.Trip;

import java.util.List;

public class TripDetailsViewModel extends AndroidViewModel {
    public TripDetailsViewModel(@NonNull Application application) {
        super(application);
    }
    private final TripDatabase database =TripDatabase.getInstance(getApplication());
    private MutableLiveData<List<Note>> notesLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isEditable = new MutableLiveData<>(false);

    public void getNotesFromDatabase(int id){
        notesLiveData.setValue(database.noteDao().getNotes(id));
    }

    public void deleteNoteFromDatabase(Note note){
        database.noteDao().delete(note);
    }

    public void updateTripInDatabase(Trip trip){
       if (isEditable.getValue()) {
//           database.tripDao().updateTrip(trip.getName(), trip.getStartPoint(), trip.getEndPoint(), trip.getDate(), trip.getTime(), trip.getDate() + " " + trip.getTime(), trip.getIsAlarmPrepared(),false,trip.getSpinner(), trip.getId());
           database.tripDao().update(trip);
           isEditable.setValue(false);
       }else {
           isEditable.setValue(true);
       }
       }

    public MutableLiveData<List<Note>> getNotesLiveData() {
        return notesLiveData;
    }

    public MutableLiveData<Boolean> getIsEditable() {
        return isEditable;
    }



}
