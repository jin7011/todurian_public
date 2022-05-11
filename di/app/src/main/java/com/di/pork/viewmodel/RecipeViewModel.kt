package com.di.pork.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.di.pork.data.Recipe
import com.di.pork.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){

    val recipeList: MutableLiveData<ArrayList<Recipe>> by lazy {
        MutableLiveData<ArrayList<Recipe>>()
    }

    fun getRecipe(id:String){
        //todo viewmodelscope를 쓰면 pause이후에 launch가 안되길래 일단 코루틴스코프로 바꿨음.
        CoroutineScope(Dispatchers.Main).launch {
            Log.e("RecipeViewModel","getRecipe")
            val response = repository.getRecipe(id)
            recipeList.value = response.recipeList
        }
    }

    fun delRecipe(id: String,title: String, content:String){
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.delRecipe(id,title, content)
            getRecipe(id)
        }
    }
}