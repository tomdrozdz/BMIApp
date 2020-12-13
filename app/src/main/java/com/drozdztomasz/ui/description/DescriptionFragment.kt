package com.drozdztomasz.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.content.ContextCompat.getDrawable
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.drozdztomasz.R
import com.drozdztomasz.calculators.OBESE_THRESHOLD
import com.drozdztomasz.calculators.OVERWEIGHT_THRESHOLD
import com.drozdztomasz.calculators.UNDERWEIGHT_THRESHOLD
import com.drozdztomasz.databinding.DescriptionFragmentBinding

class DescriptionFragment : Fragment() {
    private lateinit var binding: DescriptionFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DescriptionFragmentBinding.inflate(layoutInflater)

        val args: DescriptionFragmentArgs by navArgs()
        displayData(args.bmi.toDouble())

        return binding.root
    }

    private fun displayData(bmi: Double) {
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
            descBmiTV.setTextColor(getColor(requireContext(), colorId))
            thumbsIV.setImageDrawable(getDrawable(requireContext(), imgId))
        }
    }
}
