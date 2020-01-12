package com.example.clubcubtesting

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ClubMainActivity : AppCompatActivity() {

    var isPresident:Boolean = false
    var isMember:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.club_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val intent = intent
        val name = intent.getStringExtra("clubName")
        supportActionBar!!.title = name.toString()


        val clubID = intent.getStringExtra(RvAdapter.CLUBID)
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance()
            .reference.child("clubs")

        val tvClubPresident = findViewById<TextView>(R.id.textViewClubPresident)
        val tvClubDesc = findViewById<TextView>(R.id.textViewLabelClubDescription)
        val tvClubMember = findViewById<TextView>(R.id.textViewNumMembers)

        val currentuser: String = FirebaseAuth.getInstance().currentUser!!.uid
        val buttonJoin = findViewById<Button>(R.id.buttonJoinClub)
        val buttonEditClub = findViewById<FloatingActionButton>(R.id.floatingActionButtonEditClubDetails)

        var viewClub: ClubModel



        databaseRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            @SuppressLint("ResourceAsColor")
            override fun onDataChange(p0: DataSnapshot) {

                if(p0!!.exists()){
                    for (c in p0.children){

                        val clubs = c.getValue(ClubModel::class.java)!!

                        if(clubs.clubId == clubID){
                            viewClub = clubs

                            val presidentID = viewClub.clubPresident

                            retrievePresidentName(presidentID)
                            tvClubDesc.text = viewClub.clubDesc
                            tvClubMember.text = viewClub.clubMember.toString()

                            if(currentuser == viewClub.clubPresident){
//                                isPresident = true
                                buttonJoin.text = "JOINED"
                                buttonJoin.isEnabled=false
                                //buttonJoin.setBackgroundColor(R.color.greyColor)
                                buttonEditClub.visibility = View.VISIBLE

                            }else{
                                retrieveUserData()
                            }


                        }
                    }
                }

            }


        })

        //onclickListener for edit club details
        buttonEditClub.setOnClickListener{
            val intent = Intent(this, EditClubDetailsActivity::class.java)
            intent.putExtra(CLUBID, clubID)
            startActivity(intent)
        }



    }

    private fun retrieveUserData(){


        val currentuser: String = FirebaseAuth.getInstance().currentUser!!.uid
        val clubID = intent.getStringExtra(RvAdapter.CLUBID)

        val userDatabaseRef = FirebaseDatabase.getInstance()
            .reference.child("users").child(currentuser)

        val userdbRefhasJoin = FirebaseDatabase.getInstance()
            .reference.child("users").child(currentuser).child("joinedClub")


        userDatabaseRef.addValueEventListener(object: ValueEventListener{
            val buttonJoin = findViewById<Button>(R.id.buttonJoinClub)

            override fun onCancelled(p0: DatabaseError) {

            }

            @SuppressLint("ResourceAsColor")
            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.hasChild("joinedClub")){//never join any club, then not member
                    buttonJoin.text = "JOIN"

                    buttonJoin.setOnClickListener{

                        //member will + 1
                        addMemberNum()

                        userDatabaseRef.child("joinedClub").push().setValue(clubID).addOnCompleteListener{

                            Toast.makeText(
                                applicationContext,
                                "Successfully join club!",
                                Toast.LENGTH_SHORT).show()



                        }


                    }

                }else if(p0.hasChild("joinedClub")){
                    //got join=joined some club
                    //check user.join == clubId

                    goHere()


                }


            }
        })




    }

    private fun goHere(){
        val currentuser: String = FirebaseAuth.getInstance().currentUser!!.uid
        val clubID = intent.getStringExtra(RvAdapter.CLUBID)

        val userdbRefhasJoin = FirebaseDatabase.getInstance()
            .reference.child("users").child(currentuser).child("joinedClub")

        val buttonJoin = findViewById<Button>(R.id.buttonJoinClub)

        userdbRefhasJoin.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            @SuppressLint("ResourceAsColor")
            override fun onDataChange(p0: DataSnapshot) {

                for(user in p0.children){

                    if(user.value.toString() == clubID.toString()){
                        buttonJoin.text = "JOINED"
                        buttonJoin.isEnabled = false
                        buttonJoin.setBackgroundColor(resources.getColor(R.color.buttonclicked))
                        break
                    }else if(user.value.toString() != clubID.toString()){
                        buttonJoin.text = "JOIN"
                        buttonJoin.setOnClickListener{
                            //club member number +1
                            addMemberNum()


                            userdbRefhasJoin.push().setValue(clubID).addOnCompleteListener{

                                Toast.makeText(
                                    applicationContext,
                                    "Successfully join club!",
                                    Toast.LENGTH_SHORT).show()
                                buttonJoin.text = "JOINED"
                                buttonJoin.isEnabled = false
                                buttonJoin.setBackgroundColor(resources.getColor(R.color.buttonclicked))

                            }
                        }
                    }

                }



            }


        } )




    }

    private fun addMemberNum(){

        //clubID
        //change the num of membernum

        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance()
            .reference.child("clubs")

        //val intent = intent
        val clubID = intent.getStringExtra(RvAdapter.CLUBID)
        val tvClubMember = findViewById<TextView>(R.id.textViewNumMembers)
        var updatedTotalMember:Int = tvClubMember.text.toString().toInt()
        updatedTotalMember++


        databaseRef.child(clubID).child("clubMember").setValue(updatedTotalMember).addOnCompleteListener{
            Toast.makeText(
                applicationContext,
                "Welcome New Member!",
                Toast.LENGTH_SHORT).show()
        }



    }

    private fun retrievePresidentName(presidentID:String){
        val userdbRefhasJoin = FirebaseDatabase.getInstance()
            .reference.child("users")

        val tvClubPresident = findViewById<TextView>(R.id.textViewClubPresident)

        userdbRefhasJoin.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(u in p0.children){

                    val users = u.getValue(User::class.java)!!

                    if(u.key == presidentID ){
                        val presidentName = users.nickname
                        tvClubPresident.text = presidentName



                }
                }
            }


        })



    }



    companion object{
        const val CLUBID = "com.example.clubcubtesting.CLUBID"
    }

}
