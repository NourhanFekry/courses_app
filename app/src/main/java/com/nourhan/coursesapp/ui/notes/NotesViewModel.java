package com.nourhan.coursesapp.ui.notes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.nourhan.coursesapp.database.DatabaseManager;
import com.nourhan.coursesapp.models.Notes;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private static DatabaseManager database;
    private final static MutableLiveData<Boolean> isAdded = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
    private final static MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();
    private final static MutableLiveData<Notes> noteLiveData = new MutableLiveData<>();
    private final static MutableLiveData<List<Notes>> notesLiveData = new MutableLiveData<>();

    public NotesViewModel(@NonNull Application application) {
        super(application);
        database = DatabaseManager.getInstance(application);
    }

    public LiveData<Boolean> addNote(Notes note) {
        new AddNote().execute(note);
        return isAdded;
    }

    public LiveData<Boolean> updateNote(Notes note) {
        new UpdateNote().execute(note);
        return isUpdated;
    }

    public LiveData<Boolean> deleteNote(Notes note) {
        new DeleteNote().execute(note);
        return isDeleted;
    }

    public LiveData<Notes> findNoteById(int id) {
        new FindNote().execute(id);
        return noteLiveData;
    }

    public LiveData<List<Notes>> getAllNotes() {
        new GetAllNotes().execute();
        return notesLiveData;
    }

    public LiveData<List<Notes>> getCourseNotes(int courseId) {
        new GetCourseNotes().execute(courseId);
        return notesLiveData;
    }

    private final static class AddNote extends AsyncTask<Notes, Void, Void> {

        @Override
        protected Void doInBackground(Notes... notes) {
            database.notesDao().insert(notes[0]);
            isAdded.postValue(true);
            return null;
        }
    }

    private final static class UpdateNote extends AsyncTask<Notes, Void, Void> {

        @Override
        protected Void doInBackground(Notes... notes) {
            database.notesDao().update(notes[0]);
            isUpdated.postValue(true);
            return null;
        }
    }

    private final static class DeleteNote extends AsyncTask<Notes, Void, Void> {

        @Override
        protected Void doInBackground(Notes... notes) {
            database.notesDao().delete(notes[0]);
            isDeleted.postValue(true);
            return null;
        }
    }

    private final static class FindNote extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            noteLiveData.postValue(database.notesDao().findById(integers[0]));
            return null;
        }
    }

    private final static class GetAllNotes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            notesLiveData.postValue(database.notesDao().getAllNotes());
            return null;
        }
    }

    private final static class GetCourseNotes extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... integers) {
            notesLiveData.postValue(database.notesDao().getCourseNotes(integers[0]));
            return null;
        }
    }
}