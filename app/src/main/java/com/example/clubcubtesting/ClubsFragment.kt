package com.example.clubcubtesting


import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.club_card_view.*
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class ClubsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        activity?.setTitle("Clubs")

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_clubs, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.clubRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)

        val createClubButtonCreate = view.findViewById<FloatingActionButton>(R.id.createClubButton)

        createClubButtonCreate.setOnClickListener{
            goToCreateClub()
        }

        //create an array list
        val dataList = ArrayList<ClubModel>()
        val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("clubs")

        //display recycle view
        databaseRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0!!.exists()){
                    for(c in p0.children){
                        val clubs = c.getValue(ClubModel::class.java)
                        dataList.add(clubs!!)

                    }
                }

                //pass to rvAdapter
                val rvAdapter = RvAdapter(dataList)

                //set the recyclerView to the adapter
                recyclerView?.adapter = rvAdapter
            }

            override fun onCancelled(p0: DatabaseError){
                Log.d("ClubsFragment", "Cancelled")
            }
        })

        return view


    }

    private fun goToCreateClub(){
        val intent = Intent(view?.context, CreateClubActivity::class.java)
        startActivity(intent)
    }



}
