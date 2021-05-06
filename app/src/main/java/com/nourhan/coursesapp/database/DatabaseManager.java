package com.nourhan.coursesapp.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.nourhan.coursesapp.database.dao.CoursesDao;
import com.nourhan.coursesapp.database.dao.NotesDao;
import com.nourhan.coursesapp.models.Course;
import com.nourhan.coursesapp.models.Notes;

@Database(entities = {Course.class, Notes.class}, version = 1, exportSchema = false)
public abstract class DatabaseManager extends RoomDatabase {
    private static DatabaseManager instance;

    public abstract CoursesDao coursesDao();

    public abstract NotesDao notesDao();

    public static DatabaseManager getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(application.getApplicationContext(),
                    DatabaseManager.class,
                    "database.db")
                    .build();
        }
        return instance;
    }
}
