package com.example.efpro.notizen.Activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.efpro.notizen.Dialog.WarningDeleteDialog
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_view_note.*

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
        buttonShare.setOnClickListener{
            val emailIntent = Intent(Intent.ACTION_SEND)
            //Making the intent for email
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"
            emailIntent.putExtra(Intent.EXTRA_EMAIL, correo)
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Hello:\n I have just found this note named $titulo, please read it:\n\n $contenido \n\n\n\n")
            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                Toast.makeText(this,"Sending mail", Toast.LENGTH_LONG).show()
            } catch (ex: android.content.ActivityNotFoundException) {
                Toast.makeText(this, "Unexpected Error", Toast.LENGTH_LONG).show()
            }
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
            intent.putExtra("identificador",id)
            startActivity(intent)
            this.finish()
        }


        if(correo!=navigate.auth.currentUser!!.uid){
            val intent = Intent(this, OtherViewNoteActivity::class.java)
            intent.putExtra("content", contenido)
            intent.putExtra("titulo",titulo)
            intent.putExtra("correo",correo)
            intent.putExtra("descripcion",descripcion)
            intent.putExtra("identificador",id)
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
