package aksamit.com.myapplication

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData

class NoteRepository(application: Application) {


    companion object {
        private class InsertNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {

            override fun doInBackground(vararg p0: Note) {
                noteDao.insert(p0[0])
            }
        }

        private class UpdateNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {

            override fun doInBackground(vararg p0: Note) {
                noteDao.update(p0[0])
            }
        }

        private class DeleteNoteAsyncTask(val noteDao: NoteDao) : AsyncTask<Note, Unit, Unit>() {

            override fun doInBackground(vararg p0: Note) {
                noteDao.delete(p0[0])
            }
        }

        private class DeleteAllNotesAsyncTask(val noteDao: NoteDao) : AsyncTask<Unit, Unit, Unit>() {

            override fun doInBackground(vararg p0: Unit?) {
                noteDao.deleteAllNotes()
            }
        }
    }

    private val noteDao: NoteDao

    private val allNotes: LiveData<List<Note>>

    init {
        val database = NoteDatabase.getInstance(application)!!

        noteDao = database.noteDao()
        allNotes = noteDao.getAllNotes()
    }

    fun insert(note: Note){
        InsertNoteAsyncTask(noteDao).execute(note)
    }

    fun update(note: Note){
        UpdateNoteAsyncTask(noteDao).execute(note)
    }

    fun delete(note: Note){
        DeleteNoteAsyncTask(noteDao).execute(note)
    }

    fun deleteAllNotes(){
        DeleteAllNotesAsyncTask(noteDao).execute()
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }
}