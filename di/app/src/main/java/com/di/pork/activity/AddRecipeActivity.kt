package com.di.pork.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.di.pork.R
import com.di.pork.data.Family
import com.di.pork.data.Recipe
import com.di.pork.databinding.ActivityAddRecipeBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.di.pork.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AddRecipeActivity : AppCompatActivity() {

    lateinit var binding:ActivityAddRecipeBinding
    private val TAG = "AddRecipeActivity"
    @Inject lateinit var repository: Repository
    @Inject lateinit var preferenceManager: PreferenceManager
    val user by lazy {
        intent.extras!!.getParcelable<Family>("user")!!
    }
    val recipeArray by lazy {
        intent.getParcelableArrayListExtra<Recipe>("recipeArray")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView()
    }

    private fun setView(){
        binding = DataBindingUtil.setContentView(this,R.layout.activity_add_recipe)

        binding.writeToolbar.writeRecipeBtn.setOnClickListener {
            val title = binding.titleEdit.text.toString()
            val content = binding.contentEdit.text.toString()

            for(r in recipeArray!!){
                if (r.title == title){
                    toast("이미 존재하는 레시피 이름입니다.")
                    return@setOnClickListener
                }
            }

            CoroutineScope(Dispatchers.Main).launch {
                val response = repository.addRecipe(user.id,user.nickname,title,content)
                if(response){
                    toast("저장되었습니다.")
                    finish()
                }
                else {
                   toast("실패하였습니다.")
                }
            }
        }
    }

    fun toast(str:String){
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show()
    }
}