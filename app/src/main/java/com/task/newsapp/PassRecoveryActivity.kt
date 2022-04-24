package com.task.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.task.newsapp.databinding.ActivityPassRecoveryBinding
import com.task.newsapp.ui.saved.UserViewModel
import com.task.newsapp.ui.saved.UserViewModelFactory

private const val TAG = "PassRecoveryActivity"

class PassRecoveryActivity : AppCompatActivity() {

    lateinit var binding: ActivityPassRecoveryBinding
    private val viewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as UserApplication).db.dao)
    }

    var flag = false
    var randomNum = 0
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPassRecoveryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            otpGenerateBtn.setOnClickListener {
                val otp = otpEtSignIn.text.toString()
                val email = emailEtOtpIn.text.toString().trim()

                if (email.isEmpty()) {
                    emailEtOtpIn.error = "Email Required"
                    emailEtOtpIn.requestFocus()
                    return@setOnClickListener
                }

                emailEtOtpIn.isEnabled = false
                otpEtSignIn.isEnabled = true

                viewModel.fetchUsers().observe(this@PassRecoveryActivity) {
                    if (it.isNotEmpty()) {
                        for (element in it) {
                            if (email == element.email) {
                                count++
                            }
                        }

                        if (count == 0) {
                            Toast.makeText(
                                this@PassRecoveryActivity,
                                "Invalid Email",
                                Toast.LENGTH_SHORT
                            ).show()
                            emailEtOtpIn.isEnabled = true
                            otpEtSignIn.isEnabled = false
                        }
                    }

                    if (!flag && count > 0) {
                        otpGenerateBtn.text = resources.getString(R.string.verify)
                        randomNum = (100000 until 999999).random()
                        Toast.makeText(
                            this@PassRecoveryActivity,
                            randomNum.toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        flag = true
                    } else if (count > 0) {
                        if (randomNum.toString() == otp) {
                            Toast.makeText(
                                this@PassRecoveryActivity,
                                "Verified",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            startActivity(
                                Intent(
                                    this@PassRecoveryActivity,
                                    ResetPasswordActivity::class.java
                                )
                                    .putExtra("email", emailEtOtpIn.text.toString())
                            )
                            finish()
                        } else
                            Toast.makeText(
                                this@PassRecoveryActivity,
                                "Not Verified",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        flag = false
                    }
                }
            }
        }
    }
}















