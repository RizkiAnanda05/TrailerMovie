package com.example.movietrailer.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.movietrailer.R
import com.example.movietrailer.fragment.HomeActivity

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar!!.hide()

        Handler().postDelayed({
            startActivity(Intent(this, HomeActivity::class.java) )
            finish()
        }, 2000)
    }
}