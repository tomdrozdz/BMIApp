package com.drozdztomasz.calculators

import java.lang.IllegalArgumentException

abstract class ConstraintCalculator : BMICalculator {
	protected abstract val minHeight: Double
	protected abstract val maxHeight: Double
	protected abstract val minMass: Double
	protected abstract val maxMass: Double

	protected fun checkIllegalArguments(mass: Double, height: Double) {
		if (mass <= 0)
			throw IllegalArgumentException("Mass is <= 0")
		if (height <= 0)
			throw IllegalArgumentException("Height is <= 0")
	}

	override fun checkMass(mass: Double): BMICalculator.STATUS {
		return when {
			mass > maxMass -> BMICalculator.STATUS.TOO_BIG
			mass < minMass -> BMICalculator.STATUS.TOO_SMALL
			else -> BMICalculator.STATUS.VALID
		}
	}

	override fun checkHeight(height: Double): BMICalculator.STATUS {
		return when {
			height > maxHeight -> BMICalculator.STATUS.TOO_BIG
			height < minHeight -> BMICalculator.STATUS.TOO_SMALL
			else -> BMICalculator.STATUS.VALID
		}
	}
}
