package com.timilehinjegede.notes.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Note.class},version = 1,exportSchema = false)
public abstract class NoteDatabase extends RoomDatabase {

   private static NoteDatabase roomDatabase ;

   public abstract NoteDao noteDao();

   public static synchronized NoteDatabase getInstance(Context context){

       if (roomDatabase == null){
           roomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                   NoteDatabase.class,
                   "note_database")
                   .fallbackToDestructiveMigration()
                   .build();
       }
       return roomDatabase;
   }

}
