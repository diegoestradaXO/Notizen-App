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
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.example.efpro.notizen.Dialog.ExampleDialog
import com.example.efpro.notizen.Dialog.WarningDialog
import com.example.efpro.notizen.R.id.home
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.example.efpro.notizen.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_emailpassword.*
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.list_item.*
import java.util.ArrayList

@Suppress("UNREACHABLE_CODE")
class navigate : AppCompatActivity() {

    // [START declare_auth]
    companion object {
        lateinit var auth: FirebaseAuth
        public var fragmentControl = 0
        public var fragmentRequested = 0
    }


    val manager = supportFragmentManager

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                fragmentRequested=0
                if(fragmentControl==1){
                    warningDialog()
                    if(fragmentControl==0){
                        fragmentControl=0
                        manager.beginTransaction().replace(R.id.fragment_container, home()).commit()
                    }
                }
                else{
                    manager.beginTransaction().replace(R.id.fragment_container, home()).commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.AddNote -> {
                if(fragmentControl!=1){
                    fragmentRequested=1
                    fragmentControl=1
                    manager.beginTransaction().replace(R.id.fragment_container, addNote()).commit()
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.Search -> {
                fragmentRequested=2
                if(fragmentControl==1){
                    warningDialog()
                    if(fragmentControl==2){
                        fragmentControl=2
                        manager.beginTransaction().replace(R.id.fragment_container, Search()).commit()
                    }
                }
                else{
                    manager.beginTransaction().replace(R.id.fragment_container, Search()).commit()
                }
                return@OnNavigationItemSelectedListener true
            }
        }
        return@OnNavigationItemSelectedListener false
    }

    fun warningDialog(){
        val warningDialog = WarningDialog()
        warningDialog.show(this.supportFragmentManager,"warning")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegate)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if(fragmentControl==0){
            manager.beginTransaction().replace(R.id.fragment_container, home()).commit()
        }
        else if(fragmentControl==2){
            manager.beginTransaction().replace(R.id.fragment_container, Search()).commit()
        }
        auth = FirebaseAuth.getInstance()

    }



}

