package com.example.hannyweather.ui.place

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hannyweather.R
import com.example.hannyweather.logic.model.Place

class PlaceAdapter(private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.placeName)
        val placeAddress: TextView = itemView.findViewById(R.id.placeAddresss)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int = placeList.size
}