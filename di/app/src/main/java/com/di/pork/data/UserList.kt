package com.di.pork.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import okhttp3.MultipartBody

data class UserList(
    @SerializedName("userlist") @Expose
    val userList: List<User>,
    @SerializedName("userres") @Expose
    val userResList: List<UserRes>,
    @SerializedName("result") @Expose
    val result:Boolean,
)

data class User(
    @SerializedName("id") @Expose
    val id:String,
    @SerializedName("pass") @Expose
    val pass:String,
    @SerializedName("image") @Expose
    val image:MultipartBody.Part?,
    @SerializedName("nickname") @Expose
    val nickname:String
)

data class UserRes(
    @SerializedName("id") @Expose
    val id:String,
    @SerializedName("image") @Expose
    val image:String,
    @SerializedName("nickname") @Expose
    val nickname:String,
    @SerializedName("result") @Expose
    val result:Boolean,
)
