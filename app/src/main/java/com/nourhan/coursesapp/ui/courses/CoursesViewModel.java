package com.nourhan.coursesapp.ui.courses;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nourhan.coursesapp.database.DatabaseManager;
import com.nourhan.coursesapp.models.Course;

import java.util.List;

public class CoursesViewModel extends AndroidViewModel {


    public CoursesViewModel(@NonNull Application application) {
        super(application);
        database = DatabaseManager.getInstance(application);
    }

    private static DatabaseManager database;
    private static final MutableLiveData<Boolean> isAdded = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
    private static final MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();
    private static final MutableLiveData<Course> courseLiveData = new MutableLiveData<>();
    private static final MutableLiveData<List<Course>> coursesLiveData = new MutableLiveData<>();

    public LiveData<Boolean> addCourse(Course course) {
        new AddCourse().execute(course);
        return isAdded;
    }

    public LiveData<Boolean> updateCourse(Course course) {
        new UpdateCourse().execute(course);
        return isUpdated;
    }

    public LiveData<Boolean> deleteCourse(Course course) {
        new DeleteCourse().execute(course);
        return isDeleted;
    }

    public LiveData<Course> findCourseById(int id) {
        new FindCourse().execute(id);
        return courseLiveData;
    }

    public LiveData<List<Course>> getAllCourses() {
        new GetAllCourses().execute();
        return coursesLiveData;
    }


    private final static class AddCourse extends AsyncTask<Course, Void, Void> {

        @Override
        protected Void doInBackground(Course... courses) {
            database.coursesDao().insert(courses[0]);
            isAdded.postValue(true);
            return null;
        }
    }

    private final static class UpdateCourse extends AsyncTask<Course, Void, Void> {

        @Override
        protected Void doInBackground(Course... courses) {
            database.coursesDao().update(courses[0]);
            isUpdated.postValue(true);
            return null;
        }
    }

    private final static class DeleteCourse extends AsyncTask<Course, Void, Void> {

        @Override
        protected Void doInBackground(Course... courses) {
            database.coursesDao().delete(courses[0]);
            isDeleted.postValue(true);
            return null;
        }
    }

    private final static class FindCourse extends AsyncTask<Integer, Void, Void> {

        @Override
        protected Void doInBackground(Integer... integers) {
            courseLiveData.postValue(database.coursesDao().findById(integers[0]));
            return null;
        }
    }

    private final static class GetAllCourses extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            coursesLiveData.postValue(database.coursesDao().getAllCourses());
            return null;
        }
    }
}
