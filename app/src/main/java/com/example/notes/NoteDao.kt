package com.example.notes

import androidx.room.*

@Dao
interface NoteDao {
    @Insert
    fun addNote(note: Note)

    @Query("SELECT * FROM note")
    fun getNote() : Note

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)
}