package com.example.musicapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.createAccountBtn.setOnClickListener {

            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()

            if(!Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(),email)){
               binding.emailEditText.setError("Invalid Email")
                return@setOnClickListener
            }

            if(password.length < 6){
                binding.passwordEditText.setError("length should be 6 char")
                return@setOnClickListener
            }

            if (!password.equals(confirmPassword)){
                binding.confirmPasswordEditText.setError("passwords not matched")
                return@setOnClickListener
            }

            createAccountWithFirebase(email,password)
        }

        binding.gotoLoginBtn.setOnClickListener {
           finish()
        }
    }

    private fun createAccountWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                setInProgress(false)
                Toast.makeText(applicationContext,"Create account success",Toast.LENGTH_SHORT).show()
                finish()
            }.addOnFailureListener {
                setInProgress(false)
                Toast.makeText(applicationContext,"Create account failed",Toast.LENGTH_SHORT).show()
            }
    }

    private fun setInProgress(inProgress: Boolean){
        if (inProgress){
            binding.createAccountBtn.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.createAccountBtn.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE
        }
    }




}