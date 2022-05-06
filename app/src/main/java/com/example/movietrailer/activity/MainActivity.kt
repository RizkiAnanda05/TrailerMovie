package com.example.movietrailer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movietrailer.R
import com.example.movietrailer.adapter.MainAdapter
import com.example.movietrailer.databinding.ActivityMainBinding
import com.example.movietrailer.databinding.MainContentBinding
import com.example.movietrailer.model.Constant
import com.example.movietrailer.model.MovieModel
import com.example.movietrailer.model.MovieResponse
import com.example.movietrailer.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val moviepopular = 0
const val movienowplaying = 1

class MainActivity : AppCompatActivity() {
    private val TAG : String = "MainActivity"
    lateinit var binding: ActivityMainBinding
    lateinit var bindingcontent: MainContentBinding
    lateinit var mainAdapter: MainAdapter
    private var isScrolling = false
    private var currentPage = 1
    private var totalpages = 0

    private var categorymovie = 0
    private val api = ApiService().endPoint

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        bindingcontent = binding.ContainerMain
        setContentView(binding.root)
        setupRecycleView()
        setuplistener()

    }

    private fun setuplistener() {
        bindingcontent.nestedScroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
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
                            getMovieNextPage()
                        }
                    }
                }
            }

        })
    }

    override fun onStart() {
        super.onStart()
        getMovie()
        showLoadingNextPage(false)

    }
    private fun setupRecycleView(){
        mainAdapter = MainAdapter(arrayListOf(),object : MainAdapter.OnAdapterListener{
            override fun onClick(movies: MovieModel) {

                startActivity(Intent(applicationContext, DetailActivity::class.java))
            }
        })
        bindingcontent.Rc1.apply{
            layoutManager = GridLayoutManager(context, 2)
            adapter = mainAdapter
        }
    }
    private fun getMovie(){

        bindingcontent.nestedScroll.scrollTo(0,0)
        currentPage = 1
        showLoading(true)
        var apicall : Call<MovieResponse>? = null
        when(categorymovie){
            moviepopular -> {
                apicall = api.getMoviePopular(Constant.API_KEY,1)
            }
            movienowplaying -> {
                apicall = api.getMovieNowPlaying(Constant.API_KEY,1)
            }
        }
        apicall!!
            //ApiService().endPoint.getMovieNowPlaying(Constant.API_KEY,1)
            .enqueue(object : Callback<MovieResponse>{
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
    private fun getMovieNextPage(){
        currentPage += 1
        showLoadingNextPage(true)
        var apicall : Call<MovieResponse>? = null
        when(categorymovie){
            moviepopular -> {
                apicall = api.getMoviePopular(Constant.API_KEY,currentPage)
            }
            movienowplaying -> {
                apicall = api.getMovieNowPlaying(Constant.API_KEY,currentPage)
            }
        }
        apicall!!
            //ApiService().endPoint.getMovieNowPlaying(Constant.API_KEY,1)
            .enqueue(object : Callback<MovieResponse>{
                override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                    showLoadingNextPage(false)
                    if(response.isSuccessful){
                        showMovieNextPage(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                    Log.d(TAG,"ErrorResponse $t")
                    showLoadingNextPage(false)
                }
            })
    }
    fun showMovie(response : MovieResponse){
//
        totalpages = response.total_pages!!.toInt()
        mainAdapter.setData( response.results )
    }
    fun showMovieNextPage(response : MovieResponse){

        totalpages = response.total_pages!!.toInt()
        mainAdapter.setDataNextPage( response.results )
        Toast.makeText(this, "Page $currentPage", Toast.LENGTH_SHORT).show()
    }
    fun showLoading(loading : Boolean){
        when(loading){
            true -> bindingcontent.progressbar.visibility = View.VISIBLE
            false -> bindingcontent.progressbar.visibility = View.GONE
        }

    }
    fun showLoadingNextPage(loading : Boolean){
        when(loading){
            true -> {
                isScrolling = true
                bindingcontent.progressbarNested.visibility = View.VISIBLE
            }
            false -> {
                isScrolling = false
                bindingcontent.progressbarNested.visibility = View.GONE
            }
        }

    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.popular_item -> {

                categorymovie = moviepopular
                getMovie()

                true
            }
            R.id.now_playing_item -> {

                categorymovie = movienowplaying
                getMovie()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}