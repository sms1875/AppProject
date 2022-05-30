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

class PlaceAdapter(private val context: Context) : RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    var datas = mutableListOf<placeData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_place,parent,false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }
    /******************클릭이벤트**********************/
    interface OnItemClickListener{
        fun onItemClick(v:View, data: placeData, pos : Int)
    }
    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }
    /******************클릭이벤트**********************/
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val placeDate: TextView = itemView.findViewById(R.id.item_placeDate)
        private val placeName: TextView = itemView.findViewById(R.id.item_placeName)
        private val placePrice: TextView = itemView.findViewById(R.id.item_placePrice)
        private val placeImage: ImageView = itemView.findViewById(R.id.item_placeImage)

        fun bind(item: placeData) {
            placeDate.text = String.format("시작일 : %s \n 종료일 : %s ",(item.start_time),(item.end_time))
            placeName.text = item.name
            placePrice.text = item.cost

            val uri= item.placeImage.split(",")
            if (placeImage != null) {
                Glide
                    .with(itemView)
                    .load(serverAddress + "/space/getImg/" + uri[0])//처음 오는이미지가 대표이미지
                    .override(100, 100)
                    .into(placeImage)
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
class VerticalItemDecorator2(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.top = divHeight
        outRect.bottom = divHeight
    }
}

class HorizontalItemDecorator2(private val divHeight : Int) : RecyclerView.ItemDecoration() {
    @Override
    override fun getItemOffsets(outRect: Rect, view: View, parent : RecyclerView, state : RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.left = divHeight
        outRect.right = divHeight
    }
}
