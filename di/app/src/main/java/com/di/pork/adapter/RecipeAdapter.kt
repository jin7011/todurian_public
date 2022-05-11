package com.di.pork.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.di.pork.data.Recipe
import com.di.pork.data.Todo
import com.di.pork.databinding.ItemRecipeBinding
import com.di.pork.databinding.ItemTodoBinding
import kotlinx.coroutines.NonDisposableHandle.parent

class RecipeAdapter (
    var recipeList:ArrayList<Recipe>,
    val longClick: LongClick,
    val onClick: OnClick
) : BaseAdapter() {

    interface LongClick{
        fun longClick(id:String,title:String,content:String)
    }
    interface OnClick{
        fun onClick(recipe: Recipe)
    }

    override fun getCount(): Int {
        return recipeList.size
    }

    override fun getItem(p0: Int): Any {
        return recipeList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val list = redate(recipeList)
        val newRecipe = list[position]
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        binding.apply {
            binding.todoLinear.setOnClickListener {
                onClick.onClick(newRecipe)
            }

            binding.todoLinear.setOnLongClickListener {
                longClick.longClick(newRecipe.id,newRecipe.title,newRecipe.content)
                return@setOnLongClickListener true
            }

            recipe = newRecipe
            executePendingBindings()
        }
        return binding.root
    }

    fun redate(recipeList:ArrayList<Recipe>): ArrayList<Recipe>{
        for (Recipe in recipeList){
            var date = Recipe.date
            if(date.length > 14){
                date = date.removeRange(16,date.length)
                date = date.removeRange(0,2)
                date = date.replace("T"," ")
                Recipe.date = date
            }
        }
        return recipeList
    }

}