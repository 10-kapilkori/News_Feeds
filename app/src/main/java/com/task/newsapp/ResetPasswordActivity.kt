package com.task.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.task.newsapp.databinding.ActivityResetPasswordBinding
import com.task.newsapp.entity.UserEntity
import com.task.newsapp.ui.saved.NewsViewModel
import com.task.newsapp.ui.saved.UserViewModel
import com.task.newsapp.ui.saved.UserViewModelFactory

private const val TAG = "ResetPasswordActivity"

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetPasswordBinding

    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).db.dao)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            resetPassBtn.setOnClickListener {
                val newPass = newPassEtOtpIn.text.toString()
                val confirmPass = confirmPassEtSignIn.text.toString()

                if (newPass.isEmpty()) {
                    newPassEtOtpIn.error = "Password Required"
                    newPassEtOtpIn.requestFocus()
                    return@setOnClickListener
                }

                if (newPass.length !in 4..25) {
                    newPassEtOtpIn.error = "Password Length should be between 4 and 25 characters"
                    newPassEtOtpIn.requestFocus()
                    return@setOnClickListener
                }

                if (confirmPass.isEmpty()) {
                    confirmPassEtSignIn.error = "Confirm Password Required"
                    confirmPassEtSignIn.requestFocus()
                    return@setOnClickListener
                }

                if (newPass != confirmPass) {
                    newPassEtOtpIn.error = "Password does not match"
                    newPassEtOtpIn.requestFocus()
                    return@setOnClickListener
                }

                val email = intent.getStringExtra("email")
                viewModel.fetchUsers().observe(this@ResetPasswordActivity) {
                    if (it.isNotEmpty()) {
                        Log.i(TAG, "onCreate: users $it")
                        var user: UserEntity? = null
                        for (i in it.indices) {
                            if (email == it[i].email) {
                                user = it[i]
                                Log.i(TAG, "onCreate: inside $user")
                            }
                        }
                        Log.i(TAG, "onCreate: $user")
                        if (user != null) {
                            viewModel.updateUser(UserEntity(user.name, user.email, newPass))
//                            Toast.makeText(
//                                this@ResetPasswordActivity,
//                                "Password Updated",
//                                Toast.LENGTH_SHORT
//                            ).show()
                            finish()
                        }
                    }
                }
            }
        }
    }
}























