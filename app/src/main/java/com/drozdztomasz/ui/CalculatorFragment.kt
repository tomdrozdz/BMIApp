// Tomasz Drozdz, 246718
// Testowane na emulatorze + Samsung Galaxy Note 9

package com.drozdztomasz.ui

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.drozdztomasz.R
import com.drozdztomasz.calculators.*
import com.drozdztomasz.database.BMIDatabase
import com.drozdztomasz.databinding.CalculatorFragmentBinding

class CalculatorFragment : Fragment() {
    private lateinit var binding: CalculatorFragmentBinding
    private lateinit var viewModel: CalculatorViewModel

    companion object {
        private const val BMI_FORMAT: String = "%.2f"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CalculatorFragmentBinding.inflate(layoutInflater)

        val application = requireNotNull(this.activity).application
        val dataSource = BMIDatabase.getInstance(application).bmiDatabaseDao
        val viewModelFactory =
            CalculatorViewModelFactory(dataSource, binding.bmiTV.currentTextColor)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CalculatorViewModel::class.java)

        viewModel.bmi.observe(viewLifecycleOwner) { bmiChanged(it) }
        viewModel.calculator.observe(viewLifecycleOwner) { calculatorChanged(it) }

        binding.countBTN.setOnClickListener(this::count)
        binding.bmiTV.setOnClickListener(this::switchToDescription)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.switch_units -> {
                viewModel.switchCalculators()
                true
            }
            R.id.show_history -> {
                val action =
                    CalculatorFragmentDirections.actionCalculatorFragmentToHistoryFragment()
                requireView().findNavController().navigate(action)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun bmiChanged(bmi: Double) {
        binding.bmiTV.text = BMI_FORMAT.format(bmi)

        val visibility = when (bmi) {
            0.0 -> View.INVISIBLE
            else -> View.VISIBLE
        }
        binding.bmiClickTV.visibility = visibility

        val color = when {
            bmi == 0.0 -> viewModel.defaultTvColor
            bmi < UNDERWEIGHT_THRESHOLD -> getColor(requireContext(), R.color.underweight)
            bmi < OVERWEIGHT_THRESHOLD -> getColor(requireContext(), R.color.normal_weight)
            bmi < OBESE_THRESHOLD -> getColor(requireContext(), R.color.overweight)
            else -> getColor(requireContext(), R.color.obese)
        }
        binding.bmiTV.setTextColor(color)
    }

    private fun calculatorChanged(calculator: BMICalculator) {
        var heightText = 0
        var massText = 0

        when (calculator) {
            NormalCalculator -> {
                heightText = R.string.height_cm
                massText = R.string.mass_kg
            }
            AmericanCalculator -> {
                heightText = R.string.height_in
                massText = R.string.mass_lbs
            }
        }

        binding.apply {
            heightET.text.clear()
            massET.text.clear()
            bmiClickTV.visibility = View.INVISIBLE
            heightTV.text = getString(heightText)
            massTV.text = getString(massText)
        }
    }

    private fun checkIfEvsAreEmpty(): Boolean {
        var empty = false
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

    private fun checkIfValid(mass: Double, height: Double): Boolean {
        val calculator = requireNotNull(viewModel.calculator.value)
        val massStatus: BMICalculator.STATUS = calculator.checkMass(mass)
        val heightStatus: BMICalculator.STATUS = calculator.checkHeight(height)
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
                    BMICalculator.STATUS.TOO_SMALL -> heightET.error =
                        getString(R.string.height_small)
                    BMICalculator.STATUS.TOO_BIG -> heightET.error = getString(R.string.height_big)
                }
                valid = false
            }
        }
        return valid
    }

    private fun count(view: View) {
        if (checkIfEvsAreEmpty())
            return

        val mass: Double = binding.massET.text.toString().toDouble()
        val height: Double = binding.heightET.text.toString().toDouble()

        if (checkIfValid(mass, height)) {
            viewModel.calculate(mass, height)
        }
    }

    private fun switchToDescription(view: View) {
        if (viewModel.bmi.value == 0.0) {
            Toast.makeText(requireContext(), getString(R.string.no_bmi), Toast.LENGTH_SHORT)
                .show()
            return
        }

        val action =
            CalculatorFragmentDirections.actionCalculatorFragmentToDescriptionFragment(viewModel.bmi.value!!.toFloat())
        view.findNavController().navigate(action)
    }
}
