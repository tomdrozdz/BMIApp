package com.drozdztomasz.calculators

class NormalCalculator {
    companion object : ConstraintCalculator() {
        override val minHeight: Double = 100.0
        override val maxHeight: Double = 250.0
        override val minMass: Double = 30.0
        override val maxMass: Double = 200.0

        override fun count(mass: Double, height: Double): Double {
            checkIllegalArguments(mass, height)
            var heightInMeters = height / 100
            return mass / (heightInMeters * heightInMeters)
        }
    }
}
