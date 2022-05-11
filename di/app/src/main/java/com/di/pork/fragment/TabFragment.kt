package com.di.pork.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.di.pork.R
import com.di.pork.data.Family
import com.di.pork.databinding.FragmentTabBinding
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabFragment(
//    private val user: Family
) : Fragment() {

    companion object{
        fun newInstance(user: Family): TabFragment {
            val fragment = TabFragment()
            val args = Bundle()
            args.putParcelable("user",user)
            fragment.arguments = args

            return fragment
        }
    }

    private val user by lazy {
        requireArguments().getParcelable<Family>("user")!!
    }
    private val TAG = "TabFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e(TAG, user.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentTabBinding.inflate(inflater,container,false)
        val tabLayout = binding.tabs
        val todoFrag by lazy { TodoFragment.newInstance(user) }
        val recipeFrag by lazy { RecipeFragment.newInstance(user) }
        val calFrag by lazy { CalFragment() }
        var selected:Fragment? = null

        //todo show, hide로 바꿀 수 있는지 확인해보기
        childFragmentManager.beginTransaction().apply {
            replace(R.id.frame_tab,todoFrag)
            commit()
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position){
                    0->{selected = todoFrag}
                    1->{selected = recipeFrag}
                    2->{selected = calFrag}
                }
                childFragmentManager.beginTransaction().apply {
                    replace(R.id.frame_tab,selected!!)
                    commit()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return binding.root
    }

}