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
import android.content.Intent.*
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.efpro.notizen.R.id.home
import com.example.efpro.notizen.ViewModel.NoteViewModel
import com.example.efpro.notizen.data.User.User
import com.example.efpro.notizen.models.ApplicationExt
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item.*
import java.util.ArrayList

@Suppress("UNREACHABLE_CODE")
class navigate : AppCompatActivity() {


    companion object {
        var currentid: Int =0
        lateinit var noteViewModel: NoteViewModel

    }

    val manager = supportFragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                manager.beginTransaction().replace(R.id.fragment_container, home()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.Search -> {
                manager.beginTransaction().replace(R.id.fragment_container, Search()).commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.AddNote -> {
                manager.beginTransaction().replace(R.id.fragment_container, addNote()).commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    fun getUser(id:Int): User? {
        for (user in ApplicationExt.contactlist){
            return user
            /*Toast.makeText(this,"heeee"+user.toString(),Toast.LENGTH_SHORT).show()
            if(id==user.id){
                return user
            }

        return null*/
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegate)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        manager.beginTransaction().replace(R.id.fragment_container, home()).commit()
        val myUser: User = this.getUser(ApplicationExt.currentid)!!
        followers.setText(myUser.followers.length)
        following.setText(myUser.following.length)

    }

}
