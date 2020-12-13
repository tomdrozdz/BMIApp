package com.drozdztomasz.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.drozdztomasz.database.BMIDatabase
import com.drozdztomasz.databinding.HistoryFragmentBinding

class HistoryFragment : Fragment() {
    private lateinit var binding: HistoryFragmentBinding
    private lateinit var viewModel: HistoryViewModel

    private lateinit var viewAdapter: MeasurementAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HistoryFragmentBinding.inflate(layoutInflater)

        val application = requireNotNull(this.activity).application
        val dataSource = BMIDatabase.getInstance(application).bmiDatabaseDao
        val viewModelFactory = HistoryViewModelFactory(dataSource)
        viewModel = ViewModelProvider(this, viewModelFactory).get(HistoryViewModel::class.java)

        val viewManager = LinearLayoutManager(context)
        viewAdapter = MeasurementAdapter()

        binding.recyclerView.layoutManager = viewManager
        binding.recyclerView.adapter = viewAdapter

        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        viewModel.measurements.observe(
            viewLifecycleOwner,
            Observer {
                it?.let {
                    if (it.isEmpty()) {
                        binding.recyclerView.visibility = View.GONE
                        binding.emptyTV.visibility = View.VISIBLE
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.emptyTV.visibility = View.GONE
                    }

                    viewAdapter.measurements = it
                }
            })
    }
}
