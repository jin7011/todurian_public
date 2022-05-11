package com.di.pork.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.di.pork.data.Family
import com.di.pork.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FamilyViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {


    val family: MutableLiveData<ArrayList<Family>> by lazy {
        MutableLiveData<ArrayList<Family>>()
    }

    fun getFamily(id:String){
        viewModelScope.launch {
            val response = repository.getFamily(id)
            if(response.result){
                family.value = response.family
                Log.e("FamilyViewModel","getFamily ${family.value}")
            }else{
                //todo 회원정보가 하나도 없는 상태(본인포함)
            }
        }
    }
}