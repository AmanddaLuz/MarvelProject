package com.amandaluz.marvelproject.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.amandaluz.marvelproject.data.db.converters.Converters
import com.amandaluz.marvelproject.data.model.Favorites
import com.amandaluz.marvelproject.data.model.Results
import com.amandaluz.marvelproject.data.model.User

@Database(entities = [Favorites::class, User::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDAO

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("CREATE TABLE IF NOT EXISTS user_table(" +
//                        "`email` TEXT NOT NULL," +
//                        "`name` TEXT NOT NULL," +
//                        "`password` TEXT NOT NULL," +
//                        "`photo` INT," +
//                        "PRIMARY KEY (`email`))")
                //include sql command to update database
            }
        }

        fun getDb(context: Context): AppDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appdatabase.db")
                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance
                return instance
            }
        }
    }
}