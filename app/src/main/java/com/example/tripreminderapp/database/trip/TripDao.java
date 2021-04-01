package com.example.tripreminderapp.database.trip;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDao {
    @Query("SELECT * FROM Trip order by date_time asc ")
    List<Trip> getAll();

    @Query("SELECT * FROM Trip WHERE isOK = 1  order by date_time asc ")
    List<Trip> getTripDone();



    @Insert
    void insertTrip(Trip trip);

    @Delete
    void delete(Trip trip);


    @Update
    void update(Trip trip);

//    @Query("update Trip set name = :name , startPoint = :startPoint , endPoint = :endPoint , date = :date , time = :time , date_time = :date_time , isAlarmPrepared = :isAlarmed , isOK = :isDone, spinner = :spinner  where id = :id ")
//    void updateTrip(String name, String startPoint, String endPoint, String date, String time, String date_time, boolean isAlarmed, boolean isDone, String spinner, int id);
}
