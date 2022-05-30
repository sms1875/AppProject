
import com.bumptech.glide.Glide


import android.content.Context
import android.graphics.drawable.Drawable

import androidx.annotation.NonNull

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.ImageView
import com.example.application.R
import com.example.application.serverAddress


class ImageSliderAdapter(context: Context, sliderImage: Array<String>) :
    RecyclerView.Adapter<ImageSliderAdapter.MyViewHolder>() {
    private val context: Context
    private val sliderImage: Array<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.placeimageviewpager, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindSliderImage(sliderImage[position])
    }

    override fun getItemCount(): Int {
        return sliderImage.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mImageView: ImageView
        fun bindSliderImage(imageURL: String?) {
            Glide.with(context)
                .load(serverAddress +"/space/getImg/"+imageURL)
                .into(mImageView)
        }

        init {
            mImageView = itemView.findViewById(R.id.imageSlider)
        }
    }

    init {
        this.context = context
        this.sliderImage = sliderImage
    }
}