package com.task.newsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.task.newsapp.databinding.ActivitySignInBinding
import com.task.newsapp.ui.saved.UserViewModel
import com.task.newsapp.ui.saved.UserViewModelFactory

private const val TAG = "SignUpActivity"

class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).db.dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spf = getSharedPreferences("news", MODE_PRIVATE)
        val loggedEmail = spf.getString("email", "")
        Log.i(TAG, "onCreate: $loggedEmail")

        if (loggedEmail != "") {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        with(binding) {
            signInBtn.setOnClickListener {
                val email = emailEtSignIn.text.toString()
                val password = passwordEtSignIn.text.toString()

                viewModel.fetchUsers().observe(this@SignInActivity) {
                    Log.i(TAG, "onCreate: $it")
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

                viewModel.fetchUser(email, password).observe(this@SignInActivity) {
                    if (it != null) {
                        Toast.makeText(this@SignInActivity, "Logged In", Toast.LENGTH_SHORT).show()
                        startActivity(
                            Intent(
                                this@SignInActivity,
                                HomeActivity::class.java
                            ).putExtra("email", email)
                        )
                        finish()
                    } else {
                        Toast.makeText(
                            this@SignInActivity,
                            "Invalid email or password",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            newUserCreateBtn.setOnClickListener {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            }
        }
    }
}