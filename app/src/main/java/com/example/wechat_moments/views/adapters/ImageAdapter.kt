package com.example.wechat_moments.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.wechat_moments.R
import com.example.wechat_moments.views.utils.UnitConvertor


class ImageAdapter(
    private val context: Context,
    private val images: List<String>,
    private val layoutInflater: LayoutInflater
) : BaseAdapter() {
    private val IMAGE_WIDTH_DP = 96.0F
    private val IMAGE_HEIGHT_DP = 96.0F

    override fun getCount(): Int {
        return images.size
    }

    override fun getItem(position: Int): Any {
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        return images[position].hashCode().toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var imageView: AppCompatImageView? = null
        return if (convertView == null) {
            imageView = layoutInflater.inflate(R.layout.thumbnail_image_view, null) as AppCompatImageView
            val imageWidth = UnitConvertor.convertDpToPx(context, IMAGE_WIDTH_DP)
            val imageHeight = UnitConvertor.convertDpToPx(context, IMAGE_HEIGHT_DP)
            // Log.d("Wechat_adaptor", "$imageWidth, $imageHeight")
            Glide.with(context)
                .load(getItem(position))
                //.apply(RequestOptions().override(imageWidth.toInt(), imageHeight.toInt()))
                .override(imageWidth.toInt(), imageHeight.toInt())
                .centerCrop()
                .into(imageView)
            imageView
        } else {
            convertView
        }
    }
}
