package com.example.yazwear.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yazwear.MainActivity
import com.example.yazwear.R
import com.example.yazwear.YazwearApplication
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var loginButton: Button
    private lateinit var registerLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val repository = (application as YazwearApplication).repository

        initializeViews()
        setupClickListeners()

        // Check if a user is already logged in
        lifecycleScope.launch {
            if (repository.getLoggedInUser() != null) {
                navigateToMain()
            }
        }
    }

    private fun initializeViews() {
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        loginButton = findViewById(R.id.loginButton)
        registerLink = findViewById(R.id.registerLink)
    }

    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            attemptLogin()
        }

        registerLink.setOnClickListener {
            navigateToRegister()
        }
    }

    private fun attemptLogin() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (validateInputs(email, password)) {
            performLogin(email, password)
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailEditText.error = "L'email est requis"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.error = "Format d'email invalide"
            return false
        }

        if (password.isEmpty()) {
            passwordEditText.error = "Le mot de passe est requis"
            return false
        }

        if (password.length < 6) {
            passwordEditText.error = "Le mot de passe doit contenir au moins 6 caractères"
            return false
        }

        return true
    }

    private fun performLogin(email: String, password: String) {
        Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show()
        navigateToMain()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
