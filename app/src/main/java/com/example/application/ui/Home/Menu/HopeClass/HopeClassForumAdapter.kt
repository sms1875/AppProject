package com.example.application.ui.Home.Menu.HopeClass


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.ItemHopeClassTopicBinding

import com.example.application.network.response.*
class HopeClassForumAdapter(val data: MutableList<hopeClassTopic>) : RecyclerView.Adapter<HopeClassForumAdapter.Holder>() {

    interface OnItemClickListner{
        fun onItemClick(view: View, position: Int,data: hopeClassTopic)
    }

    private lateinit var itemClickListner: OnItemClickListner

    fun setOnItemclickListner(onItemClickListner: OnItemClickListner){
        itemClickListner = onItemClickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): HopeClassForumAdapter.Holder {
        val binding = ItemHopeClassTopicBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: HopeClassForumAdapter.Holder, position: Int) {
        val datas = data.get(position)
        holder.setItem(datas)
    }

    override fun getItemCount() = data.size

    inner class Holder(val binding: ItemHopeClassTopicBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(item: hopeClassTopic){
            binding.itemTopicTitle.text=item.desiredTitle
            binding.itemTopicRecommendationCount.text=item.desiredClassLike
            binding.itemTopicContent1.text=item.category
            binding.itemTopicContent2.text=item.isOn
            binding.itemTopicContent3.text=item.type

            binding.itemTopicTitle.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    itemClickListner.onItemClick(binding.itemTopicTitle,pos,item)
                }
            }
        }
    }
}