package com.example.tripreminderapp.ui.add_trip;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.tripreminderapp.database.TripDatabase;
import com.example.tripreminderapp.database.trip.Trip;

public class AddTripViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> isInsertedLiveData =new MutableLiveData<Boolean>(false);
    private TripDatabase tripDatabase = TripDatabase.getInstance(getApplication());

    public AddTripViewModel(@NonNull Application application) {
        super(application);
    }

    public void insertTrip(Trip trip){
        tripDatabase.tripDao().insertTrip(trip);
        isInsertedLiveData.setValue(true);
    }

    public MutableLiveData<Boolean> getIsInsertedLiveData() {
        return isInsertedLiveData;
    }

}
