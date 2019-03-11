package com.example.efpro.notizen.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.example.efpro.notizen.Dialog.WarningDeleteDialog
import com.example.efpro.notizen.R
import com.example.efpro.notizen.ViewHolder.NoteViewModel
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view_note.*
import kotlinx.android.synthetic.main.add_dialog.*

class ViewNoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_note)

        val contenido = getIntent().getStringExtra("content")
        val id = getIntent().getStringExtra("identificador")
        val correo=getIntent().getStringExtra("correo")
        val titulo = getIntent().getStringExtra("titulo")
        val descripcion = getIntent().getStringExtra("descripcion")

        buttonHome.setOnClickListener{
            this.finish()
        }

        buttonTrash.setOnClickListener{
            val reference = FirebaseDatabase.getInstance().getReference("notes")
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
                        if(currentNote.get("id").toString() == id){
                            val nota = Nota(
                                currentNote.get("nombre") as String,
                                currentNote.get("descripcion") as String,
                                currentNote.get("etiquetas") as List<String>,
                                currentNote.get("versiones") as List<List<String>>,
                                currentNote.get("privacidad") as String,"x")
                            warningDeleteDialog(nota)
                        }
                    }
                }

            })
        }

        buttonEdit.setOnClickListener{
            val intent = Intent(this, EditNoteActivity::class.java)
            intent.putExtra("content", contenido)
            startActivity(intent)
            this.finish()
        }


        if(correo!=navigate.auth.currentUser!!.uid){
            val intent = Intent(this, OtherViewNoteActivity::class.java)
            intent.putExtra("content", contenido)
            intent.putExtra("titulo",titulo)
            intent.putExtra("correo",correo)
            intent.putExtra("descripcion",descripcion)
            startActivity(intent)
            this.finish()
        }
        val reference = FirebaseDatabase.getInstance().getReference("notes")
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
                    if(currentNote.get("id").toString() == id){
                        val versiones = currentNote["versiones"] as List<List<String>>
                        var index = versiones.size - 1
                        val contenido = versiones[index]
                        content.setText(contenido[0])
                        tittle.setText(currentNote.get("nombre").toString())
                        descrip.setText(currentNote.get("descripcion").toString())
                    }
                }
            }

        })
    }
    fun warningDeleteDialog(get: Nota) {
        val warningDeleteDialog = WarningDeleteDialog(get)
        warningDeleteDialog.show(this.supportFragmentManager,"example dialog")
    }
}