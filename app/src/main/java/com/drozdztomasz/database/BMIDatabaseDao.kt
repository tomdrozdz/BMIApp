package com.drozdztomasz.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BMIDatabaseDao {
    @Insert
    suspend fun insert(measurement: Measurement)

    @Query("SELECT * FROM bmi_measurement_table ORDER BY measurement_date DESC")
    fun getAllMeasurements(): LiveData<List<Measurement>>

    @Update
    suspend fun update(measurement: Measurement)

    @Query("SELECT * from bmi_measurement_table WHERE measurementId = :key")
    suspend fun get(key: Long): Measurement

    @Query("DELETE FROM bmi_measurement_table")
    suspend fun clear()
}
