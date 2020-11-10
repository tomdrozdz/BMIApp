// Tomasz Drozdz, 246718
// Testowane na emulatorze + Samsung Galaxy Note 9

package com.example.bmiapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.bmiapp.databinding.ActivityMainBinding
import com.example.bmicalculating.*
import com.example.history.Measurement
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var bmiCalculator: BMICalculator = NormalCalculator
    private var defaultTvColor: Int = 0
    private var lastBmi: Double = 0.0
    private lateinit var measurements: LinkedList<Measurement>
    private val gson: Gson = Gson()

    companion object {
        private const val BMI_FORMAT: String = "%.2f"

        private const val BMI_VALUE: String = "BMI_VALUE"
        private const val BMI_COLOR: String = "BMI_COLOR"
        private const val BMI_CLICK_VISIBILITY: String = "BMI_CLICK_VISIBILITY"
        private const val LAST_BMI: String = "LAST_BMI"

        const val HISTORY_FILE: String = "com.example.bmiapp.HISTORY_FILE"
        const val HISTORY_LIST: String = "HISTORY_LIST"
        val HISTORY_TYPE: Type = object: TypeToken<LinkedList<Measurement>>() {}.type
        private const val MAX_HISTORY_SIZE: Int = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        defaultTvColor = binding.bmiTV.currentTextColor
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_main, menu)
        return true
    }

    override fun onResume() {
        super.onResume()
        measurements = getMeasurements()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.switch_units -> {
                if (bmiCalculator is NormalCalculator.Companion) {
                    changeToAmerican()
                }
                else {
                    changeToNormal()
                }
                true
            }
            R.id.show_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.apply {
            outState.putString(BMI_VALUE, bmiTV.text.toString())
            outState.putInt(BMI_COLOR, bmiTV.textColors.defaultColor)
            outState.putInt(BMI_CLICK_VISIBILITY, bmiClickTV.visibility)
        }
        outState.putDouble(LAST_BMI, lastBmi)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.apply {
            bmiTV.text = savedInstanceState.getString(BMI_VALUE)
            bmiTV.setTextColor(savedInstanceState.getInt(BMI_COLOR))
            bmiClickTV.visibility = savedInstanceState.getInt(BMI_CLICK_VISIBILITY)
        }
        lastBmi = savedInstanceState.getDouble(LAST_BMI )
    }

    private fun resetState() {
        lastBmi = 0.0
        changeColor()
        binding.apply {
            bmiTV.text = getString(R.string.empty_value)
            heightET.text.clear()
            massET.text.clear()
            bmiClickTV.visibility = View.INVISIBLE
        }
    }

    private fun changeToAmerican() {
        bmiCalculator = AmericanCalculator
        resetState()
        binding.apply {
            heightTV.text = getString(R.string.height_in)
            massTV.text = getString(R.string.mass_lbs)
        }
    }

    private fun changeToNormal() {
        bmiCalculator = NormalCalculator
        resetState()
        binding.apply {
            heightTV.text = getString(R.string.height_cm)
            massTV.text = getString(R.string.mass_kg)
        }
    }

    private fun checkIfEvsAreEmpty(): Boolean {
        var empty: Boolean = false
        binding.apply {
            if (massET.text.isBlank()) {
                massET.error = getString(R.string.mass_is_empty)
                empty = true
            }
            if (heightET.text.isBlank()) {
                heightET.error = getString(R.string.height_is_empty)
                empty = true
            }
        }
        return empty
    }

    private fun changeColor(bmi: Double = 0.0) {
        binding.apply {
            val color = when {
                bmi == 0.0 -> defaultTvColor
                bmi < UNDERWEIGHT_THRESHOLD -> getColor(R.color.underweight)
                bmi < OVERWEIGHT_THRESHOLD -> getColor(R.color.normal_weight)
                bmi < OBESE_THRESHOLD -> getColor(R.color.overweight)
                else -> getColor(R.color.obese)
            }
            bmiTV.setTextColor(color)
        }
    }

    private fun checkIfValid(mass: Double, height: Double): Boolean {
        val massStatus: BMICalculator.STATUS = bmiCalculator.checkMass(mass)
        val heightStatus: BMICalculator.STATUS = bmiCalculator.checkHeight(height)
        var valid = true

        binding.apply {
            if (massStatus != BMICalculator.STATUS.VALID) {
                when (massStatus) {
                    BMICalculator.STATUS.TOO_SMALL -> massET.error = getString(R.string.mass_small)
                    BMICalculator.STATUS.TOO_BIG -> massET.error = getString(R.string.mass_big)
                }
                valid = false
            }

            if (heightStatus != BMICalculator.STATUS.VALID) {
                when (heightStatus) {
                    BMICalculator.STATUS.TOO_SMALL -> heightET.error = getString(R.string.height_small)
                    BMICalculator.STATUS.TOO_BIG -> heightET.error = getString(R.string.height_big)
                }
                valid = false
            }
        }
        return valid
    }

    private fun getMeasurements(): LinkedList<Measurement> {
        val sharedPref = getSharedPreferences(HISTORY_FILE, MODE_PRIVATE)
        val savedString = sharedPref.getString(HISTORY_LIST, null) ?: return LinkedList()

        return gson.fromJson(savedString, HISTORY_TYPE)
    }

    private fun saveMeasurement(bmi: Double, mass: Double, height: Double, bmiCalculator: BMICalculator) {
        val units = when (bmiCalculator) {
            NormalCalculator -> Measurement.Companion.UNITS.NORMAL
            AmericanCalculator -> Measurement.Companion.UNITS.AMERICAN
            else -> Measurement.Companion.UNITS.NOT_DEFINED
        }

        measurements.push(Measurement(bmi, mass, height, Calendar.getInstance().time, units))
        while (measurements.size > MAX_HISTORY_SIZE)
            measurements.removeLast()

        val sharedPref = getSharedPreferences(HISTORY_FILE, MODE_PRIVATE)
        val stringToSave = gson.toJson(measurements)

        with (sharedPref.edit()) {
            this.putString(HISTORY_LIST, stringToSave)
            apply()
        }
    }

    fun count(view: View) {
        binding.apply {
            if(checkIfEvsAreEmpty())
                return

            val mass: Double = massET.text.toString().toDouble()
            val height: Double = heightET.text.toString().toDouble()

            if (checkIfValid(mass, height)) {
                lastBmi = bmiCalculator.count(mass, height)
                saveMeasurement(lastBmi, mass, height, bmiCalculator)
                changeColor(lastBmi)
                bmiTV.text = BMI_FORMAT.format(lastBmi)
                bmiClickTV.visibility = View.VISIBLE
            }
        }
    }

    fun switchToDescription(view: View) {
        if (lastBmi == 0.0) {
            Toast.makeText(applicationContext, getString(R.string.no_bmi), Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, DescActivity::class.java)
        intent.putExtra(DescActivity.BMI_VALUE, lastBmi)
        startActivityForResult(intent, 0)
    }
}
