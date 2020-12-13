package com.drozdztomasz.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.drozdztomasz.database.BMIDatabaseDao

class CalculatorViewModelFactory(
    private val dataSource: BMIDatabaseDao,
    private val defaultTvColor: Int
) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalculatorViewModel::class.java)) {
            return CalculatorViewModel(dataSource, defaultTvColor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
