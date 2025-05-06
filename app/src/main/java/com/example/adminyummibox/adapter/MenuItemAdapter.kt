package com.example.adminyummibox.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminyummibox.databinding.ItemMenuBinding
import com.example.adminyummibox.model.AllMenu
import com.google.firebase.database.DatabaseReference

class MenuItemAdapter(
//    private val MenuItemName: ArrayList<String>,
//    private val MenuItemPrice: ArrayList<String>,
//    private val MenuItemImage: ArrayList<Int>
    private val context: Context,
    private val menuList: ArrayList<AllMenu>,
    databaseReference: DatabaseReference
): RecyclerView.Adapter<MenuItemAdapter.AddItemViewHolder>() {

    private val itemQuantities = IntArray(menuList.size){ 1 }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddItemViewHolder {
        val binding = ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AddItemViewHolder(binding)
    }

    override fun getItemCount(): Int = menuList.size

    override fun onBindViewHolder(holder: AddItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class AddItemViewHolder(private val binding:ItemMenuBinding) :RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodName
                val uri = Uri.parse(uriString)
                menuFoodName.text = menuItem.foodName
                menuItemPrice.text = menuItem.foodPrice
                Glide.with(context).load(uri).into(foodImageView)
//                foodImageView.setImageResource(menuList[position])
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
            menuList.removeAt(itemPosition)
            menuList.removeAt(itemPosition)
            menuList.removeAt(itemPosition)
            notifyItemRemoved(itemPosition)
            notifyItemRangeChanged(itemPosition, menuList.size)
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