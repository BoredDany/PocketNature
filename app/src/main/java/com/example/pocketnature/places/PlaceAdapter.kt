package com.example.pocketnature.places

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pocketnature.R
import com.example.pocketnature.model.Place

class PlaceAdapter (private val context: Context?, private val places: List<Place>): BaseAdapter() {
    override fun getCount(): Int {
        return places.size
    }

    override fun getItem(position: Int): Place {
        return places[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can use a unique ID if available
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.place_view, parent, false)

        val place = getItem(position)
        val namePlace = view.findViewById<TextView>(R.id.namePlace)
        namePlace.text = place.name

        val imgPlace = view.findViewById<ImageView>(R.id.imgPlace)
        if (context != null) {
            val imageViewWidth = imgPlace.width
            val imageViewHeight = imgPlace.height
            Glide.with(context)
                .load(R.drawable.nevado)
                .override(imageViewWidth, imageViewHeight) // Cargar la imagen con la resolución correcta
                .into(imgPlace)
        }

        return view
    }
}