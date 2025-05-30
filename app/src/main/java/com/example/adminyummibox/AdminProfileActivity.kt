package com.example.adminyummibox

import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.adminyummibox.databinding.ActivityAdminProfileBinding
import com.example.adminyummibox.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private val binding : ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")
        binding.backButton.setOnClickListener {
            finish()
        }
        binding.saveInformationButton.setOnClickListener {
            updateUserData()
        }

        binding.name.isEnabled = false
        binding.address.isEnabled = false
        binding.email.isEnabled = false
        binding.phone.isEnabled = false
        binding.password.isEnabled = false
        binding.saveInformationButton.isEnabled = false

        var isEnable = false
        binding.editButton.setOnClickListener {
            isEnable = !isEnable
            binding.name.isEnabled = isEnable
            binding.name.setTextColor(Color.BLACK)

            binding.address.isEnabled = isEnable
            binding.address.setTextColor(Color.BLACK)

            binding.email.isEnabled = isEnable
            binding.email.setTextColor(Color.BLACK)

            binding.phone.isEnabled = isEnable
            binding.phone.setTextColor(Color.BLACK)

            binding.password.isEnabled = isEnable
            binding.password.setTextColor(Color.BLACK)
            binding.password.inputType = InputType.TYPE_CLASS_TEXT


            if (isEnable){
                binding.name.requestFocus()
            }
            binding.saveInformationButton.isEnabled = isEnable
        }
        retriveveUserData()

    }



    private fun retriveveUserData() {
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null){
            val userReference = adminReference.child(currentUserUid)
            userReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        var ownerName = snapshot.child("name").getValue()
                        var ownerEmail = snapshot.child("email").getValue()
                        var ownerPassword = snapshot.child("password").getValue()
                        var ownerAddress = snapshot.child("address").getValue()
                        var ownerPhone = snapshot.child("phone").getValue()
                        setDataToTextView(ownerName, ownerEmail, ownerPassword, ownerAddress, ownerPhone)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    private fun setDataToTextView(
        ownerName: Any?,
        ownerEmail: Any?,
        ownerPassword: Any?,
        ownerAddress: Any?,
        ownerPhone: Any?
    ) {
        binding.name.setText(ownerName.toString())
        binding.email.setText(ownerEmail.toString())
        binding.password.setText(ownerPassword.toString())
        binding.address.setText(ownerAddress.toString())
        binding.phone.setText(ownerPhone.toString())
    }

    private fun updateUserData() {
        var updateName = binding.name.text.toString()
        var updateEmail = binding.email.text.toString()
        var updatePassword = binding.password.text.toString()
        var updateAddress = binding.address.text.toString()
        var updatePhone = binding.phone.text.toString()
        val currentUserUid = auth.currentUser?.uid
        if (currentUserUid != null){
            val userReference = adminReference.child(currentUserUid)
            userReference.child("name").setValue(updateName)
            userReference.child("email").setValue(updateEmail)
            userReference.child("password").setValue(updatePassword)
            userReference.child("address").setValue(updateAddress)
            userReference.child("phone").setValue(updatePhone)
            Toast.makeText(this, "Profile updated successfully 🤗", Toast.LENGTH_SHORT).show()

            auth.currentUser?.updateEmail(updateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        }else
//        var userdata = UserModel(updateName, "", updateEmail, updatePassword, updateAddress, updatePhone)
//        adminReference.setValue(userdata).addOnSuccessListener {
        {
            Toast.makeText(this, "Profile updated failed 😥", Toast.LENGTH_SHORT).show()

        }
    }
}