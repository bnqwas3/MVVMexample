package aksamit.com.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    companion object {
        final val ADD_NOTE_REQUEST = 1
        final val EDIT_NOTE_REQUEST = 2
    }

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddNote.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }

        rvNoteList.layoutManager = LinearLayoutManager(this)
        val noteListAdapter = NoteListAdapter()
        rvNoteList.adapter = noteListAdapter
        rvNoteList.setHasFixedSize(true)

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        Timber.d(WithTag.withTag("getAllNotes()"))

        noteViewModel.getAllNotes().observe(this, Observer<List<Note>> { notes ->
            Timber.d(WithTag.withTag("notes changed"))

            noteListAdapter.submitList(notes)
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Timber.d(WithTag.withTag("direction: $direction"))
                noteViewModel.delete(noteListAdapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(applicationContext, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(rvNoteList)

        noteListAdapter.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                val intent = Intent(applicationContext, AddEditNoteActivity::class.java)
                Timber.d(WithTag.withTag("click at note with noteID: ${note.id}, title: ${note.title}"))
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.id)
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.title)
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION, note.description)
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.priority)

                startActivityForResult(intent, EDIT_NOTE_REQUEST)
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Timber.d(WithTag.withTag("onActivityResult"))
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {

            if (data == null) {
                return
            }

            val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title, description, priority)

            noteViewModel.insert(note)

            Toast.makeText(applicationContext, "note saved", Toast.LENGTH_SHORT).show()


        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {

            val id = data?.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1)

            if (id == -1 || data == null) {
                Toast.makeText(applicationContext, "Note can't be updated", Toast.LENGTH_SHORT).show()
                return
            }

            val title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE)
            val description = data.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION)
            val priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 1)

            val note = Note(title, description, priority)
            if (id != null) {
                note.id = id
            }
            noteViewModel.update(note)

            Toast.makeText(applicationContext, "note updated", Toast.LENGTH_SHORT).show()


        } else {

            Toast.makeText(applicationContext, "Note not saved", Toast.LENGTH_SHORT).show()

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.deleteAllNotes -> {
                noteViewModel.deleteAllNotes()
                Toast.makeText(applicationContext, "All notes deleted", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
