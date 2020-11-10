package com.example.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bmiapp.R
import com.example.bmiapp.databinding.ItemMeasurementBinding
import java.text.SimpleDateFormat

class MeasurementAdapter(private val measurements: List<Measurement>) : RecyclerView.Adapter<MeasurementAdapter.ViewHolder>() {

	private val dateFormatter: SimpleDateFormat = SimpleDateFormat(DATE_FORMAT)

	companion object {
		private const val BMI_FORMAT = "%.2f"
		private const val UNITS_FORMAT = "%.1f "
		private const val DATE_FORMAT = "dd.MM.yyyy"
	}

	inner class ViewHolder(private val binding: ItemMeasurementBinding) : RecyclerView.ViewHolder(binding.root) {
		fun bind(measurement: Measurement) {
			binding.apply {
				historyBmi.text = BMI_FORMAT.format(measurement.bmi)
				historyDate.text = dateFormatter.format(measurement.date)
				var weight = UNITS_FORMAT.format(measurement.weight)
				var height = UNITS_FORMAT.format(measurement.height)

				when (measurement.units) {
					Measurement.Companion.UNITS.NORMAL -> {
						weight += root.context.getString(R.string.kg_short)
						height += root.context.getString(R.string.cm_short)
					}
					Measurement.Companion.UNITS.AMERICAN -> {
						weight += root.context.getString(R.string.lbs_short)
						height += root.context.getString(R.string.in_short)
					}
				}
				historyWeight.text = weight
				historyHeight.text = height
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementAdapter.ViewHolder {
		val inflater = LayoutInflater.from(parent.context)
		val binding = ItemMeasurementBinding.inflate(inflater)
		return ViewHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(measurements[position])
	}

	override fun getItemCount() = measurements.size
}
