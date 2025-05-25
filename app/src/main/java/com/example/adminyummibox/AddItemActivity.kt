package com.example.adminyummibox

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.adminyummibox.databinding.ActivityAddItemBinding
import com.example.adminyummibox.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.adminyummibox.utils.SupabaseClient
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull as toMediaTypeOrNull1

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
//                Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
//                finish()
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
        val menuRef = database.getReference("menu")
        val newItemKey = menuRef.push().key

        if (foodImageUri != null && newItemKey != null) {
            //  استخدم Supabase بدل Firebase Storage
            uploadImageToSupabase(foodImageUri!!) { imageUrl ->
                val newItem = AllMenu(
                    foodName = foodName,
                    foodPrice = foodPrice,
                    foodDescription = foodDescription,
                    foodIngredient = foodIngredint,
                    foodImage = imageUrl
                )

                menuRef.child(newItemKey).setValue(newItem)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Data Uploaded Successfully 🤗", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Data Uploaded Failed 😥", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(this, "Please Select an image 😊", Toast.LENGTH_SHORT).show()
        }
    }

        private fun uploadImageToSupabase(uri: Uri, onSuccess: (imageUrl: String) -> Unit) {
            // 1. ضغط الصورة وتحويلها إلى ByteArray
            val inputStream = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val outputStream = java.io.ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream) // جودة 50%
            val compressedBytes = outputStream.toByteArray()

            // 2. تجهيز البيانات للرفع
            val requestFile = compressedBytes.toRequestBody("image/jpeg".toMediaTypeOrNull1())
            val fileName = "compressed_${System.currentTimeMillis()}.jpg"
            val body = MultipartBody.Part.createFormData("file", fileName, requestFile)

            // 3. تنفيذ الاتصال عبر Retrofit
            val call = SupabaseClient.service.uploadImage(
                SupabaseClient.getApiKey(),
                SupabaseClient.getAuthorization(),
                body,
                "images",
                fileName
            )


            call.enqueue(object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
//                        val imageUrl = "https://supabase.com/dashboard/project/wjejxjzodprauxdjtiys/storage/buckets/images/$fileName"
                        val imageUrl = "https://wjejxjzodprauxdjtiys.supabase.co/storage/v1/object/public/images/$fileName"
//                        Glide.with(this@AddItemActivity)
//                            .load(imageUrl)
//                            .into(binding.selectedImage)
                        onSuccess(imageUrl)
                    } else {
                        Log.e("Supabase", "Upload failed: code=${response.code()} body=${response.errorBody()?.string()}")
                        Toast.makeText(this@AddItemActivity, "Failed uploading image: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@AddItemActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.selectedImage.setImageURI(uri)
            foodImageUri = uri
        }
    }
}