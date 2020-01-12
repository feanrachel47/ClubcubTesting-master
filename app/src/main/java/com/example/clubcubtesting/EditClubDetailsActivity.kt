package com.example.clubcubtesting

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import android.widget.ArrayAdapter



class EditClubDetailsActivity: AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance()
        .reference.child("clubs")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_club_details)
        setTitle("Edit Club Details")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)//maybe can delete

        var viewClub: ClubModel
        val intent = intent
        val clubID = intent.getStringExtra(ClubMainActivity.CLUBID)
        val editClubName = findViewById<EditText>(R.id.tvEditClubName)
        val spinner: Spinner = findViewById(R.id.spinnerEditCat)
        val editClubDesc = findViewById<EditText>(R.id.tvEditClubDesc)
        val saveButton = findViewById<Button>(R.id.btnSaveChanges)
        val cancelButton = findViewById<Button>(R.id.buttonCancelEdit)


        //For spinner
        //spinner.onItemSelectedListener = this
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



        cancelButton.setOnClickListener{
            finish()
        }





        //Retrieve into edit text and spinner
        databaseRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0!!.exists()){
                    for (c in p0.children){

                        val clubs = c.getValue(ClubModel::class.java)!!

                        if(clubs.clubId == clubID){
                            viewClub = clubs

                            editClubName.setText(viewClub.clubName)
                            editClubDesc.setText(viewClub.clubDesc)
                            spinner.setSelection(viewClub.clubCategory)
                            val currPresident = viewClub.clubPresident

                            saveButton.setOnClickListener {
                                val newClubName = editClubName.text.toString()
                                val newClubDescription = editClubDesc.text.toString()
                                val newCategory = spinner.selectedItemPosition
                                updateClubDetails(
                                    clubID, newClubName, newCategory,
                                    newClubDescription, currPresident
                                )
                            }

                        }
                    }
                }

            }


        })




    }




    private fun updateClubDetails(clubID:String, newClubName:String,
                                  newCategory:Int, newClubDesc:String, currPresident:String){
        val updatedClubDetails = ClubModel(clubID, newClubName,newCategory,newClubDesc,currPresident)
        val clubIdKey = clubID.toString()

        val editClubDesc = findViewById<EditText>(R.id.tvEditClubDesc)
        val editClubName = findViewById<EditText>(R.id.tvEditClubName)

        if(TextUtils.isEmpty(editClubDesc.text)){

            editClubDesc.error = "Required field."
            return
        }

        if(TextUtils.isEmpty(editClubName.text)){

            editClubName.error = "Required field."
            return
        }

        databaseRef.child(clubIdKey).setValue(updatedClubDetails).addOnCompleteListener{
            Toast.makeText(
                applicationContext,
                "Club details updated successfully!",
                Toast.LENGTH_SHORT).show()

            //onResume()

        }

        finish()



    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}