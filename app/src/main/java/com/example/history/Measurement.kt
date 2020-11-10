package com.example.history

import java.util.*

class Measurement(val bmi: Double, val weight: Double, val height: Double, val date: Date, val units: UNITS) {
	companion object {
		enum class UNITS {
			NORMAL, AMERICAN, NOT_DEFINED
		}
	}
}
