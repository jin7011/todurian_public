package com.di.pork.model

import com.di.pork.data.*
import okhttp3.MultipartBody
import javax.inject.Inject

class Repository @Inject constructor(private val service: Service){

    val URL = "http://ec2-15-165-159-175.ap-northeast-2.compute.amazonaws.com:3001"

    suspend fun getTodoList(id:String):TodoList{
        return service.getTodoList(id)
    }

    suspend fun isExist(id:String):Boolean{
        return service.isExist(id)
    }

    suspend fun signin(user: User):Boolean{
        return service.signin(user.id,user.pass,user.nickname,user.image)
    }

    suspend fun updateImage(id: String,image: MultipartBody.Part?):Boolean{
        return service.updateImage(id,image)
    }

    suspend fun login(id: String,pass:String):Family{
        return service.login(id,pass)
    }

    suspend fun updateInvite(id: String, invite:Boolean):Boolean{
        return service.updateInvite(id,invite)
    }

    suspend fun getImage(id:String):String{
        return URL+"/"+service.getImage(id)
    }

    suspend fun getFamily(id:String):FamilyList{
        return service.getFamily(id)
    }

    suspend fun addFamily(id: String,nickname:String,image:String,family:String):Boolean{
        return service.addFamily(id,nickname, image, family)
    }

    suspend fun delFamily(id: String,family: String):Boolean{
        return service.delFamily(id,family)
    }

    suspend fun addTodo(myId: String, myNickname: String, content: String):Boolean {
        return service.addTodo(myId,myNickname,content)
    }

    suspend fun delTodo(id:String,content: String): Boolean{
        return service.delTodo(id,content)
    }

    suspend fun doTodo(id: String,content: String): Boolean{
        return service.doTodo(id,content)
    }

    suspend fun getRecipe(id: String): RecipeList{
        return service.getRecipe(id)
    }

    suspend fun addRecipe(id: String,nickname: String ,title: String, content: String): Boolean{
        return service.addRecipe(id,nickname, title, content)
    }

    suspend fun delRecipe(id: String,title: String, content: String): Boolean{
        return service.delRecipe(id, title, content)
    }

    suspend fun getUser(id: String): Family {
        return service.getUser(id)
    }

    suspend fun updateNick(id: String, newNick: String): Boolean {
        return service.updateNick(id,newNick)
    }

    suspend fun withdraw(id: String): Boolean {
        return service.withdraw(id)
    }
}