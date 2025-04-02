package com.example.adminyummibox.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.adminyummibox.databinding.ItemMenuBinding

class AddItemAdapter(private val MenuItemName: ArrayList<String>,
                     private val MenuItemPrice: ArrayList<String>,
                     private val MenuItemImage: ArrayList<Int>
): RecyclerView.Adapter<AddItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(MenuItemName.size){ 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AddItemViewHolder(binding)
    }

    override fun getItemCount(): Int = MenuItemName.size

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AddItemViewHolder(private val binding:ItemMenuBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                menuFoodName.text = MenuItemName[position]
                menuItemPrice.text = MenuItemPrice[position]
                foodImageView.setImageResource(MenuItemImage[position])
                menuItemQuantity.text = quantity.toString()

                minusButton.setOnClickListener {
                    deceaseQuantity(position)
                }

                plusButton.setOnClickListener {
                    increaseQuantity(position)
                }

                deleteButton.setOnClickListener {
                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION){
                        deleteItem(itemPosition)
                    }
                }
            }
        }

        private fun deleteItem(itemPosition: Int) {
            MenuItemName.removeAt(itemPosition)
            MenuItemImage.removeAt(itemPosition)
            MenuItemPrice.removeAt(itemPosition)
            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, MenuItemName.size)
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 100) {
                itemQuantities[position]++
                binding.menuItemQuantity.text = itemQuantities[position].toString()
            }        }

        private fun deceaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.menuItemQuantity.text = itemQuantities[position].toString()
            }
        }

    }
}