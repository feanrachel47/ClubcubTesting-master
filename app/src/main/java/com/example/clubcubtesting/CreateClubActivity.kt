package com.example.clubcubtesting

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CreateClubActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_club)
        setTitle("Create Your Club")

        val etClubName = findViewById<EditText>(R.id.editTextCreateClubName)
        val etClubDesc = findViewById<EditText>(R.id.editTextCreateClubDesc)
        val btnCreate = findViewById<Button>(R.id.buttonConfirmCreateClub)
        val btnCancel = findViewById<Button>(R.id.buttonCreateCancel)
        val spinner: Spinner = findViewById(R.id.spinner)


        //For spinner
        spinner.onItemSelectedListener = this
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.club_category_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }


        //Save data to firebase
            btnCreate.setOnClickListener{
            saveClubToDatabase(etClubName,etClubDesc,spinner)
        }

        btnCancel.setOnClickListener{
            goToClubFragment()
        }







    }

    private fun saveClubToDatabase(etClubName:EditText, etClubDesc:EditText, spinner: Spinner){
         //Value to be saved in database
        val clubName = etClubName.text.toString()
        val clubDesc = etClubDesc.text.toString()
        var selectedCategory:Int = spinner.selectedItemPosition
        val clubMember: Int = 1 //president consider as 1 member

        if(TextUtils.isEmpty(etClubName.text)){

            etClubName.error = "Required field."
            return
        }

        if(TextUtils.isEmpty(etClubDesc.text)){
            etClubDesc.error = "Required field."
            return
        }

        //database for clubs
        val clubdbRef = FirebaseDatabase.getInstance().getReference("clubs")
        val clubId = clubdbRef.push().key.toString()



        //get current user id
        val currentuser = FirebaseAuth.getInstance().currentUser!!.uid
        //database for users
        val userdbRef = FirebaseDatabase.getInstance().getReference("users").child(currentuser)
        //val nameClubJoined = "join"


        val createClub=ClubModel(clubId, clubName, selectedCategory,clubDesc,
            FirebaseAuth.getInstance().currentUser!!.uid,clubMember)

        clubdbRef.child(clubId).setValue(createClub).addOnCompleteListener{
            Toast.makeText(
                applicationContext,
                "Club created successfully!",
                Toast.LENGTH_SHORT).show()

        }

        //check inside join have how many data

        userdbRef.child("joinedClub").push().setValue(clubId).addOnCompleteListener{
            Toast.makeText(
                applicationContext,
                "Successfully joined club!",
                Toast.LENGTH_SHORT).show()
        }




        goToClubFragment()
    }

    private fun goToClubFragment(){
        val intent = Intent(this, ClubsFragment::class.java)
        startActivity(intent)
    }


    override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}