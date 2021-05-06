package com.nourhan.coursesapp.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.nourhan.coursesapp.models.Notes;

import java.util.List;

@Dao
public interface NotesDao {
    @Insert
    void insert(Notes note);

    @Update
    void update(Notes note);

    @Delete
    void delete(Notes note);

    @Query("SELECT * FROM notes WHERE id = :id")
    Notes findById(int id);

    @Query("SELECT * FROM notes WHERE 1")
    List<Notes> getAllNotes();

    @Query("SELECT * FROM notes WHERE courseId = :courseId")
    List<Notes> getCourseNotes(int courseId);
}
