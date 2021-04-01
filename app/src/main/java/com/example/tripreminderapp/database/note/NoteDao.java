package com.example.tripreminderapp.database.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM Note where tripId = :id")
    List<Note> getNotes(int id);

    @Insert
    void insertNote(Note note);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);
}
