package com.nourhan.coursesapp.ui.courses;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nourhan.coursesapp.R;
import com.nourhan.coursesapp.models.Course;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CoursesViewHolder> {

    private List<Course> courses;
    private Context context;
    private CourseClickListener coursesClickListeners;

    public CourseAdapter(List<Course> courses, Context context, CourseClickListener coursesClickListeners) {
        this.courses = courses;
        this.context = context;
        this.coursesClickListeners = coursesClickListeners;
    }

    @NonNull
    @Override
    public CoursesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        CoursesViewHolder holder = new CoursesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesViewHolder holder, int position) {
        holder.name.setText(courses.get(position).getName());
        holder.description.setText(courses.get(position).getContent());
        holder.delete.setOnClickListener(v -> {
            coursesClickListeners.onDelete(courses.get(position));

        });
        holder.update.setOnClickListener(v -> {
            coursesClickListeners.onUpdate(courses.get(position).getId());
        });

        holder.itemView.getRootView().setOnClickListener(v -> {
            coursesClickListeners.onClick(courses.get(position).getId());
        });

    }


    @Override
    public int getItemCount() {
        return courses.size();
    }

    public class CoursesViewHolder extends RecyclerView.ViewHolder {

        TextView name, description;
        ImageView delete;
        ImageView update;

        public CoursesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.titleTV);
            description = itemView.findViewById(R.id.contentTV);
            delete = itemView.findViewById(R.id.deleteIV);
            update = itemView.findViewById(R.id.editIV);

        }
    }
}
