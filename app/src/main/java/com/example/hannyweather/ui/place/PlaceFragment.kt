package com.example.hannyweather.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hannyweather.R
import com.example.hannyweather.databinding.FragmentPlaceBinding
import com.example.hannyweather.viewmodel.PlaceViewModel

class PlaceFragment : Fragment() {
    private val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

    private var _binding: FragmentPlaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 加载Place页面
        _binding = FragmentPlaceBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val layoutManager = LinearLayoutManager(activity)

        // 配置recyclerView
        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(viewModel.placeList)
        binding.recyclerView.adapter = adapter

        binding.searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                // 更新viewModel的liveData，让其观察到
                viewModel.searchPlaces(content)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.placeFragmentLayout.setBackgroundResource(R.drawable.bg_place)
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }

        viewModel.placeLiveData.observe(viewLifecycleOwner, {
            val places = it.getOrNull()
            if (places != null) {
                binding.recyclerView.visibility = View.VISIBLE
                binding.placeFragmentLayout.background = null
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                it.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}