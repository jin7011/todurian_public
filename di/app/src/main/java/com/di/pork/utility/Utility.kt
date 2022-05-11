package com.di.pork.utility

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.FragmentScoped
import javax.inject.Inject

class Utility @Inject constructor(private val layoutManager: LinearLayoutManager) {

    fun recyclerInit(
        recyclerView: RecyclerView,
        orientation: String,
        adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    ) {

        when (orientation) {
            "VERTICAL" -> {
                layoutManager.orientation = LinearLayoutManager.VERTICAL

            }
            "HORIZON" -> {
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            }
        }
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        val animator = recyclerView.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }
    }

    companion object {
        fun getLinearLayoutManager(@ActivityContext context: Context): LinearLayoutManager {
            return LinearLayoutManager(context)
        }
    }
}