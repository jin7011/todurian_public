package com.di.pork.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.di.pork.data.Family
import com.di.pork.data.User
import com.di.pork.fragment.CalFragment
import com.di.pork.fragment.RecipeFragment
import com.di.pork.fragment.TabFragment
import com.di.pork.fragment.TodoFragment
import com.di.pork.model.Repository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class TabPagerAdapter(
    fragment: Fragment,
    var family: ArrayList<Family>,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return family.size
    }

    override fun createFragment(position: Int): Fragment {
        val user = family[position]
        return TabFragment.newInstance(user)
    }
}