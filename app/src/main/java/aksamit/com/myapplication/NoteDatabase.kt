package aksamit.com.myapplication

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import timber.log.Timber

@Database(entities = [Note::class], version = 1)
abstract class NoteDatabase : RoomDatabase(){
    companion object {
        private var instance: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase? {
            Timber.d(WithTag.withTag("getInstance call"))
            if (instance == null) {
                synchronized(NoteDatabase::class) {
                    Timber.d(WithTag.withTag("in synchronized block, create db"))
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NoteDatabase::class.java, "note_database"
                    )
                        .fallbackToDestructiveMigration() // when version increments, it migrates (deletes db and creates new) - else it crashes
                        .addCallback(roomCallback)
                        .build()
                }
            }
            return instance
        }

        private val roomCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                Timber.d(WithTag.withTag("roomDatabase.Callback onCreate() call"))
                super.onCreate(db)
                PopulateDbAsyncTask(instance)
                    .execute()
            }
        }
    }

    public abstract fun noteDao(): NoteDao

    class PopulateDbAsyncTask(db: NoteDatabase?) : AsyncTask<Unit, Unit, Unit>() {
        private val noteDao = db?.noteDao()

        override fun doInBackground(vararg p0: Unit?) {
            Timber.d(WithTag.withTag("doInBackground call"))
            noteDao?.insert(Note("title 1", "description 1", 1))
            noteDao?.insert(Note("title 2", "description 2", 2))
            noteDao?.insert(Note("title 3", "description 3", 3))
        }
    }

}