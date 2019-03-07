package com.example.efpro.notizen.Activities

import android.app.Fragment
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.efpro.notizen.R
import com.example.efpro.notizen.R.id.AddNote
import com.example.efpro.notizen.fragments.Search
import com.example.efpro.notizen.fragments.addNote
import kotlinx.android.synthetic.main.activity_navegate.*
import com.example.efpro.notizen.fragments.home
import android.content.Intent
import android.content.pm.PackageManager

@Suppress("UNREACHABLE_CODE")
class navigate : AppCompatActivity() {

    val home:Fragment=com.example.efpro.notizen.fragments.home
    val manager = supportFragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                return@OnNavigationItemSelectedListener true
                manager.beginTransaction().replace(R.id.fragment_container,home).commit()
            }
            R.id.Search -> {
                return@OnNavigationItemSelectedListener true
                manager.beginTransaction().replace(R.id.fragment_container,Search).commit()
            }
            R.id.AddNote -> {
                return@OnNavigationItemSelectedListener true
                manager.beginTransaction().replace(R.id.fragment_container,addNote).commit()
            }
        }
            false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegate)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        manager.beginTransaction().replace(R.id.fragment_container,home).commit()
    }

}