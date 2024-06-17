package com.example.task_for_crud.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.task_for_crud.R
import com.example.task_for_crud.databinding.ActivityRegisterPageBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterPageActivity : AppCompatActivity() {
    private lateinit var registerPageBinding: ActivityRegisterPageBinding
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        registerPageBinding = ActivityRegisterPageBinding.inflate(layoutInflater)
        setContentView(registerPageBinding.root)

        registerPageBinding.button2.setOnClickListener {
            val email = registerPageBinding.editTextText.text.toString()
            val password = registerPageBinding.Password.text.toString()
            val cpassword = registerPageBinding.CPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && cpassword.isNotEmpty() ) {
                if (password == cpassword) {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "User Registered Successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@RegisterPageActivity, LoginPageActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(applicationContext, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        registerPageBinding.textView7.setOnClickListener {
            val intent = Intent(this@RegisterPageActivity, LoginPageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}