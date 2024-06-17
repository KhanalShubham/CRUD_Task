package com.example.task_for_crud.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.task_for_crud.databinding.ActivityLoginPageBinding
import com.google.firebase.auth.FirebaseAuth

class LoginPageActivity : AppCompatActivity() {
    lateinit var loginPageBinding: ActivityLoginPageBinding
    var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        loginPageBinding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(loginPageBinding.root)

    loginPageBinding.button.setOnClickListener {
        val email = loginPageBinding.editTextText.text.toString()
        val password = loginPageBinding.editTextTextPassword.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginPageActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(applicationContext, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    loginPageBinding.signup.setOnClickListener {
        val intent = Intent(this@LoginPageActivity, RegisterPageActivity::class.java)
        startActivity(intent)
        finish()
    }
}



}