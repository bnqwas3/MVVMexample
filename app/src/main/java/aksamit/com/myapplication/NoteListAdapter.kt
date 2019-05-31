package aksamit.com.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NoteListAdapter(private val notes: List<Note>) : RecyclerView.Adapter<NoteListAdapter.NoteListViewHolder>(){


    class NoteListViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.recycler_item_note, parent, false)
        return NoteListViewHolder(cellForRow)
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.tvTitle).text = notes[position].title
        }
    }
}