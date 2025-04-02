package com.example.adminyummibox.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.adminyummibox.databinding.PendingOrderItemBinding

class PendingOrderAdapter(private val CustomerNamesItem: ArrayList<String>,
                          private val Quantity: ArrayList<String>,
                          private val FoodImagesItem: ArrayList<Int>,
                          private val context: Context):
    RecyclerView.Adapter<PendingOrderAdapter.PandingOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PandingOrderViewHolder {
        val binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PandingOrderViewHolder(binding)
    }

   override fun onBindViewHolder(holder: PandingOrderViewHolder, position: Int) {
       holder.bind(position)
   }

    override fun getItemCount(): Int = CustomerNamesItem.size


    inner class PandingOrderViewHolder(private val binding: PendingOrderItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = CustomerNamesItem[position]
                pendingOrderQuantity.text = Quantity[position]
                orderedFoodImage.setImageResource(FoodImagesItem[position])

                orderedAcceptButton.apply {
                    if (!isAccepted){
                        text = "Accept"
                    }else{
                        text = "Dispatch"
                    }
                    setOnClickListener {
                        if (!isAccepted){
                            text = "Dispatch"
                            isAccepted = true
                            showToast("Order is accepted")
                        } else{
                            CustomerNamesItem.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched")
                        }
                    }
                }
            }
        }
        fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}