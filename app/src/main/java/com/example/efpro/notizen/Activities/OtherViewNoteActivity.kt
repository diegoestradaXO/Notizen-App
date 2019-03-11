package com.example.efpro.notizen.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_other_view_note.*

class OtherViewNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_other_view_note)
        val mail=getIntent().getStringExtra("correo")
        val titulo = getIntent().getStringExtra("titulo")
        val description = getIntent().getStringExtra("descripcion")
        val contenido = getIntent().getStringExtra("content")
        buttonHome.setOnClickListener{
            this.finish()
        }
        buttonShare.setOnClickListener{
            //Broadcast Reciever
        }

        tittle.text = titulo
        descripcion.text = description
        content.text = contenido
        val reference = FirebaseDatabase.getInstance().getReference("users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            override fun onDataChange(p0: DataSnapshot) {
                val nota =p0.getValue() as HashMap<*, *>
                val it = nota.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = nota.get(key) as HashMap<*,*>
                    if(currentNote.get("id").toString() == mail){
                        correo.text = currentNote.get("nombre").toString() + " | " + currentNote.get("e mail").toString()
                    }
                }
            }

        })

    }
}
