package com.example.hannyweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hannyweather.MainActivity
import com.example.hannyweather.R
import com.example.hannyweather.databinding.FragmentPlaceBinding
import com.example.hannyweather.ui.weather.WeatherActivity
import com.example.hannyweather.viewmodel.PlaceViewModel

class PlaceFragment : Fragment() {
    val viewModel by lazy {
        ViewModelProvider(this).get(PlaceViewModel::class.java)
    }

    private lateinit var adapter: PlaceAdapter

    private var _binding: FragmentPlaceBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // 搜索延迟,搜索输入不是直接请求数据，而是延迟一段时间再进行异步请求
    val delay: Long = 600

    // after the monitor the search view change for a delayed time ${delay}
    var runnable: Runnable? = null


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

        // 加载缓存数据
        // activity is MainActivity 这句话很重要
        // 因为在WeatherActivity中引用了当前Fragment
        // 这样会导致无效循环的情况
        // Log.d(HannyWeatherApplication.DEBUG_TAG,"当前活动: $activity")
        if (activity is MainActivity && viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        val layoutManager = LinearLayoutManager(activity)

        // 配置recyclerView
        binding.recyclerView.layoutManager = layoutManager
        adapter = PlaceAdapter(viewModel.placeList, this)
        binding.recyclerView.adapter = adapter

        // 配置EditText
        binding.searchPlaceEdit.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchPlaceEdit.removeCallbacks(runnable)
                runnable = null
            }

            override fun afterTextChanged(s: Editable?) {
                if (runnable == null) {
                    runnable = Runnable {
                        val content = s.toString().trim() // 去掉前后的空格

                        if (content.isEmpty()) {
                            // 如果搜索内容为空，直接隐藏recyclerList
                            binding.recyclerView.visibility = View.GONE
                            binding.placeFragmentLayout.setBackgroundResource(R.drawable.bg_place)
                            viewModel.placeList.clear()
                            adapter.notifyDataSetChanged()
                        } else {
                            // 更新viewModel的liveData，让其观察到
                            viewModel.searchPlaces(content)
                        }
                    }
                }

                binding.searchPlaceEdit.postDelayed(runnable, delay)
            }

        })


        /*
        binding.searchPlaceEdit.setOnEditorActionListener { _, actionId, _ ->
            // 隐藏键盘
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val inputMethodManager =
                    activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    view?.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            true
        }
         */


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