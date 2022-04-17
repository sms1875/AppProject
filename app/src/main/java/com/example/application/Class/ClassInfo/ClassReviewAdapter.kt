package com.example.application.Class.ClassInfo

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.application.*
import com.example.application.databinding.ItemClassReviewBinding



class ClassReviewAdapter(private val context: Context) : RecyclerView.Adapter<ClassReviewAdapter.ViewHolder>() {
    private var _binding: ClassReviewAdapter? = null

    lateinit var datas : List<userReview>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClassReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    class ViewHolder(private var binding: ItemClassReviewBinding ): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: userReview) {
            binding.itemClassReviewerName.text=item.userId
            binding.itemUserReviewContent.text=item.reviewContent
            binding.itemUserReviewRatingBar.rating=item.reviewScore.toFloat()

        }
    }

}
class ClassReviewVerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }
}

class ClassReviewHorizontalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = divHeight
        outRect.right = divHeight
    }
}


