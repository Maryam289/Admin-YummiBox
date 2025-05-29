package com.example.adminyummibox.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminyummibox.databinding.PendingOrderItemBinding

class PendingOrderAdapter(private val context: Context,
                          private val customerNamesItem: MutableList<String>,
                          private val quantity: MutableList<String>,
                          private val foodImagesItem: MutableList<String>,
                          private val itemClicked: OnItemClicked
): RecyclerView.Adapter<PendingOrderAdapter.PandingOrderViewHolder>() {


    interface OnItemClicked{
        fun onItemClickListener(position: Int)
        fun onItemAcceptClickListener(position: Int)
        fun onItemDispatchClickListener(position: Int)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PandingOrderViewHolder {
        val binding = PendingOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PandingOrderViewHolder(binding)
    }

   override fun onBindViewHolder(holder: PandingOrderViewHolder, position: Int) {
       holder.bind(position)
   }

    override fun getItemCount(): Int = customerNamesItem.size


    inner class PandingOrderViewHolder(private val binding: PendingOrderItemBinding): RecyclerView.ViewHolder(binding.root) {
        private var isAccepted = false
        fun bind(position: Int) {
            binding.apply {
                customerName.text = customerNamesItem[position]
                pendingOrderQuantity.text = quantity[position]
//                orderedFoodImage.setImageResource(foodImagesItem[position])
                var uriString = foodImagesItem[position]
                var uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderedFoodImage)

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
                            itemClicked.onItemAcceptClickListener(position)
                        } else{
                            customerNamesItem.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                            showToast("Order is dispatched")
                            itemClicked.onItemDispatchClickListener(position)
                        }
                    }
                }
                itemView.setOnClickListener {
                    itemClicked.onItemClickListener(position)
                }
            }
        }
        fun showToast(message: String){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}

fun PendingOrderAdapter.OnItemClicked.onItemClickListener(position: Int) {
    TODO("Not yet implemented")
}