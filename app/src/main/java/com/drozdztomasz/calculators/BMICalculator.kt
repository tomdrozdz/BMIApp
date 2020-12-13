package com.drozdztomasz.calculators

const val UNDERWEIGHT_THRESHOLD: Double = 18.5
const val OVERWEIGHT_THRESHOLD: Double = 25.0
const val OBESE_THRESHOLD: Double = 30.0

interface BMICalculator {
    enum class STATUS {
        VALID, TOO_BIG, TOO_SMALL
    }

    fun count(mass: Double, height: Double): Double
    fun checkMass(mass: Double): STATUS
    fun checkHeight(height: Double): STATUS
}
