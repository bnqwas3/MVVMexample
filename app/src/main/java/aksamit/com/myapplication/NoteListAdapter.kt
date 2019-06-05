package aksamit.com.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_item_note.view.*

class NoteListAdapter : ListAdapter<Note, NoteListAdapter.NoteListViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Note> = object : DiffUtil.ItemCallback<Note>() {
            override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
                return oldItem.title == newItem.title
                        && oldItem.description == newItem.description
                        && oldItem.priority == newItem.priority
            }

        }
    }

    private lateinit var listener: OnItemClickListener

    inner class NoteListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteListViewHolder {
        val itemView = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.recycler_item_note, parent, false)
        return NoteListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteListViewHolder, position: Int) {

        val note = getItem(position)

        holder.itemView.apply {

            tvTitle.text = note.title
            tvDescription.text = note.description
            tvPriority.text = note.priority.toString()

        }

        holder.itemView.apply {
            findViewById<TextView>(R.id.tvTitle).text = getItem(position).title
        }

    }

    public fun getNoteAt(position: Int): Note {
        return getItem(position)
    }

    public interface OnItemClickListener {
        fun onItemClick(note: Note)
    }

    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
}