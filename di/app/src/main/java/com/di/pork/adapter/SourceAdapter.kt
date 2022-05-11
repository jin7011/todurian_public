package com.di.pork.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.di.pork.R
import com.di.pork.data.Source

class SourceAdapter(
    val context: Context,
    private val source:ArrayList<Source>
) : BaseAdapter() {
    override fun getCount(): Int {
        return source.size
    }

    override fun getItem(p0: Int): Any {
        return source[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, p1: View?, parent: ViewGroup): View {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_source,parent,false)
            .apply {
                Log.e("adpater",source[position].toString())
                val name = findViewById<TextView>(R.id.sourceNameT)
                val link = findViewById<TextView>(R.id.sourceLinkT)
                val content = findViewById<TextView>(R.id.sourceContentT)

                name.text = "•${source[position].name}"
                link.text = source[position].link
                content.text = source[position].content
            }
        return view
    }
}