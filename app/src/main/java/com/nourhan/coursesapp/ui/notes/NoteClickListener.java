package com.nourhan.coursesapp.ui.notes;


import com.nourhan.coursesapp.models.Notes;

public interface NoteClickListener {
    void onUpdate(int Id);

    void onDelete(Notes note);
}
