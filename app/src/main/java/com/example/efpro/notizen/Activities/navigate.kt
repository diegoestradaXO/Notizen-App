package com.example.efpro.notizen.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import com.example.efpro.notizen.R
import com.example.efpro.notizen.fragments.Search
import com.example.efpro.notizen.fragments.addNote
import kotlinx.android.synthetic.main.activity_navegate.*
import com.example.efpro.notizen.fragments.home
import com.example.efpro.notizen.Dialog.WarningDialog
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Suppress("UNREACHABLE_CODE")
class navigate : AppCompatActivity() {

    // [START declare_auth]
    companion object {
        lateinit var auth: FirebaseAuth
        public var fragmentControl = 0
        public var fragmentRequested = 0
        public var contenido = ""
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

    override fun onStart() {
        super.onStart()

        val reference = FirebaseDatabase.getInstance().getReference("notes")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*, *>
                NoteViewModel.Notes = mutableListOf()
                val it = nota.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    var nombre = ""
                    for (i in NoteViewModel.allUsers){
                        if(i.id== currentNote.get("userid").toString()){
                            nombre = i.email
                        }
                    }
                    if(currentNote.get("privacidad")=="false"){
                        val nota = Nota(currentNote.get("nombre") as String,
                            currentNote.get("descripcion") as String,
                            currentNote.get("etiquetas") as List<String>,
                            currentNote.get("versiones") as List<List<String>>,
                            currentNote.get("privacidad") as String,
                            currentNote.get("userid") as String
                        )
                        NoteViewModel.Notes.add(nota)
                    }
                }
            }

        })
        reference.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*,*>
                NoteViewModel.allNotes.clear()
                val it = nota.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    if(currentNote.get("userid")== navigate.auth.currentUser!!.uid){
                        val nota = Nota(currentNote.get("nombre") as String,
                            currentNote.get("descripcion") as String,
                            currentNote.get("etiquetas") as List<String>,
                            currentNote.get("versiones") as List<List<String>>,
                            currentNote.get("privacidad") as String,
                            currentNote.get("userid") as String
                        )
                        NoteViewModel.allNotes.add(nota)
                    }
                }
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegate)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        if(getIntent().getStringExtra("content")!=null){
            contenido = getIntent().getStringExtra("content")
            manager.beginTransaction().replace(R.id.fragment_container, addNote()).commit()
        }
        else if(fragmentControl==0){
            manager.beginTransaction().replace(R.id.fragment_container, home()).commit()
        }
        else if(fragmentControl==2){
            manager.beginTransaction().replace(R.id.fragment_container, Search()).commit()
        }
        auth = FirebaseAuth.getInstance()

    }



}

