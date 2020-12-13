package com.drozdztomasz.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Measurement::class], version = 1, exportSchema = false)
abstract class BMIDatabase : RoomDatabase() {

    abstract val bmiDatabaseDao: BMIDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: BMIDatabase? = null

        const val MAX_ROWS: Int = 10

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
                        .addCallback(deleteUntilMaxCallback)
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }

        private val deleteUntilMaxCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.execSQL(
                    """
                        CREATE TRIGGER delete_until_max
                        AFTER INSERT ON bmi_measurement_table
                        BEGIN
                            DELETE FROM bmi_measurement_table
                            WHERE measurementId NOT IN 
                            (
                                SELECT measurementId FROM bmi_measurement_table
                                ORDER BY measurement_date DESC
                                LIMIT $MAX_ROWS
                            );
                        END
                        """
                )
            }
        }
    }
}
