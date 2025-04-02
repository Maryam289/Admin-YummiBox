package com.example.adminyummibox

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminyummibox.databinding.ActivityAdminProfileBinding

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.backButton.setOnClickListener {
            finish()
        }

        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable != isEnable
            binding.name.isEnabled = !isEnable
            binding.name.setTextColor(Color.BLACK)
            binding.address.isEnabled = !isEnable
            binding.address.setTextColor(Color.BLACK)
            binding.email.isEnabled = !isEnable
            binding.email.setTextColor(Color.BLACK)
            binding.phone.isEnabled = isEnable
            binding.phone.setTextColor(Color.BLACK)
            binding.password.isEnabled = !isEnable
            binding.password.setTextColor(Color.BLACK)


            if (isEnable){
                binding.name.requestFocus()
            }
        }

    }
}