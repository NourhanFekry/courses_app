package com.nourhan.coursesapp.ui.notes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.nourhan.coursesapp.R;
import com.nourhan.coursesapp.databinding.FragmentAddNoteBinding;
import com.nourhan.coursesapp.models.Course;
import com.nourhan.coursesapp.models.Notes;
import com.nourhan.coursesapp.ui.courses.CoursesViewModel;
import com.nourhan.coursesapp.ui.courses.CoursesViewModelFactory;

import java.util.List;

public class AddNoteFragment extends Fragment {

    private FragmentAddNoteBinding binding;
    private CoursesViewModel coursesViewModel;
    private NotesViewModel notesViewModel;
    private List<Course> courses;
    private Course selectedCourse;
    private Notes currentNote;
    private int id;
    private boolean isEdit;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false);
        coursesViewModel = new ViewModelProvider(this,
                new CoursesViewModelFactory(requireActivity().getApplication()))
                .get(CoursesViewModel.class);
        notesViewModel =
                new ViewModelProvider(this,
                        new NotesViewModelFactory(requireActivity().getApplication()))
                        .get(NotesViewModel.class);

        getCourses();

        if (getArguments() != null) {
            id = requireArguments().getInt(getString(R.string.note_id), 0);
            isEdit = requireArguments().getBoolean(getString(R.string.is_edit), false);
        }
        requireActivity().setTitle(getString(R.string.new_note));

        if (isEdit && id > 0) {
            requireActivity().setTitle(getString(R.string.update_note));
            loadCurrentNote(id);
        }

        binding.addButton.setOnClickListener(v -> {
            if (isEdit) updateNote();
            else addNote();
        });
        return binding.getRoot();
    }

    private void getCourses() {
        coursesViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
            this.courses = courses;
            CoursesSpinnerAdapter adapter =
                    new CoursesSpinnerAdapter(requireContext(),
                            android.R.layout.simple_spinner_item,
                            courses);
            binding.coursesSpinner.setAdapter(adapter);
            binding.coursesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectedCourse = adapter.getItem(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        });
    }

    private void addNote() {
        try {
            Notes note = new Notes(selectedCourse.getId(),
                    binding.noteNameET.getText().toString(),
                    binding.noteDescriptionET.getText().toString());
            notesViewModel.addNote(note).observe(getViewLifecycleOwner(), isAdded -> {
                if (isAdded) {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_addNoteFragment_to_nav_notes);
                } else {
                    Toast.makeText(requireContext(),
                            getString(R.string.failed_to_add_course),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateNote() {
        try {
            currentNote.setCourseId(selectedCourse.getId());
            currentNote.setTitle(binding.noteNameET.getText().toString());
            currentNote.setContent(binding.noteDescriptionET.getText().toString());
            notesViewModel.updateNote(currentNote).observe(getViewLifecycleOwner(), isUpdated -> {
                if (isUpdated) {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_addNoteFragment_to_nav_notes);
                } else {
                    Toast.makeText(requireContext(),
                            getString(R.string.failed_to_add_course),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCurrentNote(int id) {
        try {
            notesViewModel.findNoteById(id).observe(getViewLifecycleOwner(), note -> {
                currentNote = note;

                for (int i = 0; i < courses.size(); i++) {
                    if (courses.get(i).getId() == note.getCourseId()) {
                        binding.coursesSpinner.setSelection(i);
                    }
                }
                binding.noteNameET.setText(note.getTitle());
                binding.noteDescriptionET.setText(note.getContent());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}