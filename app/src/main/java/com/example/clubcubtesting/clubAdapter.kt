package com.example.clubcubtesting

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.row_layout.view.*

class clubAdapter(val joinedClubList: ArrayList<ClubModel>):
        RecyclerView.Adapter<clubAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): clubAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: clubAdapter.ViewHolder, position:Int) {
        holder.bindItems(joinedClubList[position])
        val clubName = joinedClubList[position].clubName
        val clubId = joinedClubList[position].clubId


        //imgViewClubCat
        when (joinedClubList[position].clubCategory) {
            0 -> holder.joinedClubImage.setImageResource(R.drawable.ic_arts_white_24dp)
            1 -> holder.joinedClubImage.setImageResource(R.drawable.ic_school_white_24dp)
            2 -> holder.joinedClubImage.setImageResource(R.drawable.ic_environmental_24dp)
            3 -> holder.joinedClubImage.setImageResource(R.drawable.ic_videogame_white_24dp)
            4 -> holder.joinedClubImage.setImageResource(R.drawable.ic_sports_white_24dp)
            5 -> holder.joinedClubImage.setImageResource(R.drawable.ic_others_white_24dp)
        }



        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ClubMainActivity::class.java)
            intent.putExtra("clubName",clubName)
            intent.putExtra(CLUBID, clubId)
            holder.itemView.context.startActivity(intent)


        }


    }

    override fun getItemCount(): Int {
        return joinedClubList.size
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){


        val joinedClubImage:ImageView = itemView.findViewById(R.id.clubImageView)

        fun bindItems(clubs:ClubModel) {
            val viewClubName = itemView.findViewById<TextView>(R.id.clubName_textView)
            viewClubName.text = clubs.clubName



        }

    }


    companion object{
        const val CLUBID = "com.example.clubcubtesting.CLUBID"
    }
}