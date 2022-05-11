package com.di.pork.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class FamilyList(
    @SerializedName("family") @Expose
    val family: ArrayList<Family>,
    @SerializedName("result") @Expose
    val result:Boolean,
)

@Parcelize
data class Family(
    @SerializedName("family") @Expose
    val id:String,
    @SerializedName("familyimage") @Expose
    val image:String,
    @SerializedName("familynickname") @Expose
    val nickname:String,
    @SerializedName("invite") @Expose
    val invite:Boolean,
    @SerializedName("result") @Expose
    val result:Boolean,
) : Parcelable
