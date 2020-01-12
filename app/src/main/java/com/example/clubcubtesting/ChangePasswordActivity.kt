package com.example.clubcubtesting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_change_password.*

class ChangePasswordActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = FirebaseAuth.getInstance()
        supportActionBar!!.title = "Change Password"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        changePw_button.setOnClickListener {
            changePassword()
        }
    }

    private fun changePassword() {

        if(TextUtils.isEmpty(oldpw_editText.text)){
            oldpw_editText.setError("Old Password Required")
            return
        }

        if(TextUtils.isEmpty(newpw_editText.text)){
            newpw_editText.setError("New Password Required")
            return
        }

        if(TextUtils.isEmpty(confirmpw_editText.text)){
            confirmpw_editText.setError("Confirm Password Required")
            return
        }
            if(newpw_editText.text.toString().equals(confirmpw_editText.text.toString())) {
                val user = auth.currentUser
                if(user != null && user.email != null) {
                    val credential = EmailAuthProvider
                        .getCredential(user.email!!, oldpw_editText.text.toString())

                    // Prompt the user to re-provide their sign-in credentials
                    user?.reauthenticate(credential)
                        ?.addOnCompleteListener {
                            if(it.isSuccessful) {
                                Toast.makeText(this, "Re-Authentication success", Toast.LENGTH_SHORT)

                                user?.updatePassword(newpw_editText.text.toString())
                                    ?.addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT)
                                            auth.signOut()
                                            startActivity(Intent(this, LoginActivity::class.java))
                                            finish()
                                        }
                                    }
                            }else {
                                Toast.makeText(this, "Re-Authentication failed", Toast.LENGTH_SHORT)
                            }
                        }
                }else {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }else {
                Toast.makeText(this, "Password mismatching", Toast.LENGTH_SHORT)
            }

    }
}
