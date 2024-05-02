package com.example.pocketnature.nature

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.pocketnature.R
import com.example.pocketnature.model.Specie
import com.example.pocketnature.utils.SpecieCategory

class SpeciesAdapter (private val context: Context?, private val species: List<Specie>): BaseAdapter() {
    override fun getCount(): Int {
        return species.size
    }

    override fun getItem(position: Int): Specie {
        return species[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong() // You can use a unique ID if available
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.specie_view, parent, false)

        val specie = getItem(position)
        val namePlace = view.findViewById<TextView>(R.id.specieName)
        namePlace.text = specie.commonName + " - " + specie.specie

        val imgPlace = view.findViewById<ImageView>(R.id.imgSpeicie)
        var img = R.drawable.baseline_account_blue
        when(specie.specie){
            SpecieCategory.REPTILES -> { img = R.drawable.crocodile_icon }
            SpecieCategory.MAMMALS -> { img = R.drawable.monkey_icon }
            SpecieCategory.BIRDS -> { img = R.drawable.bird_icon }
            SpecieCategory.INSECTS -> { img = R.drawable.bug_icon }
            SpecieCategory.AMPHIBIANS -> { img = R.drawable.frog_icon }
        }
        if (context != null) {
            val imageViewWidth = imgPlace.width
            val imageViewHeight = imgPlace.height
            Glide.with(context)
                .load(img)
                .override(imageViewWidth, imageViewHeight) // Cargar la imagen con la resolución correcta
                .into(imgPlace)
        }

        return view
    }
}