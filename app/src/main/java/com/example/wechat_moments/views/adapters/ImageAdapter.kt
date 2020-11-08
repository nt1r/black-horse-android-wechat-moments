package com.example.wechat_moments.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.example.wechat_moments.R

class ImageAdapter(
    private val context: Context,
    private val images: List<String>
) : BaseAdapter() {
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
        val imageView: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.content_image_view, null)
        Glide.with(context)
            .load(getItem(position))
            .into(imageView as AppCompatImageView)
        return imageView
    }
}