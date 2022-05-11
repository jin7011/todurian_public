package com.di.pork.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.di.pork.adapter.TabPagerAdapter
import com.di.pork.adapter.ProfileAdapter
import com.di.pork.data.Family
import com.di.pork.databinding.FragmentHomeViewPaperBinding
import com.di.pork.model.PreferenceManager
import com.di.pork.model.Repository
import com.di.pork.utility.Utility
import com.di.pork.viewmodel.FamilyViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeViewPagerFragment : Fragment() {
    @Inject lateinit var utility: Utility
    @Inject lateinit var preference: PreferenceManager
    @Inject lateinit var repository : Repository
    private val viewModel: FamilyViewModel by viewModels()
    private val TAG = "HomeViewPaperFragment"
    lateinit var binding: FragmentHomeViewPaperBinding
    lateinit var profileAdapter: ProfileAdapter
    lateinit var tabPagerAdapter: TabPagerAdapter
    lateinit var viewPager:ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getFamily(preference.getString("id")!!)
        Log.e(TAG,"onCreate")
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.e(TAG,"onViewCreated")

        tabPagerAdapter = TabPagerAdapter(this,ArrayList())

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(position: Int) {}
            override fun onPageScrolled(position: Int,positionOffset: Float,positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                Log.e(TAG,"position: $position")
                Log.e(TAG,"holder: ${binding.profileRecyclerView.findViewHolderForAdapterPosition(position)}")
                val holder = binding.profileRecyclerView.findViewHolderForAdapterPosition(position) as ProfileAdapter.ProfileHolder
                profileAdapter.onClick_func(holder)
            }
        })

        profileAdapter = ProfileAdapter(requireActivity(),ArrayList(),object : ProfileAdapter.OnClick {
            override fun onClick(id: String,position: Int) {
                viewPager.currentItem = position
            }
        }, object : ProfileAdapter.LongClick{
            override fun longClick(family: Family) {
                val dialog = DelDialogFragment(viewModel,family)
                dialog.show(requireActivity().supportFragmentManager,"custom")
            }
        })

        adapterInit(profileAdapter, viewPager, tabPagerAdapter)

        binding.addFamilyBtn.setOnClickListener {
            val dialog = AddDialogFragment(viewModel)
            dialog.show(requireActivity().supportFragmentManager,"custom")
        }

        viewModel.family.observe(requireActivity()){
            Log.e(TAG,"viewmodel observed")
            profileAdapter.run {
                family = it
//                notifyDataSetChanged()
            }
            tabPagerAdapter.run {
                family = it
                Log.e(TAG,"pager: ${this.family}")
//                notifyDataSetChanged()
            }
            adapterInit(profileAdapter, viewPager, tabPagerAdapter)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeViewPaperBinding.inflate(inflater,container,false)
        viewPager = binding.viewPager


        return binding.root
    }

    fun adapterInit(profileAdapter: ProfileAdapter, viewPager:ViewPager2, tabPagerAdapter: TabPagerAdapter){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        binding.profileRecyclerView.layoutManager = layoutManager
        binding.profileRecyclerView.adapter = profileAdapter
        viewPager.adapter = tabPagerAdapter
    }

    override fun onPause() {
        super.onPause()
        Log.e(TAG,"onPause")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG,"onDestroyView")
    }
}