package aksamit.com.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class NoteViewModel(application: Application): AndroidViewModel(application){

    private val repository: NoteRepository = NoteRepository(application)

    public fun insert(note: Note){
        repository.insert(note)
    }

    public fun delete(note: Note){
        repository.delete(note)
    }

    public fun update(note: Note){
        repository.update(note)
    }

    public fun deleteAllNotes(){
        repository.deleteAllNotes()
    }

    public fun getAllNotes(): LiveData<List<Note>> {
        return repository.getAllNotes()
    }


}