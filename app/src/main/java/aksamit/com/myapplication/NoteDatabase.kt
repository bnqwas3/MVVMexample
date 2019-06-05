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
                        .build()
                }
            }
            return instance
        }


    }

    public abstract fun noteDao(): NoteDao

}