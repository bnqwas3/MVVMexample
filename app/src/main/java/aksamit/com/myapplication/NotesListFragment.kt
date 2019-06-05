package aksamit.com.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_notes_list.view.*
import timber.log.Timber

class NotesListFragment : Fragment(){

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_notes_list, container, false)

        view.fabAddNote.setOnClickListener {
            val fragment = AddEditNoteFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.flMainFrame, fragment)
                ?.addToBackStack(null)
                ?.commit()

            Timber.d(WithTag.withTag("fabAddNote pressed"))
        }

        activity?.title = "All notes"

        view.rvNoteList.layoutManager = LinearLayoutManager(context)
        val noteListAdapter = NoteListAdapter()
        view.rvNoteList.adapter = noteListAdapter
        view.rvNoteList.setHasFixedSize(true)

        noteViewModel = ViewModelProviders.of(requireActivity()).get(NoteViewModel::class.java)

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
                noteViewModel.delete(noteListAdapter.getNoteAt(viewHolder.adapterPosition))
                Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
            }
        }).attachToRecyclerView(view.rvNoteList)

        noteListAdapter.setOnItemClickListener(object : NoteListAdapter.OnItemClickListener {
            override fun onItemClick(note: Note) {
                Timber.d(WithTag.withTag("click at note with noteID: ${note.id}, title: ${note.title}"))

                val args = Bundle()

                args.putInt(Constants.EXTRA_ID, note.id)
                args.putString(Constants.EXTRA_TITLE, note.title)
                args.putString(Constants.EXTRA_DESCRIPTION, note.description)
                args.putInt(Constants.EXTRA_PRIORITY, note.priority)

                val fragment = AddEditNoteFragment()
                fragment.arguments = args

                fragmentManager?.beginTransaction()
                    ?.replace(R.id.flMainFrame, fragment)
                    ?.addToBackStack(null)
                    ?.commit()
            }

        })


        return view
    }
}