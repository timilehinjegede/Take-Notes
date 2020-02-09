package com.timilehinjegede.notes.UI;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.timilehinjegede.notes.R;

public class AddNoteActivity extends AppCompatActivity {

    public static final String EXTRA_TITLE = "com.timilehinjegede.notes.UI.EXTRA_TITLE";
    public static final String EXTRA_BODY = "com.timilehinjegede.notes.UI.EXTRA_BODY";
    public static final String EXTRA_ID = "com.timilehinjegede.notes.UI.EXTRA_ID";

    EditText noteTitle,noteBody;
    Intent intent,intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

         noteTitle = findViewById(R.id.editText_noteTitle);
         noteBody = findViewById(R.id.editText_noteBody);



          intent = getIntent();
            intent2 = getIntent();

         if (intent.hasExtra(EXTRA_ID)){
             getSupportActionBar().setTitle("Edit Note");
             noteTitle.setText(intent.getStringExtra(EXTRA_TITLE));
             noteBody.setText(intent.getStringExtra(EXTRA_BODY));
         }else {
             getSupportActionBar().setTitle("Add Note");
         }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.addnote_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:
                saveNote();
                break;
            case R.id.share:
                shareNote();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareNote() {

        Intent intent = new Intent();
                String title = noteTitle.getText().toString();
                String body = noteBody.getText().toString();

                intent.putExtra(Intent.EXTRA_TEXT,title + "\n" + body);
                intent.setType("text/plain");
                intent.setAction(Intent.ACTION_SEND);

                startActivity(Intent.createChooser(intent,"Share to :"));
    }

    private void saveNote() {
        String title = noteTitle.getText().toString();
        String body = noteBody.getText().toString();

        if (title.trim().isEmpty() || body.trim().isEmpty()){
            Toast.makeText(this,"Cannot add empty note",Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE,title);
        intent.putExtra(EXTRA_BODY,body);

        int id = getIntent().getIntExtra(EXTRA_ID,-1);
        if (id != -1){
            intent.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK,intent);
        finish();
    }
}
