package com.nourhan.coursesapp.ui.courses;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nourhan.coursesapp.R;
import com.nourhan.coursesapp.databinding.FragmentAddCourseBinding;
import com.nourhan.coursesapp.models.Course;

public class AddCourseFragment extends Fragment {

    private FragmentAddCourseBinding binding;
    private CoursesViewModel mViewModel;
    private int id;
    private boolean isEdit;
    private Course mCourse;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setTitle(getString(R.string.new_course));
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddCourseBinding.inflate(inflater, container, false);
        try {
            mViewModel =
                    new ViewModelProvider(this,
                            new CoursesViewModelFactory(requireActivity().getApplication()))
                            .get(CoursesViewModel.class);

            if (getArguments() != null) {
                id = requireArguments().getInt(getString(R.string.course_id), 0);
                isEdit = requireArguments().getBoolean(getString(R.string.is_edit), false);
            }

            if (isEdit && id > 0) {
                requireActivity().setTitle(getString(R.string.update_course));
                loadCurrentCourse(id);
            }

            binding.addButton.setOnClickListener(v -> {
                if (isEdit) updateCourse();
                else addCourse();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return binding.getRoot();
    }

    private void addCourse() {
        try {
            Course course = new Course(binding.courseNameET.getText().toString(),
                    binding.courseDescriptionET.getText().toString());
            mViewModel.addCourse(course).observe(getViewLifecycleOwner(), isAdded -> {
                if (isAdded) {
                    Toast.makeText(requireContext(),
                            getString(R.string.success),
                            Toast.LENGTH_LONG)
                            .show();
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_addCourseFragment_to_nav_courses);
                } else {
                    Toast.makeText(requireContext(),
                            getString(R.string.failed_to_add_course),
                            Toast.LENGTH_LONG)
                            .show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(),
                    e.getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void updateCourse() {
        try {
            mCourse.setName(binding.courseNameET.getText().toString());
            mCourse.setContent(binding.courseDescriptionET.getText().toString());
            mViewModel.updateCourse(mCourse).observe(getViewLifecycleOwner(), isUpdated -> {
                if (isUpdated) {
                    Navigation.findNavController(binding.getRoot())
                            .navigate(R.id.action_addCourseFragment_to_nav_courses);
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

    private void loadCurrentCourse(int id) {
        try {
            mViewModel.findCourseById(id).observe(getViewLifecycleOwner(), course -> {
                mCourse = course;
                binding.courseNameET.setText(course.getName());
                binding.courseDescriptionET.setText(course.getContent());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}