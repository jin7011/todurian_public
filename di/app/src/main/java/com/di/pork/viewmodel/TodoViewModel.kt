package com.di.pork.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.di.pork.data.Todo
import com.di.pork.data.TodoList
import com.di.pork.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repository: Repository
): ViewModel(){

    val todoList: MutableLiveData<ArrayList<Todo>> by lazy {
        MutableLiveData<ArrayList<Todo>>()
    }

    fun getTodoList(id:String){
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.getTodoList(id)
            todoList.value = response.todolist
        }
    }

    fun delTodo(id:String,content:String){
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.delTodo(id,content)
            getTodoList(id)
        }
    }

    fun doTodo(id: String,content: String){
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.doTodo(id,content)
            getTodoList(id)
        }
    }

    fun addTodo(myId: String, myNickname: String, content: String){
        CoroutineScope(Dispatchers.Main).launch {
            val response = repository.addTodo(myId, myNickname, content)
            getTodoList(myId)
        }
    }
}