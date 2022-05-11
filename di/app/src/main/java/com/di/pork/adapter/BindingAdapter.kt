package com.di.pork.adapter

import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.di.pork.R

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("isDone")
fun bindIsDone(view: View, isDone: Int) {
    view.background = if (isDone == 1) {
        ContextCompat.getDrawable(view.context, R.drawable.img)
    } else {
        ContextCompat.getDrawable(view.context, R.drawable.x)
    }
}