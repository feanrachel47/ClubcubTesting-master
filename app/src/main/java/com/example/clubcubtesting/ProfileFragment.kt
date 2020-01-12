package com.example.clubcubtesting


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        activity?.setTitle("Your Profile")

        val btnEditProfile = view.findViewById(R.id.editProfile_button) as Button
        val btnChgPw = view.findViewById(R.id.changePassword_button) as Button
        val btnLogout = view.findViewById(R.id.logout_button) as Button

        // pointing to unique user id in users database
        val cUser = FirebaseAuth.getInstance().currentUser
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(cUser!!.uid)

        // find the textview
        val nickname_tv = view?.findViewById(R.id.home_nickname_textView) as TextView
        val email_tv = view?.findViewById(R.id.emailaddress_textView) as TextView
        val description_tv = view?.findViewById(R.id.profile_description_textView) as TextView


        // display nickname, email, and profile description at profile fragment
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(view.context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                var nickname = p0.child("nickname").value.toString()
                var email = p0.child("email").value.toString()
                var description = p0.child("description").value.toString()

                nickname_tv.text = nickname
                email_tv.text = email
                description_tv.text = description

                btnEditProfile.setOnClickListener{
                    // start an intent with put extra
                    val intent = Intent(view.context, EditProfileActivity::class.java).apply {
                        putExtra("Nickname", nickname)
                        putExtra("Description",description)
                        putExtra("Email", email)
                    }
                    startActivity(intent)
                }

                btnChgPw.setOnClickListener {
                    changePw()
                }

                btnLogout.setOnClickListener{
                    logOut()
                }
            }

        })



        return view
    }

    private fun changePw() {
        val intent = Intent(view?.context, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    private fun logOut() {
        FirebaseAuth.getInstance().signOut()
        activity?.finish()
        Toast.makeText(activity, "Log out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(view?.context, LoginActivity::class.java)
        startActivity(intent)
    }


}
