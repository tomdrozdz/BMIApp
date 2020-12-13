package com.drozdztomasz.ui.history

import androidx.lifecycle.ViewModel
import com.drozdztomasz.database.BMIDatabaseDao

class HistoryViewModel(dataSource: BMIDatabaseDao) : ViewModel() {
    val database = dataSource
    val measurements = database.getAllMeasurements()
}
