package com.drozdztomasz.calculators

class AmericanCalculator {
    companion object : ConstraintCalculator() {
        override val minHeight: Double = 48.0
        override val maxHeight: Double = 84.0
        override val minMass: Double = 66.0
        override val maxMass: Double = 440.0

        override fun count(mass: Double, height: Double): Double {
            checkIllegalArguments(mass, height)
            return mass / (height * height) * 703
        }
    }
}
