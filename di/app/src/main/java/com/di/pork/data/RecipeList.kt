package com.di.pork.data

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class RecipeList(
    @SerializedName("recipelist")
    val recipeList: ArrayList<Recipe>
)

@Parcelize
data class Recipe(
    @SerializedName("id") @Expose
    val id:String,
    @SerializedName("nickname") @Expose
    val nickname:String,
    @SerializedName("title") @Expose
    val title:String,
    @SerializedName("content") @Expose
    val content:String,
    @SerializedName("date") @Expose
    var date:String
) : Parcelable
