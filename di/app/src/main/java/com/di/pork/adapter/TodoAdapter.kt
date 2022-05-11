package com.di.pork.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.di.pork.data.Todo
import com.di.pork.databinding.ItemTodoBinding

class TodoAdapter(
    var todoList:ArrayList<Todo>,
    val longClick: LongClick,
    val onClick: OnClick
) : BaseAdapter() {

    private val TAG = "TodoAdapter"
    interface LongClick{
        fun longClick(id:String,content:String)
    }
    interface OnClick{
        fun onClick(id:String,content:String)
    }

    override fun getCount(): Int {
        return todoList.size
    }

    override fun getItem(p0: Int): Any {
        return todoList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val list = redate(todoList)
        val newTodo = list[position]
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.apply {
            todoLinear.setOnLongClickListener {
                longClick.longClick(newTodo.id,newTodo.content)
                return@setOnLongClickListener true
            }
            doBtn.setOnClickListener {
                onClick.onClick(newTodo.id,newTodo.content)
            }

            todo = newTodo
            executePendingBindings()
        }
        return binding.root
    }

    fun redate(todoList:ArrayList<Todo>): ArrayList<Todo>{
        for (todo in todoList){
            var date = todo.date
            if(date.length > 14){
                date = date.removeRange(16,date.length)
                date = date.removeRange(0,2)
                date = date.replace("T"," ")
                todo.date = date
            }
        }
        return todoList
    }

}