package com.example.movietrailer.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movietrailer.adapter.TrailerAdapter
import com.example.movietrailer.databinding.ActivityTrailerBinding
import com.example.movietrailer.model.Constant
import com.example.movietrailer.model.TrailerResponse
import com.example.movietrailer.retrofit.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener



class TrailerActivity : AppCompatActivity() {
    private lateinit var binding : ActivityTrailerBinding
    private lateinit var bindingtrailer : TrailerAdapter
    lateinit var youTubePlayer: YouTubePlayer
    private var youtubekey : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setuprecycleview()
    }

    private fun setuprecycleview() {
        bindingtrailer = TrailerAdapter(arrayListOf(),object : TrailerAdapter.OnAdapterListener{
            override fun onLoad(key : String) {
                youtubekey = key
            }
            override fun onPlay(key : String) {
                youTubePlayer.loadVideo(key, 0f)
            }
        })
        binding.RcTrailer.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bindingtrailer
        }
    }

    override fun onStart() {
        super.onStart()
        getMovieTrailer()
    }
    private fun setupView(){
        supportActionBar!!.title = Constant.MOVIE_TITLE
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val youTubePlayerView = binding.youtubePlayerView
        lifecycle.addObserver(youTubePlayerView)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(player: YouTubePlayer) {
                youTubePlayer = player
                youtubekey?.let {
                    youTubePlayer.cueVideo(it,0f)
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
    private fun getMovieTrailer(){
        showLoading(true)
        ApiService().endPoint.getMovieTrailer(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<TrailerResponse>{
                override fun onResponse(
                    call: Call<TrailerResponse>,
                    response: Response<TrailerResponse>
                ) {
                    showLoading(false)
                    if(response.isSuccessful){
                        showTrailer(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<TrailerResponse>, t: Throwable) {
                    showLoading(false)
                }

            })

    }
    private fun showTrailer(trailer : TrailerResponse){
        bindingtrailer.setData( trailer.results)
    }
    private fun showLoading(loading:Boolean ){
        when(loading){
            true ->{
                binding.progresstrailer.visibility = View.VISIBLE
            }
            false ->{
                binding.progresstrailer.visibility = View.GONE
            }
        }

    }
}