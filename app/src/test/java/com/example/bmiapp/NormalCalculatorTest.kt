package com.example.bmiapp

import com.example.bmicalculating.BMICalculator
import com.example.bmicalculating.NormalCalculator
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.doubles.plusOrMinus
import io.kotest.matchers.shouldBe
import java.lang.IllegalArgumentException

class NormalCalculatorTest : StringSpec({
	"NormalCalculator.count should return correct BMI value" {
		forAll(
				row(60.0, 170.0, 20.761),
				row(90.0, 160.0, 35.156),
				row(50.0, 180.0, 15.432)
		) { mass, height, result ->
			NormalCalculator.count(mass, height) shouldBe result.plusOrMinus(0.001)
		}
	}

	"NormalCalculator.count should throw an IllegalArgumentException for mass <= 0" {
		forAll(
				row(0.0, 150.0),
				row(-1.0, 150.0)
		) { mass, height ->
			val exception = shouldThrow<IllegalArgumentException> {
				NormalCalculator.count(mass, height)
			}
			exception.message shouldBe "Mass is <= 0"
		}
	}

	"NormalCalculator.count should throw an IllegalArgumentException for height <= 0" {
		forAll(
				row(50.0, 0.0),
				row(50.0, -1.0)
		) { mass, height ->
			val exception = shouldThrow<IllegalArgumentException> {
				NormalCalculator.count(mass, height)
			}
			exception.message shouldBe "Height is <= 0"
		}
	}

	"NormalCalculator.count should throw an IllegalArgumentException for mass and height <= 0" {
		forAll(
				row(0.0, 0.0),
				row(-1.0, 0.0),
				row(0.0, -1.0),
				row(-1.0, -1.0)
		) { mass, height ->
			val exception = shouldThrow<IllegalArgumentException> {
				NormalCalculator.count(mass, height)
			}
			exception.message shouldBe "Mass is <= 0"
		}
	}

	"NormalCalculator.checkMass should detect mass that is too small" {
		forAll(
				row(29.0),
				row(0.0),
				row(-1.0)
		) { mass ->
			NormalCalculator.checkMass(mass) shouldBe BMICalculator.STATUS.TOO_SMALL
		}
	}

	"NormalCalculator.checkMass should detect mass that is valid" {
		forAll(
				row(30.0),
				row(70.0),
				row(200.0)
		) { mass ->
			NormalCalculator.checkMass(mass) shouldBe BMICalculator.STATUS.VALID
		}
	}

	"NormalCalculator.checkMass should detect mass that is too big" {
		forAll(
				row(201.0),
				row(500.0),
				row(1000.0)
		) { mass ->
			NormalCalculator.checkMass(mass) shouldBe BMICalculator.STATUS.TOO_BIG
		}
	}

	"NormalCalculator.checkHeight should detect height that is too small" {
		forAll(
				row(99.0),
				row(0.0),
				row(-1.0)
		) { height ->
			NormalCalculator.checkHeight(height) shouldBe BMICalculator.STATUS.TOO_SMALL
		}
	}

	"NormalCalculator.checkHeight should detect height that is valid" {
		forAll(
				row(100.0),
				row(170.0),
				row(250.0)
		) { height ->
			NormalCalculator.checkHeight(height) shouldBe BMICalculator.STATUS.VALID
		}	}

	"NormalCalculator.checkHeight should detect height that is too big" {
		forAll(
				row(251.0),
				row(500.0),
				row(1000.0)
		) { height ->
			NormalCalculator.checkHeight(height) shouldBe BMICalculator.STATUS.TOO_BIG
		}
	}
})
