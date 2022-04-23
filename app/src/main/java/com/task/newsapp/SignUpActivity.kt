package com.task.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import com.task.newsapp.databinding.ActivitySignUpBinding
import com.task.newsapp.entity.UserEntity
import com.task.newsapp.ui.saved.UserViewModel
import com.task.newsapp.ui.saved.UserViewModelFactory
import java.util.regex.Pattern

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).db.dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            signUpBtn.setOnClickListener {
                val name = usernameEtSignIn.text.toString()
                val email = emailEtSignIn.text.toString()
                val password = passwordEtSignIn.text.toString()
                val confirmPass = confirmPasswordEtSignIn.text.toString()

                if (name.isEmpty()) {
                    usernameEtSignIn.error = "Username Required"
                    usernameEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (name.length < 2) {
                    usernameEtSignIn.error = "Username too small"
                    usernameEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (name.length > 15) {
                    usernameEtSignIn.error = "Username too long"
                    usernameEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (email.isEmpty()) {
                    emailEtSignIn.error = "Email Address Required"
                    emailEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailEtSignIn.error = "Invalid Email Address"
                    emailEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (password.isEmpty()) {
                    passwordEtSignIn.error = "Password Required"
                    passwordEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (password.length !in 4..25) {
                    passwordEtSignIn.error = "Password Length should be between 4 and 25 characters"
                    passwordEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (name == password) {
                    passwordEtSignIn.error = "Password cannot be your name"
                    passwordEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (confirmPass.isEmpty()) {
                    confirmPasswordEtSignIn.error = "Confirm Password Required"
                    confirmPasswordEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (password != confirmPass) {
                    confirmPasswordEtSignIn.error = "Password does not match"
                    confirmPasswordEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                viewModel.insertUser(UserEntity(name = name, email = email, password = password))
                Toast.makeText(this@SignUpActivity, "User Created", Toast.LENGTH_SHORT).show()
                finish()
            }

            alreadyUserBtn.setOnClickListener {
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}