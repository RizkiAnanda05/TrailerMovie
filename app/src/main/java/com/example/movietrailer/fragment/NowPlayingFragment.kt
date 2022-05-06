package com.example.movietrailer.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietrailer.R
import com.example.movietrailer.activity.DetailActivity
import com.example.movietrailer.adapter.MainAdapter
import com.example.movietrailer.databinding.FragmentNowPlayingBinding
import com.example.movietrailer.model.Constant
import com.example.movietrailer.model.MovieModel
import com.example.movietrailer.model.MovieResponse
import com.example.movietrailer.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NowPlayingFragment : Fragment() {

    private var isScrolling = false
    private var currentPage = 1
    private var totalpages = 0

    lateinit var mainAdapter: MainAdapter

    private lateinit var binding: FragmentNowPlayingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        setuplistener()
    }

    override fun onStart() {
        super.onStart()
        getMovieNowPlaying()
        showLoadingNowPlayingNextPage(false)
    }
    private fun setuplistener() {
        binding.nestedScroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(
                v: NestedScrollView?,
                scrollX: Int,
                scrollY: Int,
                oldScrollX: Int,
                oldScrollY: Int
            ) {
                if(scrollY == v!!.getChildAt(0).measuredHeight - v.measuredHeight){
                    if(!isScrolling){
                        if(currentPage <= totalpages){
                            getMovieNowPlayingNextPage()
                        }
                    }
                }
            }

        })
    }

    private fun setupRecycleView(){
        mainAdapter = MainAdapter(arrayListOf(),object : MainAdapter.OnAdapterListener{
            override fun onClick(movies: MovieModel) {

                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }
        })
        binding.Rc1.apply{
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }
    private fun getMovieNowPlaying(){
        currentPage = 1
        showLoading(true)
        ApiService().endPoint.getMovieNowPlaying(Constant.API_KEY,1)
            //ApiService().endPoint.getMovieNowPlaying(Constant.API_KEY,1)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    showLoading(false)
                    if(response.isSuccessful){
                        showMovie(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                    showLoading(false)
                }

            })
    }
    private fun getMovieNowPlayingNextPage(){
        currentPage += 1
        showLoadingNowPlayingNextPage(true)
        ApiService().endPoint.getMovieNowPlaying(Constant.API_KEY,currentPage)
            //ApiService().endPoint.getMovieNowPlaying(Constant.API_KEY,1)
            .enqueue(object : Callback<MovieResponse> {
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    showLoadingNowPlayingNextPage(false)
                    if(response.isSuccessful){
                        showMovieNowPlayingNextPage(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {

                    showLoadingNowPlayingNextPage(false)
                }

            })
    }
    fun showMovie(response : MovieResponse){
        totalpages = response.total_pages!!.toInt()
        mainAdapter.setData( response.results )
    }
    fun showMovieNowPlayingNextPage(response : MovieResponse){
        totalpages = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage( response.results )
    }
    fun showLoading(loading : Boolean){
        when(loading){
            true -> binding.progressbar.visibility = View.VISIBLE
            false -> binding.progressbar.visibility = View.GONE
        }

    }
    fun showLoadingNowPlayingNextPage(loading : Boolean){
        when(loading){
            true -> {
                isScrolling = true
                binding.progressbarNested.visibility = View.VISIBLE
            }
            false ->{
                isScrolling = false
                binding.progressbarNested.visibility = View.GONE
            }
        }

    }

}