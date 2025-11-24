package com.example.sclad_salo.models

import java.io.Serializable


data class Sclad_operator(
    var uid: String,
    val name_surname: String,
    val email: String,
    val password:String,
    var operators_code:Int,
    var added_product:Int,
    var promoted_product:Int,
    var role:String

): Serializable

{
    constructor() :this ("","","","",
        0,0,0,"")

}






