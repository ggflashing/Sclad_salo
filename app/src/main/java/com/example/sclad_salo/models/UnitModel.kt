package com.example.sclad_salo.models

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable



@SuppressLint("ParcelCreator")
data class UnitModel(
    val name: String,
    var price:Double,
    var count:Int,
    var comment: String,
    val image: String


): Parcelable

{
    constructor() :this("",0.0,0,"","")

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        TODO("Not yet implemented")
    }

}
