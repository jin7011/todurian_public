package com.di.pork.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.di.pork.activity.AddRecipeActivity
import com.di.pork.activity.RecipeInfoActivity
import com.di.pork.adapter.RecipeAdapter
import com.di.pork.data.Family
import com.di.pork.data.Recipe
import com.di.pork.databinding.FragmentRecipeBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.viewmodel.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment(
//    private val user: Family
) : Fragment() {

    @Inject lateinit var preference: PreferenceManager
    val id :String
        get() = preference.getString("id")!!
    private val viewModel:RecipeViewModel by viewModels()
    private val TAG = "RecipeFragment"

    companion object{
        fun newInstance(user: Family): RecipeFragment {
            val fragment = RecipeFragment()
            val args = Bundle()
            args.putParcelable("user",user)
            fragment.arguments = args

            return fragment
        }
    }

    private val user by lazy {
        requireArguments().getParcelable<Family>("user")!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG,"onCreate : $viewModel")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentRecipeBinding.inflate(inflater,container,false)
        val adapter = RecipeAdapter(ArrayList(),object : RecipeAdapter.LongClick{
            override fun longClick(id: String,title:String ,content: String) {
                viewModel.delRecipe(id,title,content)
                Toast.makeText(requireContext(),"삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }, object : RecipeAdapter.OnClick{
            override fun onClick(recipe:Recipe) {
                requireActivity().startActivity(Intent(requireContext(),RecipeInfoActivity::class.java).apply {
                    putExtra("recipe",recipe)
                })
            }
        })

        if(user.id != id)
            binding.addRecipeBtn.visibility = View.GONE

        binding.addRecipeBtn.setOnClickListener {
            requireActivity().startActivityFromFragment(this, Intent(requireActivity(),
                AddRecipeActivity::class.java).apply {
                putExtra("user",user)
                putParcelableArrayListExtra("recipeArray",viewModel.recipeList.value)},0)
        }

        binding.recipeListView.adapter = adapter

        viewModel.recipeList.observe(viewLifecycleOwner) {
                Log.e("asd","${it.size}")
                adapter.run {
                    recipeList = it
                    notifyDataSetChanged()
                }
            }

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        Log.e(TAG,"onResume : $viewModel")
        viewModel.getRecipe(user.id)

    }
}