package com.example.yazwear.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.yazwear.R
import com.example.yazwear.YazwearApplication
import com.example.yazwear.data.UserEntity
import com.example.yazwear.service.InactivityService
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var fullNameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var passwordEditText: TextInputEditText
    private lateinit var registerButton: Button
    private lateinit var loginLink: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val stopIntent = Intent(this, InactivityService::class.java)
        stopIntent.action = InactivityService.ACTION_STOP_TIMER
        startService(stopIntent)

        initializeViews()
        setupClickListeners()
    }

    private fun initializeViews() {
        fullNameEditText = findViewById(R.id.fullNameEditText)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        registerButton = findViewById(R.id.registerButton)
        loginLink = findViewById(R.id.loginLink)
    }

    private fun setupClickListeners() {
        registerButton.setOnClickListener {
            attemptRegistration()
        }

        loginLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun attemptRegistration() {
        val fullName = fullNameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()

        if (validateInputs(fullName, email, password)) {
            performRegistration(fullName, email, password)
        }
    }

    private fun validateInputs(fullName: String, email: String, password: String): Boolean {
        if (fullName.isEmpty()) {
            fullNameEditText.error = "Le nom complet est requis"
            return false
        }

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

    private fun performRegistration(fullName: String, email: String, password: String) {
        val repository = (application as YazwearApplication).repository

        lifecycleScope.launch {
            val user = UserEntity(fullName = fullName, email = email)
            repository.insertUser(user)

            Toast.makeText(this@RegisterActivity, "Inscription réussie!", Toast.LENGTH_SHORT).show()
            navigateToLoginWithEmail(email)
        }
    }

    private fun navigateToLoginWithEmail(email: String) {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
