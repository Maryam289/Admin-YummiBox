package com.example.adminyummibox

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminyummibox.adapter.PendingOrderAdapter
import com.example.adminyummibox.databinding.ActivityPendingOrderBinding

class PendingOrderActivity : AppCompatActivity() {
    private val binding: ActivityPendingOrderBinding by lazy {
        ActivityPendingOrderBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val orderedCustomerName = arrayListOf(
            "John michail",
            "Andray John",
            "Anna Doe",
            "Sasha Johnson"
        )

        val orderedQuantity = arrayListOf(
            "9",
            "8",
            "2",
            "6"
        )

        val orderedFoodImages = arrayListOf(
            R.drawable.herring_under_afur_coat,
            R.drawable.chicken_kiev,
            R.drawable.pancakeswith_red_caviar,
            R.drawable.pancakes
        )

        val adapter = PendingOrderAdapter(orderedCustomerName, orderedQuantity, orderedFoodImages, this)
        binding.pendingOrderRecyclerView.adapter = adapter
        binding.pendingOrderRecyclerView.layoutManager = LinearLayoutManager(this)

        binding.backButton.setOnClickListener {
            finish()
        }

    }
}