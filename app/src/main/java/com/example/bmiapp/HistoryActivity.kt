package com.example.bmiapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bmiapp.databinding.ActivityHistoryBinding
import com.example.history.MeasurementAdapter
import com.example.history.Measurement
import com.google.gson.Gson

class HistoryActivity : AppCompatActivity() {
	lateinit var binding: ActivityHistoryBinding

	private lateinit var viewAdapter: RecyclerView.Adapter<*>
	private lateinit var viewManager: RecyclerView.LayoutManager

	private val gson: Gson = Gson()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityHistoryBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val dataset = getDataset()

		viewManager = LinearLayoutManager(this)
		viewAdapter = MeasurementAdapter(dataset)

		binding.apply {
			recyclerView.layoutManager = viewManager
			recyclerView.adapter = viewAdapter

			if (dataset.isEmpty()) {
				binding.apply {
					recyclerView.visibility = View.GONE
					emptyTV.visibility = View.VISIBLE
				}
			}
		}
	}

	private fun getDataset(): List<Measurement> {
		val sharedPref = getSharedPreferences(MainActivity.HISTORY_FILE, MODE_PRIVATE)
		val savedString = sharedPref.getString(MainActivity.HISTORY_LIST, null) ?: return listOf()
		return gson.fromJson(savedString, MainActivity.HISTORY_TYPE)
	}
}
