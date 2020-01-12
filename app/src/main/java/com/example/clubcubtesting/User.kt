package com.example.clubcubtesting

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User (
    var nickname: String,
    var email: String,
    var description: String? = "Hello! Nice to meet you!"
){
    constructor(): this("","","Hello! Nice to meet you!")
}
