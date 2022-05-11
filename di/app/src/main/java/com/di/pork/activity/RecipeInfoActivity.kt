package com.di.pork.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.di.pork.R
import com.di.pork.data.Family
import com.di.pork.data.Recipe
import com.di.pork.databinding.ActivityRecipeInfoBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeInfoActivity : AppCompatActivity() {

    lateinit var binding:ActivityRecipeInfoBinding
    val recipe by lazy {
        intent.extras!!.getParcelable<Recipe>("recipe")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe_info)

        binding.recipeToolbar.recipeTitleT.text = recipe.title
        binding.recipeT.text = recipe.content
    }
}