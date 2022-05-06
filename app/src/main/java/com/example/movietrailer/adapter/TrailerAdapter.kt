package com.example.movietrailer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.movietrailer.R
import com.example.movietrailer.databinding.AdapterMainBinding
import com.example.movietrailer.databinding.AdapterTrailerBinding
import com.example.movietrailer.model.Constant
import com.example.movietrailer.model.MovieModel
import com.example.movietrailer.model.TrailerModel
import com.squareup.picasso.Picasso

class TrailerAdapter(var videos : ArrayList<TrailerModel>, var listener: OnAdapterListener) : RecyclerView.Adapter<TrailerAdapter.ViewHolder>() {

    private lateinit var binding: AdapterTrailerBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)= ViewHolder (
        AdapterTrailerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
    )
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val video = videos[position]
        holder.bind(video)

        holder.view.textVideos.setOnClickListener {
            listener.onPlay(video.key!!)
        }


    }
    override fun getItemCount() = videos.size
    //view holder adapter
    class ViewHolder(view : AdapterTrailerBinding) : RecyclerView.ViewHolder(view.root) {
        val view = view
        fun bind(videos : TrailerModel){
            view.textVideos.text = videos.name

        }

    }
    public fun setData(newvideos : List<TrailerModel> ){
        videos.clear()
        videos.addAll(newvideos)
        notifyDataSetChanged()
//        listener.onLoad(newvideos[0].key!!)

    }
    interface OnAdapterListener{
        fun onLoad(key: String)
        fun onPlay(key: String)
    }
}