package com.nourhan.coursesapp.ui.courses;

import com.nourhan.coursesapp.models.Course;

public interface CourseClickListener {

    void onClick(int id);

    void onUpdate(int id);

    void onDelete(Course course);
}
