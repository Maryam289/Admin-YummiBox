package com.example.adminyummibox

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminyummibox.adapter.DeliveryAdapter
import com.example.adminyummibox.databinding.ActivityOutForDeliveryBinding

class OutForDeliveryActivity : AppCompatActivity() {
    private val binding : ActivityOutForDeliveryBinding by lazy{
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val customerName = arrayListOf(
            "John michail",
            "Andray John",
            "Anna Doe",
            "Sasha Johnson"
        )

        val moneyStatus = arrayListOf(
            "Not Received",
            "Received",
            "Payment",
            "Test"
        )

        val adapter = DeliveryAdapter(customerName, moneyStatus)
        binding.deliveryRecyclerView.adapter = adapter
        binding.deliveryRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}