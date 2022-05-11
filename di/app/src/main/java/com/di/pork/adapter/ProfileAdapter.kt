package com.di.pork.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.di.pork.R
import com.di.pork.data.Family
import com.google.android.material.card.MaterialCardView

class ProfileAdapter(
    val activity: Activity,
    var family: ArrayList<Family>,
    val onClick: OnClick,
    val longClick: LongClick,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var currentHolder: ProfileHolder? = null

    interface OnClick{
        fun onClick(id:String,position: Int)
    }

    interface LongClick{
        fun longClick(family:Family)
    }

    inner class ProfileHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val profile = view.findViewById<ImageView>(R.id.profileIV)
        val nickname = view.findViewById<TextView>(R.id.nicknameT)
        val cardView = view.findViewById<MaterialCardView>(R.id.profileCardview)

        fun bind(item: Family) {
//            if(family[0].id == item.id){ //들어오면 첫 번째 화면은 반드시 본인으로 설정하기 위함
//                pick()
//                onClick.onClick(family[absoluteAdapterPosition].id,0)
//            }
            nickname.text = item.nickname
            Glide.with(view).load(activity.resources.getString(R.string.imageUrl) + item.image)
                .apply(RequestOptions().fitCenter().circleCrop())
                .into(profile)
        }

        fun pick() {
            //이전꺼는 꺼주고
            currentHolder?.let {
                currentHolder!!.cardView.strokeColor = ContextCompat.getColor(activity, R.color.night)
            }
            cardView.strokeColor = ContextCompat.getColor(activity, R.color.white)
            currentHolder = this
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_profile, parent, false)
        val holder = ProfileHolder(view)

        view.setOnClickListener {
            onClick_func(holder)
        }

        view.setOnLongClickListener {
            val myId = family[0].id
            val family = family[holder.absoluteAdapterPosition]
            if(myId != family.id){
                longClick.longClick(family)
                return@setOnLongClickListener true
            }else
                return@setOnLongClickListener false
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProfileHolder -> {
                holder.bind(family[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return family.size
    }

    fun onClick_func(holder:ProfileHolder){
        holder.pick()
        onClick.onClick(family[holder.absoluteAdapterPosition].id,holder.absoluteAdapterPosition)
    }
}