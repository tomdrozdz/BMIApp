package com.example.bmiapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.bmiapp.databinding.ActivityDescBinding
import com.example.bmicalculating.OBESE_THRESHOLD
import com.example.bmicalculating.OVERWEIGHT_THRESHOLD
import com.example.bmicalculating.UNDERWEIGHT_THRESHOLD

class DescActivity : AppCompatActivity() {
	private lateinit var binding: ActivityDescBinding

	companion object {
		const val BMI_VALUE: String = "BMI_VALUE"
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityDescBinding.inflate(layoutInflater)
		setContentView(binding.root)

		displayData()
	}

	private fun displayData() {
		val bmi: Double = intent.getDoubleExtra(BMI_VALUE, 0.0)

		binding.apply {
			descBmiTV.text = "%.2f".format(bmi)

			var imgId: Int = R.drawable.down
			val colorId: Int
			val textId: Int

			when {
				bmi < UNDERWEIGHT_THRESHOLD -> {
					textId = R.string.underweight_desc
					colorId = R.color.underweight
				}
				bmi < OVERWEIGHT_THRESHOLD -> {
					textId = R.string.normal_desc
					colorId = R.color.normal_weight
					imgId = R.drawable.up
				}
				bmi < OBESE_THRESHOLD -> {
					textId = R.string.overweight_desc
					colorId = R.color.overweight
				}
				else -> {
					textId = R.string.obese_desc
					colorId = R.color.obese
				}
			}

			descTV.text = getString(textId)
			descBmiTV.setTextColor(getColor(colorId))
			thumbsIV.setImageDrawable(ResourcesCompat.getDrawable(resources, imgId, theme))
		}
	}
}
