package com.example.hannyweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hannyweather.R
import com.example.hannyweather.logic.model.Place
import com.example.hannyweather.ui.weather.WeatherActivity

class PlaceAdapter(private val placeList: List<Place>, private val fragment: PlaceFragment) :
        RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {
    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeName: TextView = itemView.findViewById(R.id.placeName)
        val placeAddress: TextView = itemView.findViewById(R.id.placeAddresss)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.place_item, parent, false)
        val holder = PlaceViewHolder(view)

        // 绑定holder的每个itemView的点击事件
        holder.itemView.setOnClickListener {
            val position = holder.absoluteAdapterPosition
            val place = placeList[position]
            // 如果当前Activity本身就是在WeatherActivity中
            // 则不需要再启动新的Activity，只需刷新数据即可
            val activity = fragment.activity
            if (activity is WeatherActivity) {
                activity.binding.drawerLayout.closeDrawers()
                activity.viewModel.locationLng = place.location.lng
                activity.viewModel.locationLat = place.location.lat
                activity.viewModel.placeName = place.name
                activity.refreshWeather()
            } else {
                val intent = Intent(fragment.activity, WeatherActivity::class.java).apply {
                    putExtra("location_lng", place.location.lng)
                    putExtra("location_lat", place.location.lat)
                    putExtra("place_name", place.name)
                }
                fragment.startActivity(intent)
            }
            fragment.viewModel.savePlace(place)
        }

        return holder
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
    }

    override fun getItemCount(): Int = placeList.size
}