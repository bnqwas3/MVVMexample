package aksamit.com.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_note.*
import timber.log.Timber

class AddEditNoteActivity : AppCompatActivity() {

    companion object {
        final val EXTRA_ID: String = "aksamit.com.myapplication.EXTRA_ID"
        final val EXTRA_TITLE: String = "aksamit.com.myapplication.EXTRA_TITLE"
        final val EXTRA_DESCRIPTION: String = "aksamit.com.myapplication.EXTRA_DESCRIPTION"
        final val EXTRA_PRIORITY: String = "aksamit.com.myapplication.EXTRA_PRIORITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        npPriority.minValue = 1
        npPriority.maxValue = 10

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_close)

        if(intent.hasExtra(EXTRA_ID)){
            title = "Edit note"
            etTitle.setText(intent.getStringExtra(EXTRA_TITLE))
            etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
            npPriority.value = intent.getIntExtra(EXTRA_PRIORITY,1)
        } else {
            title = "Add note"
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_note_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.save_note -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote(){
        val title = etTitle.text.toString()
        val description = etDescription.text.toString()
        val priority = npPriority.value

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "please insert a title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, description)
        data.putExtra(EXTRA_PRIORITY, priority)

        val id = intent.getIntExtra(EXTRA_ID, -1)

        Timber.d(WithTag.withTag("get id: $id"))

        if(id != -1){
            data.putExtra(EXTRA_ID, id)
        }

        setResult(RESULT_OK, data)
        finish()
    }
}
