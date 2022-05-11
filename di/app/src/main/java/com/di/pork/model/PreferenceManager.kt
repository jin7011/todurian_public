package com.di.pork.model

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceManager @Inject constructor(private val preferences: SharedPreferences){

    @SuppressLint("CommitPrefEdits")
    fun setString(key:String, value:String){
        preferences.edit().putString(key,value).apply()
    }

    @SuppressLint("MutatingSharedPrefs")
    fun setStringSet(key: String, value: String){
        val set = preferences.getStringSet(key,HashSet<String>())
        set?.add(value)
        preferences.edit().putStringSet(key,set).apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun setBoolean(key:String, value:Boolean){
        preferences.edit().putBoolean(key,value).apply()
    }

    @SuppressLint("CommitPrefEdits")
    fun setInt(key:String, value:Int){
        preferences.edit().putInt(key,value).apply()
    }

    fun getString(key:String): String? {
        return preferences.getString(key,"")
    }

    fun getStringSet(key: String): Set<String>? {
        return preferences.getStringSet(key,HashSet<String>())
    }

    fun getBoolean(key:String): Boolean {
        return preferences.getBoolean(key,true)
    }

//    fun getNoti(context: Context, key:String): Boolean { //setting에 같이 쓰고 싶었는데 기본을 true로 놓아야해서 따로 만듬
//        val pre = context.getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE)
//        return pre.getBoolean(key,true)
//    }
//
//    @SuppressLint("CommitPrefEdits")
//    fun setNoti(context: Context, key:String, value:Boolean) {
//        val pre = context.getSharedPreferences("NOTIFICATION", Context.MODE_PRIVATE)
//        pre.edit().putBoolean(key,value).apply()
//    }

    fun getInt(key:String): Int {
        return preferences.getInt(key,-1)
    }

    @SuppressLint("MutatingSharedPrefs")
    fun removeSetElement(key:String, value:String){
        val set = preferences.getStringSet(key,HashSet<String>())
        set?.remove(value)
        preferences.edit().putStringSet(key,set).apply()
    }

    fun removeKey(key:String){
        preferences.edit().remove(key).apply()
    }

    fun clear(){
        preferences.edit().clear().apply()
    }

    companion object{
        private val PREFERENCES_NAME = "Setting"

        fun getPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }
}