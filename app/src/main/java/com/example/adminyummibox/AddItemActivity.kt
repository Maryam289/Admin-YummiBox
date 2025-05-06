package com.example.adminyummibox

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminyummibox.databinding.ActivityAddItemBinding
import com.example.adminyummibox.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.IOException

class AddItemActivity : AppCompatActivity() {

    //    Food item Details
    private lateinit var foodName: String
    private lateinit var foodPrice: String
    private lateinit var foodDescription: String
    private lateinit var foodIngredint: String
    private var foodImageUri: Uri? = null

    // Firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding: ActivityAddItemBinding by lazy {
        ActivityAddItemBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initialize Firebase()
        auth = FirebaseAuth.getInstance()
        // initialize Firebase database instance()
        database = FirebaseDatabase.getInstance()

        binding.addItemButton.setOnClickListener {
            // Get Data from Filed
            foodName = binding.nameFood.text.toString().trim()
            foodPrice = binding.priceFood.text.toString().trim()
            foodDescription = binding.descriptionText.text.toString().trim()
            foodIngredint = binding.ingredientsText.text.toString().trim()

            if (!(foodName.isBlank() || foodPrice.isBlank() || foodDescription.isBlank() || foodIngredint.isBlank())) {
                uploadData()
                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fill all the details", Toast.LENGTH_SHORT).show()
            }
        }
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            finish()
        }
    }

    private fun uploadData() {
//        get a reference to the "menu" node in the database
        val menuRef = database.getReference("menu")
//        Generate a unique key for the new menu item
        val newItemKey = menuRef.push().key

        if (foodImageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("menu_images/${newItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImageUri!!)

//        if (foodImageUri != null && newItemKey != null) {
//            // Convert URI to byte array
//            val inputStream = contentResolver.openInputStream(foodImageUri!!)
//            val fileBytes = inputStream?.readBytes()
//            inputStream?.close()


            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                    create a new menu item
                    val newItem = AllMenu(
                        foodName = foodName,
                        foodPrice = foodPrice,
                        foodDescription = foodDescription,
                        foodIngredient = foodIngredint,
                        foodImage = downloadUrl.toString(),
                    )
                    newItemKey?.let { key ->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "Data Uploaded Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }
                    }
                }
            }
                .addOnFailureListener {
                    Toast.makeText(this, "Image Upload Failed", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Please Select an image", Toast.LENGTH_SHORT).show()
        }
    }


//            if (fileBytes != null) {
//                uploadToSupabase(fileBytes, "$newItemKey.jpg") { imageUrl ->
//                    if (imageUrl != null) {
//                        val newItem = AllMenu(
//                            foodName = foodName,
//                            foodPrice = foodPrice,
//                            foodDescription = foodDescription,
//                            foodIngredient = foodIngredint,
//                            foodImage = imageUrl
//                        )
//                        menuRef.child(newItemKey).setValue(newItem)
//                            .addOnSuccessListener {
//                                Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT).show()
//                            }
//                    } else {
//                        Toast.makeText(this, "Image upload failed", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        } else {
//            Toast.makeText(this, "Please Select an image", Toast.LENGTH_SHORT).show()
//        }
//    }

//    private fun uploadToSupabase(fileBytes: ByteArray, fileName: String, callback: (String?) -> Unit) {
//        val supabaseUrl = "https://wjejxjzodprauxdjtiys.supabase.co"
//        val bucket = "images"
//        val apiKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6IndqZWp4anpvZHByYXV4ZGp0aXlzIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDYzNzYxNDMsImV4cCI6MjA2MTk1MjE0M30.5LsQa51okC7owyYLPTTB03XFw0srhjwgAks260ERmIo"
//
//        val uploadUrl = "$supabaseUrl/storage/v1/object/$bucket/$fileName"
//        val client = okhttp3.OkHttpClient()
//
//        val requestBody = okhttp3.RequestBody.create(
//            "image/jpeg".toMediaTypeOrNull(),
//            fileBytes
//        )
//
//        val request = okhttp3.Request.Builder()
//            .url(uploadUrl)
//            .header("Authorization", "Bearer $apiKey")
//            .header("Content-Type", "image/jpeg")
//            .put(requestBody)
//            .build()
//
//        client.newCall(request).enqueue(object : okhttp3.Callback {
//            override fun onFailure(call: okhttp3.Call, e: IOException) {
//                e.printStackTrace()
//                runOnUiThread { callback(null) }
//            }
//
//            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
//                if (response.isSuccessful) {
//                    // Public URL if the bucket is public
//                    val imageUrl = "$supabaseUrl/storage/v1/object/public/$bucket/$fileName"
//                    runOnUiThread { callback(imageUrl) }
//                } else {
////                    println("Upload failed: ${response.code()} - ${response.body()?.string()}")
//                    runOnUiThread { callback(null) }
//                }
//            }
//        })
//    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}