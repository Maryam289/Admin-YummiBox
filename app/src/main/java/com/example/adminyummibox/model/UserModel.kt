package com.example.adminyummibox.model

import okhttp3.Address

data class UserModel(
    val name:String? = null,
    val nameOfRestaurant:String? = null,
    val email:String? = null,
    val password:String? = null,
    val address: String? = null,
    val phone: String? = null
)
