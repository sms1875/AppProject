package com.example.application

import android.content.Context
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.application.network.response.*

class ClassAdapter(private val context: Context) : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    var datas = mutableListOf<classData>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_class,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(datas[position])
    }
    /******************클릭이벤트**********************/
    interface OnItemClickListener{
        fun onItemClick(v:View, data: classData, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
    /******************클릭이벤트**********************/
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val className: TextView?= itemView.findViewById(R.id.item_className)
        private val classTutor: TextView? = itemView.findViewById(R.id.item_classTutor)
        private val classPrice: TextView? = itemView.findViewById(R.id.item_classPrice)
        private val classImage: ImageView? = itemView.findViewById(R.id.item_classImage)
        private val classOnOffline: TextView? = itemView.findViewById(R.id.item_classOnOffType)

        fun bind(item: classData) {

            if (className != null) {
                className.text = item.className
            }
            if (classTutor != null) {
                classTutor.text = item.classTutor
            }
            if (classPrice != null) {
                classPrice.text = item.classPrice
            }
            if (classOnOffline != null) {
                if (item.classOnOffline.toInt()==1)
                    classOnOffline.text = "온라인 강의"
                else
                    classOnOffline.text = "오프라인 강의"
            }
            val uri= item.classImage.split(",")
            if (classImage != null) {
                Glide
                    .with(itemView)
                    .load(serverAddress+"/class/getImg/" + uri[0])//처음 오는이미지가 대표이미지
                    .override(100, 100)
                    .into(classImage)
            }


            /******************아이템클릭이벤트**********************/
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
            /******************아이템클릭이벤트**********************/
        }
    }

}
class VerticalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }
}

class HorizontalItemDecorator(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = divHeight
        outRect.right = divHeight
    }
}
