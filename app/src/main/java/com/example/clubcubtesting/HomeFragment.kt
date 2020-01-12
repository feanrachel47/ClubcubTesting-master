package com.example.clubcubtesting


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */

class HomeFragment : Fragment() {

    lateinit var adapter: clubAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.clubRecycleView)
        recyclerView?.layoutManager= LinearLayoutManager(view.context, LinearLayoutManager.HORIZONTAL,false)


        activity?.setTitle("Homepage")

        // pointing to unique user id in users database
        val cUser = FirebaseAuth.getInstance().currentUser!!.uid
        val databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(cUser)

        // find text view
        val nickname_tv = view?.findViewById(R.id.home_nickname_textView) as TextView
        val description_tv = view?.findViewById(R.id.home_profileDescription_textView) as TextView

        // display nickname and description in home fragment
        databaseReference.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(view.context, p0.message, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                var nickname = p0.child("nickname").value.toString()
                var description = p0.child("description").value.toString()


                nickname_tv.text = nickname
                description_tv.text = description
            }

        })




        //for those who don't join any club
        val userDatabaseRef = FirebaseDatabase.getInstance()
            .reference.child("users").child(cUser.toString())
        val titleJoinedClub = view.findViewById<TextView>(R.id.joinedClubs_textView)


        //read data
        userDatabaseRef.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot) {

                for(j in p0.children){
                if (!p0.hasChild("joinedClub")) {
                    titleJoinedClub.text = "Currently No Joined Club"
                    break
                }
                 else {

                    val userdbRefhasJoin = FirebaseDatabase.getInstance()
                        .reference.child("users").child(cUser.toString()).child("joinedClub")

                    userdbRefhasJoin.addValueEventListener(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            for (user in p0.children) {

                                val joinedClubID: String = user.value.toString()

                                //search joined club
                                val dataList = ArrayList<ClubModel>()
                                val databaseRef: DatabaseReference =
                                    FirebaseDatabase.getInstance().reference.child("clubs")


                                databaseRef.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        if (p0!!.exists()) {
                                            for (c in p0.children) {


                                                val clubs = c.getValue(ClubModel::class.java)
                                                if (clubs?.clubId == joinedClubID) {
                                                    dataList.add(clubs!!)

                                                }


                                            }
                                        }

                                        //pass to clubAdapter
                                        val clubAdapter = clubAdapter(dataList)

                                        //set the recyclerView to the adapter
                                        recyclerView?.adapter = clubAdapter
                                    }

                                    override fun onCancelled(p0: DatabaseError) {
                                        Log.d("HomeFragment", "Cancelled")
                                    }
                                })


                            }
                        }

                    })

                }
            }
            }


        })

















        //display all de, will change






        return view

    }


}
