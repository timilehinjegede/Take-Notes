package com.timilehinjegede.notes.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.timilehinjegede.notes.R;
import com.timilehinjegede.notes.db.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder>   {

    public List<Note> notes = new ArrayList<>();
    private onNoteClickListener onNoteClickListener ;

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card_list,
                        parent,
                        false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        Note currentNote = notes.get(position);
        holder.textViewTitle.setText(currentNote.getTitle());
        holder.textViewBody.setText(currentNote.getBody());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNote(int position){
            return notes.get(position);
    }

    public void setOnNoteClickListener(onNoteClickListener listener){
        onNoteClickListener = listener ;
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView textViewTitle, textViewBody ;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.noteTitle_card_list);
            textViewBody = itemView.findViewById(R.id.noteBody_card_list);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    onNoteClickListener.onNoteClick(notes.get(position));
                }
            });
        }
    }

    public interface onNoteClickListener{
        void onNoteClick(Note note);
    }
}
