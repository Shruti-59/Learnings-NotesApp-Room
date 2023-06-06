package com.example.demoviewmodel.data

import android.content.Context

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notesappwithroom.model.Note
import com.example.notesappwithroom.util.Converters

@Database(entities = [Note::class], version = 4)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
      /*  private var instance: NoteDatabase? = null
        @Synchronized
        fun getInstance(context: Context): NoteDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java, "note_database"
                )
                    .fallbackToDestructiveMigration()

                    .build()
            }
            return instance!!
        }*/

        val migration_3_4 = object : Migration(3,4){
            override fun migrate(database: SupportSQLiteDatabase) {
               database.execSQL("ALTER TABLE note_table ADD COLUMN isDone BOOLEAN NOT NULL DEFAULT(1)")
            }
        }

        private lateinit var INSTANCE: NoteDatabase

        fun getInstance(context: Context): NoteDatabase {
            synchronized(NoteDatabase::class.java) {
                if (!Companion::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        NoteDatabase::class.java,
                        "note_database")
                        //.fallbackToDestructiveMigration()
                        //.allowMainThreadQueries()
                        .addMigrations(migration_3_4)
                        .build()
                }
            }
            return INSTANCE
        }
    }
}