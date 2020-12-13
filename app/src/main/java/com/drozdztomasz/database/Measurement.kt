package com.drozdztomasz.database

import androidx.room.*
import java.util.*

@Entity(tableName = "bmi_measurement_table")
@TypeConverters(Converters::class)
data class Measurement(
    @ColumnInfo(name = "bmi_value")
    val bmiValue: Double,

    @ColumnInfo(name = "weight_value")
    var weightValue: Double,

    @ColumnInfo(name = "height_value")
    var heightValue: Double,

    @ColumnInfo(name = "measurement_date")
    var measurementDate: Date,

    @ColumnInfo(name = "measurement_units")
    var measurementUnits: UNITS = UNITS.NOT_DEFINED
) {
    @PrimaryKey(autoGenerate = true)
    var measurementId: Long = 0L

    companion object {
        enum class UNITS(val id: Int) {
            NORMAL(0), AMERICAN(1), NOT_DEFINED(-1);

            companion object {
                private val map = values().associateBy(UNITS::id)
                fun fromInt(type: Int) = map[type] ?: throw IllegalArgumentException()
            }
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromIntToUnit(value: Int): Measurement.Companion.UNITS {
        return Measurement.Companion.UNITS.fromInt(value)
    }

    @TypeConverter
    fun fromUnitToInt(unit: Measurement.Companion.UNITS): Int {
        return unit.id
    }
}
