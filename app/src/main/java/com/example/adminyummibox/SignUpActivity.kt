package com.example.adminyummibox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.adminyummibox.databinding.ActivitySignUpBinding
import com.example.adminyummibox.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class SignUpActivity : AppCompatActivity() {

    private lateinit var userName: String
    private lateinit var nameOfRestaurant: String
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private val binding: ActivitySignUpBinding by lazy{
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //initialize Firebase Auth
        auth = Firebase.auth
        database = Firebase.database.reference

        binding.createButton.setOnClickListener {
            //get text from editText
            userName = binding.name.text.toString().trim()
            nameOfRestaurant = binding.nameOfRestaurant.text.toString().trim()
            email = binding.emailOrPhone.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (userName.isBlank() || nameOfRestaurant.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill the details", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email, password)
            }
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
        }
        binding.alreadyHaveAccountButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val locationList = arrayOf("Russia", "Egypt", "Franca", "China", "Canada")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, locationList)
        val autoCompleteTextView = binding.listOfLocation
        autoCompleteTextView.setAdapter(adapter)

    }

    private fun createAccount(email: String, password: String) {
         auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
             if (task.isSuccessful){
                 Toast.makeText(this, "Account created Successfully", Toast.LENGTH_SHORT).show()
                 saveUserData()
                 val intent = Intent(this, LoginActivity::class.java)
                 startActivity(intent)
                 finish()
             }else{
                 Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                 Log.d("Account", "createAccount: Failure", task.exception)

             }

         }
    }

//    save data in to database
    private fun saveUserData() {
        //get text from editText
        userName = binding.name.text.toString().trim()
        nameOfRestaurant = binding.nameOfRestaurant.text.toString().trim()
        email = binding.emailOrPhone.text.toString().trim()
        password = binding.password.text.toString().trim()
        val user = UserModel(userName, nameOfRestaurant, email, password)
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
//        save user data Firebase database
        database.child("user").child(userId).setValue(user)
    }
}