package com.example.bmiapp

import com.example.bmicalculating.AmericanCalculator
import com.example.bmicalculating.BMICalculator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.lang.IllegalArgumentException

class AmericanCalculatorTest : StringSpec({
	"AmericanCalculator.count should return correct BMI value" {
		forAll(
				row(150.0, 65.0, 24.959),
				row(200.0, 60.0, 39.056),
				row(100.0, 70.0, 14.347)
		) { mass, height, result ->
			AmericanCalculator.count(mass, height) shouldBe result.plusOrMinus(0.001)
		}
	}

	"AmericanCalculator.count should throw an IllegalArgumentException for mass <= 0" {
		forAll(
				row(0.0, 65.0),
				row(-1.0, 65.0)
		) { mass, height ->
			val exception = shouldThrow<IllegalArgumentException> {
				AmericanCalculator.count(mass, height)
			}
			exception.message shouldBe "Mass is <= 0"
		}
	}

	"AmericanCalculator.count should throw an IllegalArgumentException for height <= 0" {
		forAll(
				row(150.0, 0.0),
				row(150.0, -1.0)
		) { mass, height ->
			val exception = shouldThrow<IllegalArgumentException> {
				AmericanCalculator.count(mass, height)
			}
			exception.message shouldBe "Height is <= 0"
		}
	}

	"AmericanCalculator.count should throw an IllegalArgumentException for mass and height <= 0" {
		forAll(
				row(0.0, 0.0),
				row(-1.0, 0.0),
				row(0.0, -1.0),
				row(-1.0, -1.0)
		) { mass, height ->
			val exception = shouldThrow<IllegalArgumentException> {
				AmericanCalculator.count(mass, height)
			}
			exception.message shouldBe "Mass is <= 0"
		}
	}

	"AmericanCalculator.checkMass should detect mass that is too small" {
		forAll(
				row(65.0),
				row(0.0),
				row(-1.0)
		) { mass ->
			AmericanCalculator.checkMass(mass) shouldBe BMICalculator.STATUS.TOO_SMALL
		}
	}

	"AmericanCalculator.checkMass should detect mass that is valid" {
		forAll(
				row(66.0),
				row(150.0),
				row(440.0)
		) { mass ->
			AmericanCalculator.checkMass(mass) shouldBe BMICalculator.STATUS.VALID
		}
	}

	"AmericanCalculator.checkMass should detect mass that is too big" {
		forAll(
				row(441.0),
				row(750.0),
				row(1500.0)
		) { mass ->
			AmericanCalculator.checkMass(mass) shouldBe BMICalculator.STATUS.TOO_BIG
		}
	}

	"AmericanCalculator.checkHeight should detect height that is too small" {
		forAll(
				row(47.0),
				row(0.0),
				row(-1.0)
		) { height ->
			AmericanCalculator.checkHeight(height) shouldBe BMICalculator.STATUS.TOO_SMALL
		}
	}

	"AmericanCalculator.checkHeight should detect height that is valid" {
		forAll(
				row(48.0),
				row(65.0),
				row(84.0)
		) { height ->
			AmericanCalculator.checkHeight(height) shouldBe BMICalculator.STATUS.VALID
		}	}

	"AmericanCalculator.checkHeight should detect height that is too big" {
		forAll(
				row(85.0),
				row(100.0),
				row(200.0)
		) { height ->
			AmericanCalculator.checkHeight(height) shouldBe BMICalculator.STATUS.TOO_BIG
		}
	}
})
