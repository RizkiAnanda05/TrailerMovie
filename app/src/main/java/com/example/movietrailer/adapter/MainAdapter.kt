package com.example.movietrailer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietrailer.R
import com.example.movietrailer.databinding.AdapterMainBinding
import com.example.movietrailer.model.Constant
import com.example.movietrailer.model.MovieModel
import com.squareup.picasso.Picasso

class MainAdapter(var movies : ArrayList<MovieModel>,var listener: OnAdapterListener) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    lateinit var binding: AdapterMainBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
        AdapterMainBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
        val posterPath = Constant.POSTER_PATH+movie.poster_path

        //Picasso
        Picasso.get().load(posterPath)
            .placeholder(R.drawable.img)
            .error(R.drawable.img)
            .into( holder.view.poster )
        holder.view.poster.setOnClickListener {
            Constant.MOVIE_ID = movie.id!!
            Constant.MOVIE_TITLE = movie.title!!
            listener.onClick(movie)
        }
    }
    override fun getItemCount() = movies.size
    //view holder adapter
    class ViewHolder(binding : AdapterMainBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding
        fun bind(movies : MovieModel){
            view.movies.text = movies.title

        }

    }
    public fun setData(newMovies : List<MovieModel> ){
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()

    }
    public fun setDataNextPage(newMovies : List<MovieModel> ){
        movies.addAll(newMovies)
        notifyDataSetChanged()

    }
    interface OnAdapterListener{
        fun onClick(movies: MovieModel){

        }
    }
}