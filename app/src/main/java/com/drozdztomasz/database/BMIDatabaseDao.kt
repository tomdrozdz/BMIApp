package com.drozdztomasz.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface BMIDatabaseDao {

    @Insert
    suspend fun insert(measurement: Measurement)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param measurement new value to write
     */
    @Update
    suspend fun update(measurement: Measurement)

    /**
     * Selects and returns the row that matches the supplied start time, which is our key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from bmi_measurement_table WHERE measurementId = :key")
    suspend fun get(key: Long): Measurement

    @Query("DELETE FROM bmi_measurement_table")
    suspend fun clear()

    @Query("SELECT * FROM bmi_measurement_table ORDER BY measurement_date DESC")
    fun getAllMeasurements(): LiveData<List<Measurement>>
}
