package com.example.clubcubtesting

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.se.omapi.Session
import android.util.Log
import android.util.Patterns
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*
import java.util.regex.Pattern
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class LoginActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        login_button_login.setOnClickListener{
            performLogin()
        }

        forget_password_button.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")
            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val userEmail = view.findViewById<EditText>(R.id.et_userEmail)

            builder.setView(view)
            builder.setPositiveButton("Reset", DialogInterface.OnClickListener { dialog, which ->
                forgotPassword(userEmail)
            })

            builder.setNegativeButton("Close", DialogInterface.OnClickListener { dialog, which ->  })
            builder.show()
        }

        back_to_registration_textView.setOnClickListener{
            // go to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

//        val auth = FirebaseAuth.getInstance()
//        // when user is signed in
//        if (auth.currentUser != null) {
//            val intent = Intent(this, HomepageActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
    }

    private fun performLogin() {
        val email = email_edittext_login.text.toString()
        val password = password_edittext_login.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }
        else {

            Log.d("Login", "Attempt login with email/pw $email/***")

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        Log.d(
                            "LoginActivity",
                            "Login successfully with uid: ${it.result?.user?.uid}"
                        )

                        val user = FirebaseAuth.getInstance().currentUser
                        updateUI(user)


                    } else {
                        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }
                }
        }
    }


    private fun updateUI(currentUser: FirebaseUser?) {
        if(currentUser != null) {
            if(currentUser.isEmailVerified){
                // start an intent (homepage) without put extra
                val intent = Intent(this, HomepageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Please verify your email address", Toast.LENGTH_SHORT).show()
            }

        }
        else {
            Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun forgotPassword(userEmail:EditText) {

        // validate user email
        if(userEmail.text.toString().isEmpty()) {
            return
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(userEmail.text.toString()).matches()) {
            return
        }

        FirebaseAuth.getInstance().sendPasswordResetEmail(userEmail.text.toString())
            .addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "Email is sent to reset password.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}