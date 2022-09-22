package com.travelblog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.travelblog.databinding.ActivityLoginBinding

class LoginActivity: AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val preferences: BlogPreferences by lazy {
        BlogPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (preferences.isLoggedIn()) {
            startMainActivity()
            finish()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener{ onLoginClicked()}

        binding.textUsernameInput.editText
            ?.addTextChangedListener(createTextWatcher(binding.textUsernameInput))

        binding.textPasswordInput.editText
            ?.addTextChangedListener(createTextWatcher(binding.textPasswordInput))

    }

    private fun onLoginClicked() {
        val username: String = binding.textUsernameInput.editText?.text.toString()
        val password: String = binding.textPasswordInput.editText?.text.toString()
        if (username.isEmpty()) {
            binding.textUsernameInput.error = "Username cannot be blank"
        } else if (password.isEmpty()) {
            binding.textPasswordInput.error = "Password cannot be blank"
        } else if (username != "user" && password != "user") {
            showErrorDialog()
        } else {
            onLogin()
        }
    }

    private fun createTextWatcher(textPasswordInput: TextInputLayout): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                textPasswordInput.error = null
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        }
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Incorrect")
            .setMessage("Your username or password is incorrect. Please try again.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss()}
            .show()
    }

    private fun onLogin() {
        preferences.setLoggedIn(true)
        binding.textUsernameInput.isEnabled = false
        binding.textPasswordInput.isEnabled = false
        binding.loginButton.visibility = View.INVISIBLE
        binding.ProgressBar.visibility = View.VISIBLE

        Handler().postDelayed({
            startMainActivity()
            finish()
        }, 2000)
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}