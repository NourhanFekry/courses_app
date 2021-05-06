package com.nourhan.coursesapp.ui.courses;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.nourhan.coursesapp.R;
import com.nourhan.coursesapp.databinding.CoursesFragmentBinding;
import com.nourhan.coursesapp.models.Course;

public class CoursesFragment extends Fragment implements CourseClickListener {

    private CoursesViewModel mViewModel;
    private CoursesFragmentBinding binding;
    private Bundle bundle;

    public static CoursesFragment newInstance() {
        return new CoursesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = CoursesFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this,
                new CoursesViewModelFactory(requireActivity().getApplication()))
                .get(CoursesViewModel.class);

        requireActivity().setTitle(getString(R.string.courses));

        binding.fabAddCourse.setOnClickListener(v ->
                Navigation.findNavController(binding.getRoot())
                        .navigate(R.id.action_nav_courses_to_addEditCourseFragment)
        );

        getCourses();
        return binding.getRoot();
    }

    private void getCourses() {
        try {
            mViewModel.getAllCourses().observe(getViewLifecycleOwner(), courses -> {
                if (courses.size() > 0) {
                    binding.noCoursesTV.setVisibility(View.GONE);
                    binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recyclerView.setAdapter(new CourseAdapter(courses,getContext(),this));
                } else {
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.noCoursesTV.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(int id) {
        bundle = new Bundle();
        bundle.putInt(getString(R.string.course_id), id);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.nav_notes, bundle);
    }

    @Override
    public void onUpdate(int id) {
        bundle = new Bundle();
        bundle.putInt(getString(R.string.course_id), id);
        bundle.putBoolean(getString(R.string.is_edit), true);
        Navigation.findNavController(binding.getRoot())
                .navigate(R.id.action_nav_courses_to_addEditCourseFragment, bundle);

    }

    @Override
    public void onDelete(Course course) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.delete_confirmation);
            builder.setMessage(R.string.delete_course_confirmation);
            builder.setCancelable(false);
            builder.setNegativeButton(R.string.yes, (dialog, which) ->
                   deleteCourse(course));
            builder.setPositiveButton(R.string.no, null);
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCourse(Course course) {
        try {
            mViewModel.deleteCourse(course)
                    .observe(getViewLifecycleOwner(), isDeleted -> {
                        if (isDeleted) {
                            getCourses();
                        } else {
                            Toast.makeText(requireContext(),
                                    getString(R.string.failed_to_delete_course) + course.getName(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}