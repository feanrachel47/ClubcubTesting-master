package com.example.clubcubtesting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar!!.title = "Sign Up New Account"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        register_button_register.setOnClickListener{
            performRegister()
        }

        already_have_account_textView.setOnClickListener{
            // back to login activity
            finish()
        }
    }

    private fun performRegister() {
        auth = FirebaseAuth.getInstance()

        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Firebase authentication to create user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if(it.isSuccessful) {
                val user = auth.currentUser

                user?.sendEmailVerification()?.addOnCompleteListener{
                    if(it.isSuccessful) {
                        Log.d("RegisterActivity", "Email Sent.")
                    }
                }

                Toast.makeText(
                    this,
                    "Register Successfully, please verify your email address.",
                    Toast.LENGTH_SHORT
                ).show()

                createProfile()
            }
            else {
                Toast.makeText(
                    this,
                    "Failed to create user",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun createProfile() {
        val nickname = nickname_edittext_register.text.toString()
        val email = email_edittext_register.text.toString()
        val description = "Hello! Nice to meet you!"

        val user = User(nickname, email, description)
        database = FirebaseDatabase.getInstance().getReference("users")

        database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).addOnCompleteListener {
            Toast.makeText(
                this,
                "Success to create user profile",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
