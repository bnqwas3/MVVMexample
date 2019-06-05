package aksamit.com.myapplication

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_add_note.view.*
import timber.log.Timber

class AddEditNoteFragment : Fragment(){

    private lateinit var noteViewModel: NoteViewModel
    private var replace = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_note, container, false)

        view.npPriority.minValue = 1
        view.npPriority.maxValue = 10

        if(arguments != null){
            view.etTitle.setText(arguments?.getString(Constants.EXTRA_TITLE))
            view.etDescription.setText(arguments?.getString(Constants.EXTRA_DESCRIPTION))
            view.npPriority.value = arguments?.getInt(Constants.EXTRA_PRIORITY) ?: 1
            replace = true
            view.btnAddNote.text = "edit"
            activity?.title = "Edit note"
        } else {
            replace = false
            Timber.d(WithTag.withTag("arguments null"))
            view.btnAddNote.text = "add"
            activity?.title = "Add note"
        }

        noteViewModel = ViewModelProviders.of(requireActivity()).get(NoteViewModel::class.java)

        view.btnAddNote.setOnClickListener {
            Timber.d(WithTag.withTag("add note click"))
            if (view.etTitle.text.isEmpty() || view.etDescription.text.isEmpty()){
                Toast.makeText(context, "please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {
                val note = Note(view.etTitle.text.toString(), view.etDescription.text.toString(), view.npPriority.value)
                if(replace) {
                    note.id = arguments!!.getInt(Constants.EXTRA_ID)
                    noteViewModel.update(note)
                } else {
                    noteViewModel.insert(note)
                }
                val fragment = NotesListFragment()
                fragmentManager?.beginTransaction()?.replace(R.id.flMainFrame, fragment)?.commit()
            }
        }
        return view
    }
}