package com.nourhan.coursesapp.ui.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nourhan.coursesapp.R;
import com.nourhan.coursesapp.databinding.NotesFragmentBinding;
import com.nourhan.coursesapp.models.Notes;

import java.util.List;

public class NotesFragment extends Fragment implements NoteClickListener {

    private NotesViewModel mViewModel;
    private NotesFragmentBinding binding;
    private int courseId = 0;
    Bundle bundle;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = NotesFragmentBinding.inflate(inflater, container, false);
        mViewModel =
                new ViewModelProvider(this,
                        new NotesViewModelFactory(requireActivity().getApplication()))
                        .get(NotesViewModel.class);

        if (getArguments() != null) {
            courseId = requireArguments().getInt(getString(R.string.course_id), 0);
            requireActivity().setTitle(getString(R.string.course_notes));
            getCourseNotes(courseId);
        } else {
            requireActivity().setTitle(getString(R.string.all_notes));
            loadAllNotes();
        }

        binding.fabAddNote.setOnClickListener(v ->
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_nav_notes_to_addNoteFragment));

        return binding.getRoot();
    }

    private void getCourseNotes(int courseId) {
        mViewModel.getCourseNotes(courseId).observe(getViewLifecycleOwner(), notes -> {
            if (notes.size() > 0) {
                initRecyclerView(notes);
            } else {
                binding.recyclerView.setVisibility(View.GONE);
                binding.noNotesTV.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadAllNotes() {
        try {
            mViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
                if (notes.size() > 0) {
                    initRecyclerView(notes);
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.noNotesTV.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initRecyclerView(List<Notes> notes) {
        try {
            RecyclerView recyclerView = binding.recyclerView;
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            recyclerView.setHasFixedSize(true);
            NotesAdapter adapter = new NotesAdapter(notes,getContext(),this);
            recyclerView.setAdapter(adapter);
            recyclerView.setVisibility(View.VISIBLE);
            binding.noNotesTV.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(int id) {
        bundle = new Bundle();
        bundle.putInt(getString(R.string.note_id), id);
        bundle.putBoolean(getString(R.string.is_edit), true);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_nav_notes_to_addNoteFragment, bundle);
    }

    @Override
    public void onDelete(Notes note) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.delete_confirmation);
            builder.setMessage(R.string.delete_confirmation);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.yes, (dialog, which) ->
                    deleteNote(note));
            builder.setPositiveButton(R.string.no, null);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteNote(Notes note) {
        try {
            mViewModel.deleteNote(note)
                    .observe(getViewLifecycleOwner(), isDeleted -> {
                        if (isDeleted) {
                            if (courseId == 0) {
                                loadAllNotes();
                            } else {
                                getCourseNotes(courseId);
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    getString(R.string.failed_to_add_course) + note.getTitle(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}