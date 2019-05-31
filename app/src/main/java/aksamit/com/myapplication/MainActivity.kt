package aksamit.com.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Timber.d(WithTag.withTag("onCreate call"))

        Timber.d(WithTag.withTag("assign noteViewModel"))

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel::class.java)

        Timber.d(WithTag.withTag("getAllNotes()"))

        noteViewModel.getAllNotes().observe(this, Observer<List<Note>> {
            Timber.d(WithTag.withTag("make toast"))
            Toast.makeText(applicationContext, "onChanged", Toast.LENGTH_LONG).show()
        })
    }
}
