package com.example.application.ui.Class.ClassInfo

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.application.databinding.ItemClassQnaBinding
import com.example.application.databinding.ItemClassQnaReplyBinding

import com.example.application.network.response.*
/*
class ClassQnAAdapter(private val context: Context) :  RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var _binding: ClassQnAAdapter? = null

    //lateinit var datas : List<QnAPostList>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            0 -> {
                val binding = ItemClassQnaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder1(binding)
            }
            else -> {
                val binding = ItemClassQnaReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ViewHolder2(binding)
            }
        }
    }

    override fun getItemCount(): Int = datas.size

    override fun getItemViewType(position: Int): Int {
        if(datas[position].QnAId==datas[position].QnAgroupId){
            return 0
        }
        else return 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(datas[position].QnAId==datas[position].QnAgroupId){
            (holder as ViewHolder1).bind(datas[position])
            holder.setIsRecyclable(false)
        }
        else {
            (holder as ViewHolder2).bind(datas[position])
            holder.setIsRecyclable(false)
        }
    }

    class ViewHolder1(private var binding: ItemClassQnaBinding ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QnAPostList) {
            with(binding) {
                itemClassQnADate.text = item.QnAdate
                itemClassQnATitle.text = item.QnAtitle
                itemClassQnAauthor.text = item.QnAauther
            }
        }
    }
    class ViewHolder2(private var binding: ItemClassQnaReplyBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: QnAPostList) {
            with(binding) {
                itemClassQnADate.text = item.QnAdate
                itemClassQnATitle.text = item.QnAtitle
                itemClassQnAauthor.text = item.QnAauther
            }
        }
    }
}
class ClassQnAVerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }
}

class ClassQnAHorizontalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = divHeight
        outRect.right = divHeight
    }
}


*/