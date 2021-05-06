package com.nourhan.coursesapp.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.nourhan.coursesapp.R;
import com.nourhan.coursesapp.models.Notes;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private List<Notes> notes;
    private Context context;
    private NoteClickListener noteClickListener;

    public NotesAdapter(List<Notes> notes, Context context, NoteClickListener noteClickListener) {
        this.notes = notes;
        this.context = context;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_course, parent, false);
        NotesViewHolder holder = new NotesViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {

        holder.content.setText(notes.get(position).getContent());
        holder.deleteButton.setOnClickListener(v -> {
            noteClickListener.onDelete(notes.get(position));

        });
        holder.title.setText(notes.get(position).getTitle());


        holder.updateButton.setOnClickListener(v -> {
            noteClickListener.onUpdate(notes.get(position).getId());
        });

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder {

        TextView content, title;
        ImageView updateButton;
        ImageView deleteButton;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTV);
            content = itemView.findViewById(R.id.contentTV);
            deleteButton = itemView.findViewById(R.id.deleteIV);
            updateButton = itemView.findViewById(R.id.editIV);

        }
    }
}
