// Tomasz Drozdz, 246718
// Testowane na emulatorze + Samsung Galaxy Note 9

package com.drozdztomasz.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drozdztomasz.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
