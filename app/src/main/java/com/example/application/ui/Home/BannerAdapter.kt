package com.example.application.ui.Home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.application.databinding.ItemBannerBinding

class BannerAdapter(var data: MutableList<String>) : RecyclerView.Adapter<BannerAdapter.Holder>() {

    interface OnItemClickListner{
        fun onItemClick(view: View, position: Int,data: String)
    }

    private lateinit var itemClickListner: OnItemClickListner

    fun setOnItemclickListner(onItemClickListner: OnItemClickListner){
        itemClickListner = onItemClickListner
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): BannerAdapter.Holder {
        val binding = ItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: BannerAdapter.Holder, position: Int) {
        val datas = data.get(position%ItemCount())
        holder.setItem(datas)
    }

    override fun getItemCount() = 2000

    fun ItemCount() = data.size

    inner class Holder(val binding: ItemBannerBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItem(uri: String){
            Glide
                .with(itemView)
                //.load(serverAddress +"/db/class/getImg/" + uri)//처음 오는이미지가 대표이미지
                .load("https://www.google.com/images/" + uri)//처음 오는이미지가 대표이미지
                .override(100, 100)
                .into(binding.itemBannerImage)
            binding.itemBannerImage.setOnClickListener {
                val pos = adapterPosition
                if(pos != RecyclerView.NO_POSITION){
                    itemClickListner.onItemClick(binding.itemBannerImage,pos,uri)
                }
            }
        }
    }
    fun submitList(list: List<String>?) {
        data = list as MutableList<String>
        notifyDataSetChanged()
    }
}