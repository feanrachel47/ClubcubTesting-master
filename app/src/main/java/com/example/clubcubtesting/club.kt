package com.example.clubcubtesting

class club {
    private lateinit var clubname:String
    private var img_path: Int = 0

     constructor(clubname: String, img_path: Int) {
         this.setPath(img_path)
         this.setClubname(clubname)
     }

    fun getClubname() :String {
        return clubname
    }

    fun setClubname(clubname: String){
        this.clubname = clubname
    }

    fun getPath(): Int {
        return img_path
    }

    fun setPath(path:Int) {
        img_path = path
    }
}