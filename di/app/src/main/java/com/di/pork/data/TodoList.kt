package com.di.pork.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TodoList(
    @SerializedName("todolist")
    val todolist: ArrayList<Todo>
)

data class Todo(
    @SerializedName("id") @Expose
    val id:String,
    @SerializedName("nickname") @Expose
    val nickname:String,
    @SerializedName("content") @Expose
    val content:String,
    @SerializedName("date") @Expose
    var date:String,
    @SerializedName("isdone") @Expose
    val isdone:Int,
)
