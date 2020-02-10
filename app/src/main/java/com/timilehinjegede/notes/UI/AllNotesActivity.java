package com.timilehinjegede.notes.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.timilehinjegede.notes.R;
import com.timilehinjegede.notes.db.Note;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AllNotesActivity extends AppCompatActivity {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    NoteViewModel noteViewModel;
    CoordinatorLayout layout ;
    Date date = Calendar.getInstance().getTime();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
    String myDate = simpleDateFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton floatingActionButton = findViewById(R.id.addNoteFab);
        layout = findViewById(R.id.rootLayout);


        getSupportActionBar().setTitle("All Notes");

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AllNotesActivity.this,AddNoteActivity.class);
                startActivityForResult(intent,ADD_NOTE_REQUEST);
             }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(AllNotesActivity.this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(AllNotesActivity.this).get(NoteViewModel.class);

        noteViewModel.getAllNotes().observe(AllNotesActivity.this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                noteAdapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNote(viewHolder.getAdapterPosition()));
//
//                Snackbar.make(layout,"Note Deleted",Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Note note = noteAdapter.getNote(viewHolder.getAdapterPosition());
//                        String title = note.getTitle();
//                        String body = note.getBody();
//
//                        noteViewModel.insert(new Note(title,body));
//                        noteViewModel.insert(noteAdapter.getNote(viewHolder.getAdapterPosition()));
//                    }
//                }).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnNoteClickListener(new NoteAdapter.onNoteClickListener() {
            @Override
            public void onNoteClick(Note note) {
                Intent intent = new Intent(AllNotesActivity.this,AddNoteActivity.class);
                intent.putExtra(AddNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddNoteActivity.EXTRA_BODY,note.getBody());
                startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String body = data.getStringExtra(AddNoteActivity.EXTRA_BODY);

            Note note = new Note(title,body,myDate);
            noteViewModel.insert(note);

            Toast.makeText(this,"Note Saved",Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){
            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID,-1);

            if (id == -1){
                Toast.makeText(this,"Note Not Updated",Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String body = data.getStringExtra(AddNoteActivity.EXTRA_BODY);

            Note note = new Note(title,body,myDate);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.allnote_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.deleteall:
                noteViewModel.deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
