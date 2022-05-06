package com.example.movietrailer.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.movietrailer.R
import com.example.movietrailer.databinding.ActivityDetailBinding
import com.example.movietrailer.databinding.ContentDetailBinding
import com.example.movietrailer.model.Constant
import com.example.movietrailer.model.DetailResponse
import com.example.movietrailer.retrofit.ApiService
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var bindingcontent : ContentDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        bindingcontent = binding.detailContent
        setContentView(binding.root)
        setupView()
        setuplistener()
    }



    override fun onStart() {
        super.onStart()
        getMovieDetail()
    }
    private fun setuplistener(){
        bindingcontent.fabPlay.setOnClickListener {
            startActivity(Intent(applicationContext, TrailerActivity::class.java))
        }
    }

    private fun setupView(){
        setSupportActionBar(findViewById(R.id.toolbar))

        supportActionBar!!.title = ""
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
    private fun getMovieDetail(){
        ApiService().endPoint.getMovieDetail(Constant.MOVIE_ID, Constant.API_KEY)
            .enqueue(object : Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful){
                        showMovie( response.body()!! )
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                }

            })
    }
    @SuppressLint("SetTextI18n")
    fun showMovie(detail: DetailResponse){
        val backdropPath = Constant.BACKDROP_PATH + detail.backdrop_path
        Picasso.get().load(backdropPath)
            .placeholder(R.drawable.img)
            .fit()
            .centerCrop()
            .error(R.drawable.img)
            .into( binding.posterimage )
        binding.toolbar.title = detail.title
        bindingcontent.textTitle.text = detail.title
        bindingcontent.textVote.text = detail.vote_average.toString()
        bindingcontent.textOverview.text = detail.overview

        for(genre in detail.genres!! ){
            bindingcontent.textGenre.text = "${genre.name} "
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }
}