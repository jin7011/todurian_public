package com.di.pork.model

import com.di.pork.data.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface Service {
    @FormUrlEncoded
    @POST("/todo/all")
    suspend fun getTodoList(
        @Field("id") id:String
    ):TodoList

    @FormUrlEncoded
    @POST("/user/check")
    suspend fun isExist(
        @Field("id") id:String
    ):Boolean

    @Multipart
    @POST("/user/signin")
    suspend fun signin(
        @Part("id") id:String,
        @Part("pass") pass:String,
        @Part("nickname") nickname:String,
        @Part image:MultipartBody.Part?
    ):Boolean

    @Multipart
    @POST("/user/image/update")
    suspend fun updateImage(
        @Part("id") id:String,
        @Part image:MultipartBody.Part?
    ):Boolean

    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(
        @Field("id") id:String,
        @Field("pass") pass:String
    ):Family

    @FormUrlEncoded
    @POST("/user/image")
    suspend fun getImage(
        @Field("id") id:String,
    ):String

    @FormUrlEncoded
    @POST("/family")
    suspend fun getFamily(
        @Field("id") id:String,
    ):FamilyList

    @FormUrlEncoded
    @POST("/family/add")
    suspend fun addFamily(
        @Field("id") id:String,
        @Field("nickname") nickname:String,
        @Field("image") image:String,
        @Field("family") family:String,
    ):Boolean

    @FormUrlEncoded
    @POST("/family/del")
    suspend fun delFamily(
        @Field("id") id:String,
        @Field("family") family:String,
    ):Boolean

    @FormUrlEncoded
    @POST("/todo/add")
    suspend fun addTodo(
        @Field("id") id:String,
        @Field("nickname") nickname:String,
        @Field("content") content:String
    ):Boolean

    @FormUrlEncoded
    @POST("/todo/del")
    suspend fun delTodo(
        @Field("id") id:String,
        @Field("content") content:String
    ):Boolean

    @FormUrlEncoded
    @POST("/todo/do")
    suspend fun doTodo(
        @Field("id") id:String,
        @Field("content") content:String
    ):Boolean

    @FormUrlEncoded
    @POST("/recipe/add")
    suspend fun addRecipe(
        @Field("id") id:String,
        @Field("nickname") nickname: String,
        @Field("title") title : String,
        @Field("content") content:String
    ):Boolean

    @FormUrlEncoded
    @POST("/recipe/del")
    suspend fun delRecipe(
        @Field("id") id:String,
        @Field("title") title : String,
        @Field("content") content:String
    ):Boolean

    @FormUrlEncoded
    @POST("/recipe/all")
    suspend fun getRecipe(
        @Field("id") id:String
    ):RecipeList

    @FormUrlEncoded
    @POST("/user/invite/update")
    suspend fun updateInvite(
        @Field("id") id:String,
        @Field("invite") invite:Boolean
    ):Boolean

    @FormUrlEncoded
    @POST("/user/get")
    suspend fun getUser(
        @Field("id") id:String
    ):Family

    @FormUrlEncoded
    @POST("/user/nickname/update")
    suspend fun updateNick(
        @Field("id") id:String,
        @Field("nickname") nickname: String
    ):Boolean

    @FormUrlEncoded
    @POST("/user/withdraw")
    suspend fun withdraw(
        @Field("id") id:String,
    ):Boolean


    companion object{
        private const val URL = "http://ec2-15-165-159-175.ap-northeast-2.compute.amazonaws.com:3001"

        fun create(): Service {
            val httpClient = OkHttpClient.Builder()
                .callTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

            return Retrofit.Builder()
                .baseUrl(URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Service::class.java)
        }
    }
}