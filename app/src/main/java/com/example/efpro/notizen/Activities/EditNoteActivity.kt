package com.example.efpro.notizen.Activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.efpro.notizen.R
import com.example.efpro.notizen.models.Nota
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.android.synthetic.main.activity_edit_user.*
import java.text.SimpleDateFormat
import java.util.*

class EditNoteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        var nombre = ""
        val idToExtract = getIntent().getStringExtra("identificador")
        var versiones : List<List<String>>?= null
        val referencia = FirebaseDatabase.getInstance().getReference("notes")
        referencia.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
            @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
            override fun onDataChange(p0: DataSnapshot) {
                val note =p0.getValue() as HashMap<*, *>
                val it = note.keys.iterator()//We iterate the hash
                while(it.hasNext()){
                    val key = it.next()
                    val currentNote = note.get(key) as HashMap<*,*>
                    val nota = Nota(
                        currentNote.get("nombre") as String,
                        currentNote.get("descripcion") as String,
                        currentNote.get("etiquetas") as List<String>,
                        currentNote.get("versiones") as List<List<String>>,
                        currentNote.get("privacidad") as String,
                        currentNote.get("userid") as String
                    )
                    if(idToExtract==nota.id){
                        editTitulo.setText(nota.nombre)
                        editdescription.setText(nota.descripcion)
                        var etiquetas = ""
                        for (etiqueta in nota.etiquetas){
                            etiquetas=etiquetas+etiqueta+","
                        }
                        tags.setText(etiquetas)
                        switch2.isChecked = nota.privacidad=="true"
                        nombre = nota.nombre
                        versiones = nota.versiones
                        editcontent.setText(nota.versiones[versiones!!.size - 1][0])
                    }
                }
            }

        })
        actualizar.setOnClickListener{
            val database = FirebaseDatabase.getInstance().reference
            database.child("notes").child(nombre+navigate.auth.currentUser!!.uid).setValue(null)
            val newVersions = versiones as MutableList
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            val newOne = listOf<String>(editcontent.text.toString(),currentDate)
            newVersions.add(newOne)
            var privacidad = "false"
            if (switch2.isChecked){
                privacidad="true"
            }
            val nota = Nota(editTitulo.text.toString(),editdescription.text.toString(),tags!!.text.toString().split(",".toRegex()),newVersions as List<List<String>>,privacidad,
                navigate.auth.currentUser!!.uid)
            database.child("notes").child(nota.nombre+navigate.auth.currentUser!!.uid).setValue(nota)
            val intento = Intent(this, ViewNoteActivity::class.java)//Redirigimos a la viewnoteactivity
            intento.putExtra("content",nota.versiones[nota.versiones.size-1][0])
            intento.putExtra("identificador",nota.id)
            intento.putExtra("correo", navigate.auth.currentUser!!.email)
            intento.putExtra("titulo",nota.nombre)
            intento.putExtra("descripcion",nota.descripcion)
            startActivity(intento)
            this.finish()
        }
    }
}
