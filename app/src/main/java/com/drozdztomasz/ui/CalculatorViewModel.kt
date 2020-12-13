package com.drozdztomasz.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drozdztomasz.calculators.AmericanCalculator
import com.drozdztomasz.calculators.BMICalculator
import com.drozdztomasz.calculators.NormalCalculator
import com.drozdztomasz.database.BMIDatabaseDao
import com.drozdztomasz.database.Measurement
import kotlinx.coroutines.launch
import java.util.*

class CalculatorViewModel(dataSource: BMIDatabaseDao, val defaultTvColor: Int) : ViewModel() {
    val database = dataSource

    private val _calculator = MutableLiveData<BMICalculator>(NormalCalculator)
    val calculator: LiveData<BMICalculator>
        get() = _calculator

    private val _bmi = MutableLiveData<Double>(0.0)
    val bmi: LiveData<Double>
        get() = _bmi

    fun calculate(mass: Double, height: Double) {
        val calculator = requireNotNull(calculator.value)
        val bmi = calculator.count(mass, height)
        _bmi.value = bmi
        viewModelScope.launch { saveMeasurement(bmi, mass, height, calculator) }
    }

    fun switchCalculators() {
        _calculator.value = if (calculator.value is NormalCalculator.Companion) {
            AmericanCalculator
        } else {
            NormalCalculator
        }
        _bmi.value = 0.0
    }

    private suspend fun saveMeasurement(
        bmi: Double,
        mass: Double,
        height: Double,
        calculator: BMICalculator
    ) {
        val units = when (calculator) {
            NormalCalculator -> Measurement.Companion.UNITS.NORMAL
            AmericanCalculator -> Measurement.Companion.UNITS.AMERICAN
            else -> Measurement.Companion.UNITS.NOT_DEFINED
        }

        database.insert(
            Measurement(
                bmi,
                mass,
                height,
                Calendar.getInstance().time,
                units
            )
        )
    }
}
