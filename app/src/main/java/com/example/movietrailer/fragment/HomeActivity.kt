package com.example.movietrailer.fragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.movietrailer.adapter.TabAdapter
import com.example.movietrailer.databinding.ActivityHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val tabadapter = TabAdapter(supportFragmentManager, lifecycle)
        binding.videPager.adapter = tabadapter

        val tabtitles = arrayOf("Popular","Now Playing")
        TabLayoutMediator(binding.tabLayoutHome, binding.videPager){
            tab, position -> tab.text = tabtitles[position]
        }.attach()
    }
}