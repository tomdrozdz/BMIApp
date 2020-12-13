package com.drozdztomasz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Measurement::class], version = 1, exportSchema = false)
abstract class BMIDatabase : RoomDatabase() {

    abstract val bmiDatabaseDao: BMIDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: BMIDatabase? = null

        /**
         * @param context The application context Singleton, used to get access to the filesystem.
         */
        fun getInstance(context: Context): BMIDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BMIDatabase::class.java,
                        "bmi_measurement_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}
