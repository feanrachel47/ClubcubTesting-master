package com.example.clubcubtesting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        supportActionBar!!.title = "Edit Profile"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val nickname = findViewById<EditText>(R.id.edit_nickname)
        val description = findViewById<EditText>(R.id.edit_description)
        val btn_updateProfile = findViewById<Button>(R.id.updateProfile_button)

        var intent = intent
        val intent_nickname = intent.getStringExtra("Nickname")
        val intent_description = intent.getStringExtra("Description")
        val intent_email = intent.getStringExtra("Email")

        nickname.setText(intent_nickname)
        description.setText(intent_description)

        btn_updateProfile.setOnClickListener{
            val newNickname = edit_nickname.text.toString()
            val newDescription = edit_description.text.toString()
            updateProfile(newNickname, intent_email ,newDescription)
        }

    }

    private fun updateProfile(nickname: String, email: String ,description: String) {


        database = FirebaseDatabase.getInstance().getReference("users")

        val updateUser = User(nickname, email ,description)

        database.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(updateUser).addOnCompleteListener {
            Toast.makeText(
                this,
                "Success to update user profile",
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, HomepageActivity::class.java)
            startActivity(intent)
        }
    }
}
