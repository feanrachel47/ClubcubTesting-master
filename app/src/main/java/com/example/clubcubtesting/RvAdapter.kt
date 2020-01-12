package com.example.clubcubtesting

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.club_card_view.view.*


class RvAdapter(val clubList: ArrayList<ClubModel>):
    RecyclerView.Adapter<RvAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.club_card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvAdapter.ViewHolder, position:Int) {
        holder.bindItems(clubList[position])
        val clubName = clubList[position].clubName
        val clubId = clubList[position].clubId
        //val clubCatPosition = clubList[position].clubCategory

        when (clubList[position].clubCategory) {
            0 -> holder.imgViewClubCat.setImageResource(R.drawable.ic_arts_white_24dp)
            1 -> holder.imgViewClubCat.setImageResource(R.drawable.ic_school_white_24dp)
            2 -> holder.imgViewClubCat.setImageResource(R.drawable.ic_environmental_24dp)
            3 -> holder.imgViewClubCat.setImageResource(R.drawable.ic_videogame_white_24dp)
            4 -> holder.imgViewClubCat.setImageResource(R.drawable.ic_sports_white_24dp)
            5 -> holder.imgViewClubCat.setImageResource(R.drawable.ic_others_white_24dp)
        }



        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, ClubMainActivity::class.java)
            intent.putExtra("clubName",clubName)
            intent.putExtra(CLUBID, clubId)
            holder.itemView.context.startActivity(intent)


        }


    }

    override fun getItemCount(): Int {
        return clubList.size
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){


        val imgViewClubCat:ImageView = itemView.findViewById(R.id.clubIcon)

        fun bindItems(clubs:ClubModel) {
            val viewClubName = itemView.findViewById<TextView>(R.id.textViewClubName)
            viewClubName.text = clubs.clubName



        }

    }

    companion object{
        const val CLUBID = "com.example.clubcubtesting.CLUBID"
    }
}



